package meletos.rpg_game.file_io;

import android.util.Log;

import meletos.rpg_game.GameView;
import meletos.rpg_game.inventory.Inventory;

/**
 * Thread for loading.
 */
class Loader extends Thread {
    private final GameView gameView;
    private final String filePath;
    private final boolean userSave;
    private final String lvlName;
    private final Inventory inventory;

    Loader(GameView gameView, String filePath, boolean userSave, String lvlName, Inventory inventory) {
        super();
        this.inventory = inventory;
        this.gameView = gameView;
        this.filePath = filePath;
        this.userSave = userSave;
        this.lvlName = lvlName;
    }

    public synchronized void run() {
        LevelGenerator lvlGenerator = new LevelGenerator(gameView.getContext(), filePath, lvlName, inventory);
        gameView.setGameHandler(lvlGenerator.buildLevel(userSave), filePath);
    }
}
