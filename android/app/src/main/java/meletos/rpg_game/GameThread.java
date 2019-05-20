package meletos.rpg_game;


import android.util.Log;

/**
 * The thread on which the game logic runs
 */
public class GameThread extends Thread {
    private static final String TAG = "GameThread";
    private GameHandler gameHandler;

        /*variable used to stop the thread -- when false,
        game loop terminates and the system kills the thread*/
        private boolean running;

    GameThread(GameHandler gameHandler) {
            super();
            this.gameHandler = gameHandler;
            running = true;
    }

    void setNewGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
    /**
     * The heart of the game logic
     */
    @Override
    public synchronized void run() {
        while (running) {
            gameHandler.updateGame();
            try {
                sleep(10); // puts the thread asleep so it isnt too fast :D
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    /**
     * used to kill the thread
     * @param state when false, thread stops because it will not have any more work to do
     */
    void setRunning(boolean state) {
        running = state;
    }
}
