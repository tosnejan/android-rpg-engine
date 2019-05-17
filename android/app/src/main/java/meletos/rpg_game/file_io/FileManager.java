package meletos.rpg_game.file_io;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.inventory.Inventory;

/**
 * Used for coordinating loading and saving
 */
public class FileManager {
    private String rootDirPath;
    private String currentLevel;
    private Context context;
    private ArrayList<GameHandler> gameHandlers;
    private GameView gameview;

    public FileManager(Context context, GameView gameview) {
        this.gameview = gameview;
        this.context = context;
    }

    /**
     * Sets job -- the save structure it should be processing
     */
    public void setJob (String rootDirPath) {
        this.rootDirPath = rootDirPath;
        currentLevel = new GsonBuilder().create().fromJson(
                LevelGenerator.loadFile(
                        false, rootDirPath + "/currLevel.json",
                        context
                ),
                String.class
        );
        loadLevels(currentLevel);
    }

    public void loadLevels (String levelToBeLoaded) {
        currentLevel = levelToBeLoaded;
        new Loader(gameview, rootDirPath, false, currentLevel).start();
    }

    /**
     * Will be used for saving level TODO -- SATURDAY
     */
    public void save() {

    }

    /**
     * Kills and loads new levels
     */
    private void checkLoadedLevels() {

    }
}
