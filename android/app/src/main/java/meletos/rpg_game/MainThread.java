package meletos.rpg_game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Class that creates the thread for the GUI to run on
 */
class MainThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private boolean running;
    private static Canvas canvas;
    private boolean canUseSurfaceHolder;

    MainThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /**
     * Can stop the thread
     * @param isRunning boolean - if false, thread stops
     */
    void setRunning(boolean isRunning) {
        running = isRunning;
    }

    /**
     * This fixes the error we experienced -- is called from GameView's methods
     * onSurfaceCreated onSurfaceDestroyed
     * @param canUseSurfaceHolder boolean to set
     */
    void canUseSurfaceHolder(boolean canUseSurfaceHolder) {
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
                Log.e(this.getClass().getSimpleName(), e.getMessage());
                break;
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                    }
                }
            }
        }
    }
}
