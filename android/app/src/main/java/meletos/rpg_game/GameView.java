package meletos.rpg_game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import meletos.rpg_game.characters.BouncingCharacter;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.FirstCharacter;
import meletos.rpg_game.characters.RandomWalker;

/**
 * The main surface of the game
 * SurfaceHolder.Callback -- enables to catch events
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameHandler gameHandler;
    private FatherCharacter[] characters =
            {
            new RandomWalker(100, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
            new RandomWalker(10, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
            new BouncingCharacter(0, 100, BitmapFactory.decodeResource(getResources(),R.drawable.coin)),
    };
    private MainThread thread;

    public GameView(Context context) {
        super(context);
        gameHandler = new GameHandler(characters);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        //LevelGenerator.generateAndSaveLevel(characters, "../../../assets/lvl/attempt.json"); doesnt work yet :D
        //characters = null;
        //characters = LevelInterpreter.getLevel("raw/attempt.json");

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            //Paint paint = new Paint();
            //paint.setColor(Color.rgb(250, 0, 0));
            //canvas.drawRect(100, 100, 200, 200, paint);
            //paint.setColor(Color.rgb(0, 250, 0));
            //canvas.drawRect(1720, 880, 1920, 1080, paint);
            gameHandler.drawGame(canvas);

        }
    }

    /**
     * the main game function - called per every loop
     */
    public void update() {
        //String threadName = Thread.currentThread().getName();
        //System.out.println(threadName);
        //gameHandler.updateGame();
        new Thread(gameHandler).start(); // should be running in a new thread :D
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    /**
     * starts up the thread
     */
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        if (thread.getState() == Thread.State.NEW) {
            thread.start();
        }


        //firstCharacter = new FirstCharacter(10, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin));



    }

    @Override
    /**
     * destroys the surface -- might take more attempts, hence the while loop
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
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
    }

}
