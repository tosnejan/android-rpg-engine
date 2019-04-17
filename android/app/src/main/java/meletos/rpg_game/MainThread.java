package meletos.rpg_game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Class that creates the thread for the GUI to run on
 */
public class MainThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    public static Canvas canvas;
    private boolean canUseSurfaceHolder;

    public MainThread (SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    /**
     * This fixes the error we experienced -- is called from GameView's methods
     * onSurfaceCreated onSurfaceDestroyed
     * @param canUseSurfaceHolder
     */
    public void canUseSurfaceHolder (boolean canUseSurfaceHolder) {
        this.canUseSurfaceHolder = canUseSurfaceHolder;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;
            // tries to lock canvas to draw on and catch exception if not successful
            try {
                // locks the canvas so that only one thread can draw onto it
                if (canUseSurfaceHolder) {
                    synchronized (surfaceHolder) {
                        canvas = this.surfaceHolder.lockCanvas();
                        this.gameView.draw(canvas);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
