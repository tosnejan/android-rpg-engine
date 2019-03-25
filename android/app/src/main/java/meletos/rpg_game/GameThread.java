package meletos.rpg_game;


/**
 * The thread on which the game logic runs
 */
public class GameThread extends Thread {
        private GameHandler gameHandler;

        /*variable used to stop the thread -- when false,
        game loop terminates and the system kills the thread*/
        private boolean running;

    public GameThread (GameHandler gameHandler) {
            super();
            this.gameHandler = gameHandler;
            running = true;
        }

    /**
     * The heart of the game logic
     */
    @Override
    public void run() {
        while (running) {
            synchronized (gameHandler) {
                    gameHandler.updateGame();
                try {
                    sleep(10); // puts the thread asleep so it isnt too fast :D
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * used to kill the thread
     * @param state when false, thread stops because it will not have any more work to do
     */
    public void setRunning (boolean state) {
        running = state;
    }
}
