package meletos.rpg_game.file_io;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;


/**
 * TODO -- GET RID OF THIS CLASS
 */
public class Loader extends Thread {
    private GameView gameView;
    private String filePath;
    private boolean userSave;
    private String lvlName;

    public Loader (GameView gameView, String filePath, boolean userSave, String lvlName) {
        super();
        this.gameView = gameView;
        this.filePath = filePath;
        this.userSave = userSave;
        this.lvlName = lvlName;
    }

    public synchronized void run() {
        try {
            LevelGenerator lvlGenerator = new LevelGenerator(gameView.getContext(), filePath, lvlName, null);
            gameView.setGameHandler(lvlGenerator.buildLevel(userSave));

        } catch (UnsupportedTypeException e) {
            e.printStackTrace();
        }
    }
}
