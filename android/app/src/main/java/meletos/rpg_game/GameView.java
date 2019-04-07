package meletos.rpg_game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import meletos.rpg_game.inventory.InventoryGUI;
import meletos.rpg_game.inventory.itinerary.Itinerary;
import meletos.rpg_game.menu.Settings;
import meletos.rpg_game.file_io.LevelGenerator;
import meletos.rpg_game.file_io.UnsupportedTypeException;
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
    private GameThread gameThread;
    private Button exampleButton;
    JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),
            BitmapFactory.decodeResource(getResources(),R.drawable.ring));
    private ArrayList<Button> buttons;
    private State state = State.MAIN_MENU;
    private MainMenu mainMenu;
    private Menu menu;
    private Text text;
    private Sound sound;
    private InventoryGUI inventory;
    private boolean isInLevel;
    private Itinerary itinerary;

    /**
     * Starts up the game -- the main menu
     * @param context
     * @param text
     */
    public GameView(Context context, Text text) {
        super(context);
        this.text = text;
        itinerary = Itinerary.load(context, text, "itinerary/items.json");
        sound = new Sound(context);
        settings = new Settings(text, sound, context);
        mainMenu = new MainMenu(this, context, text, settings);
        getHolder().addCallback(this);
        viewThread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    /**
     * Loads a specific level -- quite buggy at the moment
     * @param filePath name and path of file to read
     * @param userSave <code>true</code> if loading userSave
     *                 <code>false</code> if loading level
     */
    public void loadLevel (String filePath, boolean userSave) {

        try {
            LevelGenerator lvlGenerator = new LevelGenerator(getContext(), filePath);
            gameHandler = lvlGenerator.buildLevel(userSave);
        } catch (UnsupportedTypeException e) {
            e.printStackTrace();
        }
        gameHandler.setGameView(this);
        gameHandler.setJoystickToHero(js);
        gameThread = new GameThread(gameHandler);
        inventory = new InventoryGUI(this, getContext(), gameHandler, text, itinerary);
        menu = new Menu(gameHandler, this, getContext(), text, settings);
        gameThread.start();
    }


    /**
     * Is triggered
     * when entering the menu and leaving the app
     */
    public void exitLevel () {
        gameHandler.pauseGame();
        gameHandler.saveGameState();
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
        gameThread = null;
        gameHandler = null;
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            if (state == State.MAIN_MENU){
                mainMenu.draw(canvas);
                //} else if(state == State.FIGHT) {
            } else {
                //canvas.drawColor(Color.WHITE);
                gameHandler.drawGame(canvas);
                switch (state) {
                    case MAP:
                        inventory.draw(canvas);
                        js.draw(canvas);
                        break;
                    case MENU:
                        menu.draw(canvas);
                        break;
                    case INVENTORY:
                        inventory.draw(canvas);
                        break;
                }
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
            case FIGHT:
                break;
            case INVENTORY:
                if (event.getAction() == ACTION_DOWN) {
                    inventory.buttonTouchedDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    inventory.buttonTouchedUp((int)event.getX(), (int)event.getY());
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
        if (state == State.MAP) gameHandler.pauseGame();
    }

    /**
     * resumes the game when user navigates back
     */
    public void onResume () {
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        if (state == State.INVENTORY || state == State.MENU) {
            gameHandler.pauseGame();
        } else if (state == State.MAP) {
            gameHandler.resumeGame();
        }
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
}
