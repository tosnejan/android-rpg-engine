package meletos.rpg_game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.JoyStick;
import meletos.rpg_game.navigation.MainMenu;
import meletos.rpg_game.navigation.Menu;
import meletos.rpg_game.text.Text;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

/**
 * The main surface of the game
 * SurfaceHolder.Callback -- enables to catch events
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameHandler gameHandler;

    private MainThread viewThread;
    private GameThread gameThread;
    private Button exampleButton;
    JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),
            BitmapFactory.decodeResource(getResources(),R.drawable.ring));
    Hero hero;
    private ArrayList<Button> buttons;
    private State state = State.MAIN_MENU;
    private MainMenu mainMenu;
    private Menu menu;
    private Text text;

    public GameView(Context context, Text text) {
        super(context);
        this.text = text;
        FatherCharacter[] characters =
                {
                        new Hero(100, 500, "characters/warrior_m", context),
                        new RandomWalker(100, 800, "characters/ninja_m", context),
                        new RandomWalker(500, 1000, "characters/townfolk1_f", context),
                        //new BouncingCharacter(500, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin))
                };
        hero = (Hero) characters[0];
        hero.setJoystick(js);
        gameHandler = new GameHandler(characters, context, this);
        mainMenu = new MainMenu(this, context, text, gameHandler);
        menu = new Menu(this, context, text, gameHandler);

        //LevelHandler lvlHandler = new LevelHandler("first_file", context);
        //lvlHandler.serialiseLevel(gameHandler);
        //gameHandler = lvlHandler.deserialiseLevel();

        getHolder().addCallback(this);
        gameHandler.pauseGame();
        gameThread = new GameThread(gameHandler);
        viewThread = new MainThread(getHolder(), this);
        setFocusable(true);
        //exampleButton = new Button(1000, 100, BitmapFactory.decodeResource(getResources(),R.drawable.coin));

        //Gson gs = new Gson();
        //String res = gs.toJson(gameHandler);
        //GameHandler gH = new GsonBuilder().create().fromJson(res, GameHandler.class);
        //System.out.println(res);
    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            if (state == State.MAIN_MENU){
                mainMenu.draw(canvas);
            //} else if(state == State.FIGHT) {
            } else {
                canvas.drawColor(Color.WHITE);
                gameHandler.drawGame(canvas);
                //exampleButton.draw(canvas);
                switch (state) {
                    case MAP:
                        js.draw(canvas);
                        break;
                    case MENU:
                        menu.draw(canvas);
                        break;
                    case INVENTORY:
                        break;
                }
            }
        }
    }

    /**
     * right now only demonstrating usage of touchevents -- pauses game on touch
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
                    /*if (exampleButton.isTouched((int)event.getX(), (int)event.getY())) {
                        gameHandler.pauseGame();
                    } else {
                        js.setUsed(true);
                        js.setBase(event.getX(), event.getY());
                    }*/
                    js.setUsed(true);
                    js.setBase(event.getX(), event.getY());
                }
                if (event.getAction() == ACTION_UP) {
                    js.setUsed(false);
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
        gameThread.start();
    }

    /**
     * Called when user exits the game by pressing home button
     */
    public void onPause () {
        gameHandler.pauseGame();
    }

    /**
     * resumes the game when user navigates back
     */
    public void onResume () {
        gameHandler.resumeGame();
    }

    /**
     * Called upon pressing the back button or generally when system wants to kill the app
     * It kills he threads.
     */
    public void onDestroy () {
        boolean retry = true;
        while (retry) {
            try {
                viewThread.setRunning(false);
                gameThread.setRunning(false);
                viewThread.join();
                gameThread.join();
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
        if (state == State.MENU) {
            gameHandler.pauseGame();
        }
    }
}
