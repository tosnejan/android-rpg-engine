package meletos.rpg_game.file_io;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import meletos.rpg_game.GameView;
import meletos.rpg_game.inventory.Inventory;
import meletos.rpg_game.menu.HeroProperties;

/**
 * Used for coordinating loading stories and levels within them
 */
public class FileManager {
    private String rootDirPath;
    private String currentLevel;
    private final Context context;
    private final GameView gameview;

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
        // save current level
        saveCurrentLevelNo();
        Loader loader = new Loader(gameview, rootDirPath, false, currentLevel, inventory);
        gameview.setLoadingCheck(loader.getId());
        loader.start();

    }

    /**
     * Loads hero properties.
     * @return HeroProperties leaded from file
     */
    public HeroProperties loadHeroProperties() {
        String heroJson = LevelGenerator.loadFile(false, rootDirPath + "/heroProperties.json", context);
        return new GsonBuilder().create().fromJson(heroJson, HeroProperties.class);
    }

    /**
     * Saves current level number.
     */
    private void saveCurrentLevelNo() {
        String json = new Gson().toJson(currentLevel);
        saveFile(rootDirPath + "/currLevel.json", json);
    }

    /**
     * Saves file to a specified path
     * @param path to save to
     * @param json data in json (dont have to be)
     */
    public static void saveFile(String path, String json) {
        FileWriter out = null;
        try {
            File saveFile = new File(path);
            if (!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
            }
            out = new FileWriter(saveFile);
            out.write(json);
            out.flush();
        } catch (IOException e) {
            Log.e("FileManager", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.e("FileManager", e.getMessage());
            }
        }
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
