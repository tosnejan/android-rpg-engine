package meletos.rpg_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import meletos.rpg_game.battle.BattleGUI;
import meletos.rpg_game.dialog.Dialog;
import meletos.rpg_game.file_io.FileManager;
import meletos.rpg_game.inventory.InventoryGUI;
import meletos.rpg_game.inventory.itinerary.Itinerary;
import meletos.rpg_game.menu.MenuStates;
import meletos.rpg_game.menu.Settings;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.JoyStick;
import meletos.rpg_game.menu.MainMenu;
import meletos.rpg_game.menu.Menu;
import meletos.rpg_game.sound.Sound;
import meletos.rpg_game.text.Text;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * The main surface of the game
 * SurfaceHolder.Callback -- enables to catch events
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameHandler gameHandler;
    private Settings settings;
    private MainThread viewThread;
    public GameThread gameThread;
    JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),
            BitmapFactory.decodeResource(getResources(),R.drawable.ring));
    private ArrayList<Button> buttons;
    private State state = State.MAIN_MENU;
    private MainMenu mainMenu;
    private Menu menu;
    private Text text;
    public Sound sound;
    private InventoryGUI inventory;
    private BattleGUI battle;
    private Dialog dialog;
    private Endgame endgame;
    private boolean hasGameHandler = false;
    private boolean isInLevel;
    private Itinerary itinerary;
    private FileManager fileManager;

    private Boolean init = true; // used to recognise initiation


    /**
     * Starts up the game -- the main menu
     * @param context
     * @param text
     */
    public GameView(Context context, Text text) {
        super(context);
        this.text = text;
        this.text.setGameView(this);
        fileManager = new FileManager(context, this);
        itinerary = Itinerary.load(context, text, "itinerary/items.json");
        sound = new Sound(context);
        sound.play(state);
        settings = new Settings(text, sound, context);
        mainMenu = new MainMenu(this, context, text, settings);
        getHolder().addCallback(this);
        viewThread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    /** TODO obsolete -- will not be here
     * Loads a specific level -- quite buggy at the moment
     * @param filePath name and path of file to read
     * @param userSave <code>true</code> if loading userSave
     *                 <code>false</code> if loading level
     */
    public void loadLevel (String filePath, boolean userSave) {
        hasGameHandler = false;
        //Loader loader = new Loader(this, filePath, userSave);
        //loader.start();
    }
    
    /**
     * Is triggered
     * when entering the menu and leaving the app
     */
    public void exitLevel () {
        init = true;
        gameHandler.pauseGame();
        state = State.MAIN_MENU;
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        hasGameHandler = false;
        gameThread = null;
        gameHandler = null;
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            switch (state) {
                case MAIN_MENU:
                    mainMenu.draw(canvas);
                    break;
                case MAP:
                    gameHandler.drawGame(canvas);
                    inventory.draw(canvas);
                    js.draw(canvas);
                    break;
                case MENU:
                    gameHandler.drawGame(canvas);
                    menu.draw(canvas);
                    break;
                case BATTLE:
                    battle.draw(canvas);
                    break;
                case INVENTORY:
                    inventory.draw(canvas);
                    break;
                case ENDGAME:
                    endgame.draw(canvas);
                    break;
                case DIALOG:
                    gameHandler.drawGame(canvas);
                    dialog.draw(canvas);
                    break;
            }
        }
    }

    /**
     * All the management of touch events
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (state) {
            case MAIN_MENU:
                if (event.getAction() == ACTION_DOWN) {
                    mainMenu.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    mainMenu.touchUp((int)event.getX(), (int)event.getY());
                }
                break;
            case MAP:
                if (event.getAction() == ACTION_DOWN) {
                    if (!inventory.buttonTouchedDown((int)event.getX(), (int)event.getY())) {
                        js.setUsed(true);
                        js.setBase(event.getX(), event.getY());
                    }
                }
                if (event.getAction() == ACTION_UP) {
                    js.setUsed(false);
                    inventory.buttonTouchedUp((int)event.getX(), (int)event.getY());
                    //gameHandler.resumeGame();
                }
                if (js.used) {
                    js.setPos(event.getX(), event.getY());
                }
                break;
            case MENU:
                if (event.getAction() == ACTION_DOWN) {
                    menu.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    menu.touchUp((int)event.getX(), (int)event.getY());
                }
                break;
            case BATTLE:
                if (event.getAction() == ACTION_DOWN) {
                    battle.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    battle.touchUp((int)event.getX(), (int)event.getY());
                }
                break;
            case INVENTORY:
                if (event.getAction() == ACTION_DOWN) {
                    inventory.buttonTouchedDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    inventory.buttonTouchedUp((int)event.getX(), (int)event.getY());
                }
                break;
            case ENDGAME:
                if (event.getAction() == ACTION_DOWN) {
                    endgame.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    endgame.touchUp((int)event.getX(), (int)event.getY());
                }
                break;
            case DIALOG:
                if (event.getAction() == ACTION_DOWN) {
                    dialog.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    dialog.touchUp((int)event.getX(), (int)event.getY());
                }
                break;
        }
        return true;
    }

    /**
     * Starts up the threads when game is created.
     */
    public void onCreate() {
        viewThread.setRunning(true);
        viewThread.start();
    }

    /**
     * Called when user exits the game by pressing home button
     */
    public void onPause () {
        sound.killSounds();
        if (state == State.MAP) gameHandler.pauseGame();
    }

    /**
     * resumes the game when user navigates back
     */
    public void onResume () {
        sound.play(state);
        if (state == State.MAP) gameHandler.resumeGame();

    }

    /**
     * Called upon pressing the back button or generally when system wants to kill the app
     * It kills he threads.
     */
    public void onDestroy () {
        boolean retry = true;
        while (retry) {
            try {
                if (state == State.MAP || state == State.INVENTORY || state == State.MENU) {
                    gameThread.setRunning(false);
                    gameThread.join();
                }
                viewThread.setRunning(false);
                viewThread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        viewThread = null;
        gameThread = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewThread.canUseSurfaceHolder(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        viewThread.canUseSurfaceHolder(false);
    }

    public State getState() {
        return state;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setState(State state) {
        if (state == State.INVENTORY || state == State.MENU || state == State.DIALOG || state == State.ENDGAME) {
            gameHandler.pauseGame();
        } else if (state == State.MAP && this.state == State.MENU) {
            gameHandler.resumeGame();
            menu.setState(MenuStates.MAIN);
            js.setUsed(false);
        } else if (state == State.MAP) {
            gameHandler.resumeGame();
            js.setUsed(false);
        } else if (state == State.BATTLE){
            gameHandler.pauseGame();
            battle.init();
        }
        this.state = state;
    }

    /**
     * Takes screenshot and saves it
     * @param filename
     */
    public void takeScreenshot (String filename) {
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas screenShot = new Canvas(b);
        draw(screenShot);

        String path = Environment.getExternalStorageDirectory().toString() + "/rpg_game_data/" + filename;
        OutputStream out = null;
        try {
            File imageFile = new File(path);
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(imageFile);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameHandler(GameHandler gH) {
        gameHandler = gH;
        gameHandler.pauseGame();
        gameHandler.setGameView(this);
        gameHandler.setJoystickToHero(js);
        inventory = new InventoryGUI(this, getContext(), gameHandler, text, itinerary);
        gameHandler.getInventory().setItinerary(itinerary);
        gameHandler.setText(text);
        menu = new Menu(gameHandler, this, getContext(), text, settings);
        battle = new BattleGUI(gameHandler, this, getContext());
        text.loadDialogs();
        dialog = new Dialog(gameHandler, this, text);
        endgame = new Endgame(gameHandler);
        if (init) {
            gameThread = new GameThread(gameHandler);
            gameThread.start();
        } else {
            gameThread.setNewGameHandler(gameHandler);
        }
        //gameHandler.resumeGame(); //Možná pak torchu pozměnit, kdyby se to využívalo i na přechod mezi mapama.
        hasGameHandler = true;
        init = false;
    }

    public boolean hasGameHandler() {
        return hasGameHandler;
    }

    public void setHasGameHandler(boolean hasGameHandler) {
        this.hasGameHandler = hasGameHandler;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Endgame getEndgame() {
        return endgame;
    }
}
