package meletos.rpg_game.file_io;

import android.content.Context;

import com.google.gson.GsonBuilder;

import meletos.rpg_game.GameView;
import meletos.rpg_game.inventory.Inventory;
import meletos.rpg_game.menu.HeroProperties;

/**
 * Used for coordinating loading stories and levels within them
 */
public class FileManager {
    private String rootDirPath;
    private String currentLevel;
    private Context context;
    private GameView gameview;

    public FileManager(Context context, GameView gameview) {
        this.gameview = gameview;
        this.context = context;
    }


    /**
     * Sets job -- the save structure it should be processing
     * @param rootDirPath of world
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
        loadLevels(currentLevel, null);
    }

    /**
     * Loads level
     * @param levelToBeLoaded full path to level
     * @param inventory passes inventory if only switching levels so it
     *                  doesn't have to load it form file
     */
    public void loadLevels (String levelToBeLoaded, Inventory inventory) {
        currentLevel = levelToBeLoaded;
        new Loader(gameview, rootDirPath, false, currentLevel, inventory).start();
    }

    /**
     * Loads hero properties.
     * @return HeroProperties leaded from file
     */
    public HeroProperties loadHeroProperties() {
        String heroJson = LevelGenerator.loadFile(false, rootDirPath + "/heroProperties.json", context);
        return new GsonBuilder().create().fromJson(heroJson, HeroProperties.class);
    }

    public String getCurrLvl() {
        return currentLevel;
    }

    public String getCurrPath() {
        return rootDirPath;
    }

    public String getRootDirPath() {
        return rootDirPath;
    }
}
