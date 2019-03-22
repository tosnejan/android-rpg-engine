package meletos.rpg_game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * The thread on which the game logic runs
 */
public class GameThread extends Thread {
        private GameHandler gameHandler;

    public GameThread (GameHandler gameHandler) {
            super();
            this.gameHandler = gameHandler;
        }

    @Override
    public void run() {
        while (true) {
            synchronized (gameHandler) {
                this.gameHandler.updateGame();
                try {
                    sleep(10); // puts the thread asleep so it isnt too fast :D
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
