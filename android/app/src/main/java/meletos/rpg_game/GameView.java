package meletos.rpg_game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import meletos.rpg_game.battle.BattleGUI;
import meletos.rpg_game.dialog.Dialog;
import meletos.rpg_game.file_io.FileManager;
import meletos.rpg_game.file_io.FileScout;
import meletos.rpg_game.file_io.LevelGenerator;
import meletos.rpg_game.inventory.InventoryGUI;
import meletos.rpg_game.inventory.itinerary.Itinerary;
import meletos.rpg_game.menu.MainMenu;
import meletos.rpg_game.menu.Menu;
import meletos.rpg_game.menu.MenuStates;
import meletos.rpg_game.menu.Settings;
import meletos.rpg_game.navigation.JoyStick;
import meletos.rpg_game.sound.Sound;
import meletos.rpg_game.text.Text;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * The main surface of the game.
 * SurfaceHolder.Callback -- enables to catch events.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    //screen size
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private GameHandler gameHandler;
    private final Settings settings;
    private MainThread viewThread;
    private GameThread gameThread;
    private final JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),
            BitmapFactory.decodeResource(getResources(),R.drawable.ring));

    // states used to tell apart different stages of game
    private State state = State.MAIN_MENU;
    private State prevState = State.MENU;

    private final MainMenu mainMenu;
    private Menu menu;
    private final Text text;
    private final Sound sound;
    private InventoryGUI inventory;
    private BattleGUI battle;
    private Dialog dialog;
    private Endgame endgame;
    private final Loading loading;
    private boolean hasGameHandler = false;
    private final Itinerary itinerary;
    private final FileManager fileManager;
    private long loadingCheck;

    private Boolean init = true; // used to recognise initiation
    private final String TAG = "GameView";
    private final char logSensitivity;
    public boolean screenshotTaken = false;

    /**
     * Starts up the game -- the main menu
     * @param context of app
     * @param text class for viewing test
     */
    public GameView(Context context, Text text) {
        super(context);
        logSensitivity = getLogSensitivity();
        loading = new Loading(this);
        this.text = text;
        this.text.setGameView(this);
        fileManager = new FileManager(context, this);
        itinerary = Itinerary.load(context, text, "itinerary/items.json");
        sound = new Sound(context);
        settings = new Settings(text, sound, context);
        mainMenu = new MainMenu(this, context, text, settings);
        getHolder().addCallback(this);
        viewThread = new MainThread(getHolder(), this);
        setFocusable(true);
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
                Log.e(TAG, e.getMessage());
            }
            retry = false;
        }
        gameHandler.recycleBitmaps();
        hasGameHandler = false;
        gameThread = null;
        init = true;
        gameHandler = null;
    }

    /**
     * Function that draws entities
     * @param canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            if (prevState != state) {
                prevState = state;
                sound.play(state);
            }
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
                case LOADING:
                    loading.draw(canvas);
                    break;
            }
        }
    }

    /**
     * All the management of touch events
     * @param event touch event to handle
     * @return true on success
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
                    if (!inventory.touchDown((int)event.getX(), (int)event.getY())) {
                        js.setUsed(true);
                        js.setBase(event.getX(), event.getY());
                    }
                }
                if (event.getAction() == ACTION_UP) {
                    js.setUsed(false);
                    inventory.touchUp((int)event.getX(), (int)event.getY());
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
                    inventory.touchDown((int)event.getX(), (int)event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    inventory.touchUp((int)event.getX(), (int)event.getY());
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
     * Resumes the game when user navigates back
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
                Log.e(TAG, e.getMessage());
            }
            retry = false;
        }
        viewThread = null;
        gameThread = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        saveLog(logSensitivity);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        viewThread.canUseSurfaceHolder(true);
        saveLog(logSensitivity);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        viewThread.canUseSurfaceHolder(false);
        saveLog(logSensitivity);
    }

    public State getState() {
        return state;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Sets state and takes care of the logic surrounding it
     * @param state to set
     */
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

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     * Takes screenshot and saves it. Not used at th moment
     * @param path to save into
     */
    public void takeScreenshot (String path) {
        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas screenShot = new Canvas(b);
        draw(screenShot);
        screenshotTaken = true;
        OutputStream out = null;
        try {
            File imageFile = new File(path);
            out = new FileOutputStream(imageFile);
            double ratio = (double)getHeight()/getWidth();
            double imgHeight = screenHeight/12.305; // z toho ratio -- vynasobit scrrenwidth
            double imgWidth = ratio*imgHeight;
            b = Bitmap.createScaledBitmap(b, (int)imgHeight, (int)imgWidth, false);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Log.i("TakeScreenshot", "Saved screenshot to: " + path);
            out.flush();
            b.recycle();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Sets game handler. Effectively starts game. Gets called
     * from different threads, so it implements some checks
     * @param gH gameHandler to be set
     * @param storyPath so that it can be erased if needed
     */
    public void setGameHandler(GameHandler gH, String storyPath) {
        if(Thread.currentThread().getId() != loadingCheck) {
            Log.i(TAG, "Deleting unused save.");
            FileScout.deleteStory(storyPath); // delete unused save
            return;
        }
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
        hasGameHandler = true;
        init = false;
        saveLog(logSensitivity);
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

    public Sound getSound() {
        return sound;
    }

    /**
     * Function that saves logs into file.
     * @param sensitivity char - what to put into file
     */
    private void saveLog(char sensitivity) {
        String filename = Environment.getExternalStorageDirectory() + "/rpg_game_data/rpg_game.log";
        String printCommand = "logcat -d *:" + sensitivity;

        try {
            Process process = Runtime.getRuntime().exec(printCommand);

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try{
                File file = new File(filename);
                if (file.getTotalSpace() > 1000000) cleanLogs(); // clean old logs
                FileWriter writer = new FileWriter(file);
                while((line = in.readLine()) != null){
                    writer.write(line + "\n");
                }
                writer.flush();
                writer.close();
            }
            catch(IOException e){
                Log.e(TAG, e.getMessage());
            }
        }
        catch(IOException e){
            Log.e(TAG, e.getMessage());;
        }
    }

    /**
     * Cleans old logs.
     */
    private void cleanLogs () {
        String clearCommand = "logcat -c";
        try {
            Runtime.getRuntime().exec(clearCommand); // clears logcat of old logs
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
        }
    }

    /**
     * Gets log sensitivity from a file.
     * @return char sensitivity to set
     */
    private char getLogSensitivity() {
        String sensitivityPath = Environment.getExternalStorageDirectory().toString() + "/rpg_game_data/sensitivity.txt";
        File file = new File(sensitivityPath);
        try {
            if (file.createNewFile()) {
                FileManager.saveFile(sensitivityPath, "E");
            }
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
        }
        String data = FileManager.loadFile(sensitivityPath);
        char[] array = data.toCharArray();
        return array[0];
    }

    /**
     * Sets loading check.
     * @param loadingCheck ID of a thread that began loading
     */
    public synchronized void setLoadingCheck(long loadingCheck) {
        this.loadingCheck = loadingCheck;
    }
}
