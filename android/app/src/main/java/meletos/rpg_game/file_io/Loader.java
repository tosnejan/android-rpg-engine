package meletos.rpg_game.file_io;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;

public class Loader extends Thread {
    private GameView gameView;
    private String filePath;
    private boolean userSave;

    public Loader (GameView gameView, String filePath, boolean userSave) {
        super();
        this.gameView = gameView;
        this.filePath = filePath;
        this.userSave = userSave;
    }

    public synchronized void run() {
        try {
            LevelGenerator lvlGenerator = new LevelGenerator(gameView.getContext(), filePath);
            gameView.setGameHandler(lvlGenerator.buildLevel(userSave));
        } catch (UnsupportedTypeException e) {
            e.printStackTrace();
        }
    }
}
