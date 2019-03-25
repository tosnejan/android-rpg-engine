package meletos.rpg_game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;

import meletos.rpg_game.characters.BouncingCharacter;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Follower;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
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
    private FatherCharacter[] characters =
            {
            new RandomWalker(100, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
            new Follower(500, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
            new BouncingCharacter(500, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
            new Hero(700, 800, BitmapFactory.decodeResource(getResources(),R.drawable.coin))
    };

    private MainThread thread;
    private GameThread gameThread;
    private Button exampleButton;
    private NavigationArrows navigationArrows;
    JoyStick js = new JoyStick(BitmapFactory.decodeResource(getResources(),R.drawable.circle),BitmapFactory.decodeResource(getResources(),R.drawable.ring));
    Hero hero = (Hero)characters[3];
    private ArrayList<Button> buttons;

    public GameView(Context context) {
        super(context);
        hero.setJoystick(js);
        gameHandler = new GameHandler(characters);
        getHolder().addCallback(this);
        gameThread = new GameThread(gameHandler);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        exampleButton = new Button(1000, 100, BitmapFactory.decodeResource(getResources(),R.drawable.coin));
        navigationArrows = new NavigationArrows(this);
        //LevelGenerator.generateAndSaveLevel(characters, "../../../assets/lvl/attempt.json"); doesnt work yet :D
        //characters = LevelInterpreter.getLevel("raw/attempt.json");

    }

    @Override
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.WHITE);
            gameHandler.drawGame(canvas);
            //navigationArrows.draw(canvas);
            exampleButton.draw(canvas);
            js.draw(canvas);
            System.out.println(js.getDirection());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    /**
     * starts up the thread
     */
    public void surfaceCreated(SurfaceHolder holder) {
        //thread.setRunning(true);

        //thread.start();

        //gameThread.start();

    }

    @Override
    /**
     * destroys the surface -- might take more attempts, hence the while loop
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                //thread.setRunning(false);
                //thread.join();
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        gameThread = null;
    }

    /**
     * right now only demonstrating usage of touchevents -- pauses game on touch
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == ACTION_DOWN) {
            //gameHandler.pauseGame();
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
        /*System.out.println("x: " + event.getX());
        System.out.println("y: " + event.getY());*/
        return true;
    }

    public void onPause () {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
        thread = null;
    }

    public void onResume () {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
        gameThread.start();

    }
}
