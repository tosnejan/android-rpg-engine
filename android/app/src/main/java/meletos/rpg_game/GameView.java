package meletos.rpg_game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

/**
 * The main surface of the game
 * SurfaceHolder.Callback -- enables to catch events
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private FirstCharacter firstCharacter;
    private MainThread thread;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
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
            firstCharacter.draw(canvas);

        }
    }

    /**
     * the main game function - called per every loop
     */
    public void update() {
        firstCharacter.update(10, 10);
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
        thread.start();
        firstCharacter = new FirstCharacter(10, 20, BitmapFactory.decodeResource(getResources(),R.drawable.coin));
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
