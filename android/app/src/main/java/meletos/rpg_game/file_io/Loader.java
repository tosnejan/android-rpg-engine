package meletos.rpg_game.file_io;

import android.util.Log;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.inventory.Inventory;

/**
 * Thread for loading.
 */
public class Loader extends Thread {
    private GameView gameView;
    private String filePath;
    private boolean userSave;
    private String lvlName;
    private Inventory inventory;

    public Loader (GameView gameView, String filePath, boolean userSave, String lvlName, Inventory inventory) {
        super();
        this.inventory = inventory;
        this.gameView = gameView;
        this.filePath = filePath;
        this.userSave = userSave;
        this.lvlName = lvlName;
    }

    public synchronized void run() {
        try {
            LevelGenerator lvlGenerator = new LevelGenerator(gameView.getContext(), filePath, lvlName, inventory);
            gameView.setGameHandler(lvlGenerator.buildLevel(userSave));
            while(!gameView.hasGameHandler()) {
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
            }
            gameView.setState(meletos.rpg_game.State.MAP);

        } catch (UnsupportedTypeException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }
}
