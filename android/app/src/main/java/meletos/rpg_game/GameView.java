package meletos.rpg_game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import meletos.rpg_game.characters.BouncingCharacter;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Follower;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.file_io.LevelHandler;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.JoyStick;
import meletos.rpg_game.navigation.NavigationArrows;

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
    JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),BitmapFactory.decodeResource(getResources(),R.drawable.ring));
    Hero hero;
    private ArrayList<Button> buttons;

    public GameView(Context context) {
        super(context);
        FatherCharacter[] characters =
                {
                        new RandomWalker(100, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
                        new Follower(500, 100, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
                        new BouncingCharacter(500, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
                        new Hero(700, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin))
                };
        hero = (Hero) characters[3];
        hero.setJoystick(js);
        gameHandler = new GameHandler(characters);

        //LevelHandler lvlHandler = new LevelHandler("first_file", context);
        //lvlHandler.serialiseLevel(gameHandler);
        //gameHandler = lvlHandler.deserialiseLevel();

        getHolder().addCallback(this);
        gameThread = new GameThread(gameHandler);
        viewThread = new MainThread(getHolder(), this);
        setFocusable(true);
        exampleButton = new Button(1000, 100, BitmapFactory.decodeResource(getResources(),R.drawable.coin));

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.WHITE);
            gameHandler.drawGame(canvas);
            exampleButton.draw(canvas);
            js.draw(canvas);
        }
    }

    /**
     * right now only demonstrating usage of touchevents -- pauses game on touch
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
            if (exampleButton.isTouched((int)event.getX(), (int)event.getY())) {
                gameHandler.pauseGame();
            } else {
                js.setUsed(true);
                js.setBase(event.getX(), event.getY());
            }
        }
        if (event.getAction() == ACTION_UP) {
            js.setUsed(false);
            gameHandler.resumeGame();
        }
        if (js.used) {
            js.setPos(event.getX(), event.getY());
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
}
