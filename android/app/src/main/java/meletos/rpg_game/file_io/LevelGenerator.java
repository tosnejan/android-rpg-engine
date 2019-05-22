package meletos.rpg_game.file_io;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.Chest;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.characters.StandingCharacter;
import meletos.rpg_game.dialog.DialogSwitcher;
import meletos.rpg_game.inventory.Inventory;

/**
 * Generates a level from json by reading it into level representation and
 * then building characters, inventory etc.
 */
public class LevelGenerator {
    private final String filePath;
    private final String lvlName;
    private final Context context;
    private LevelRepresentation levelRepresentation;
    private String json;
    private boolean userSave;
    private Inventory inventory;


    // WARNING -- SHOULD BE GIVEN FULL PATH TO EXTERNAL STORAGE
    LevelGenerator(Context context, String filePath, String lvlName, Inventory inventory) {
        this.context = context;
        this.filePath = filePath;
        this.inventory = inventory; // this is useful for passing already existing inventory between levels
        this.lvlName = lvlName;
    }

    /**
     * Main function, builds level
     * @param userSave whether uses custom pictures
     * @return GameHandler
     */
    GameHandler buildLevel(boolean userSave) {
        if (inventory == null) {
            inventory = loadInventory();
        }
        this.userSave = userSave;
        ArrayList<FatherCharacter> characters = new ArrayList<>();
        levelRepresentation = extractLevelRepresentation();
        ArrayList<CharacterRepresentation> characterInfo = levelRepresentation.getCharacters();
        for (CharacterRepresentation characterI: characterInfo) {
            characters.add(buildCharacter(characterI));
        }
        int xShift = levelRepresentation.getxShift();
        int yShift = levelRepresentation.getyShift();
        HashMap<String, List<HashMap>> spawnStructure = levelRepresentation.getSpawnStructure();
        Hero hero = (Hero)buildCharacter(levelRepresentation.getHero());
        List<Chest> chests = levelRepresentation.getChests();
        if (chests != null)
            for (Chest chest: chests) {
                chest.loadImage(context);
            }

        GameHandler gh = new GameHandler(
                    characters,
                    hero,
                    context,
                    levelRepresentation.getLvlName(),
                    levelRepresentation.getTransitionManager(),
                    levelRepresentation.getChests()
            );
        gh.setShifts(xShift, yShift);


        gh.loadMap(levelRepresentation.getMapSource());
        gh.setInventory(inventory);
        Log.i("LevelGenerator", "Returning new game handler.");
        return gh;
    }

    /**
     * Function that constructs a character from characterHash.
     * @param characterInfo characterRepresentation
     * @return FatherCharacter generated character
     */
    private FatherCharacter buildCharacter (CharacterRepresentation characterInfo) {
        boolean enemy = characterInfo.isEnemy;
        int x = characterInfo.xCoord;
        int y = characterInfo.yCoord;
        String imagesFolder = characterInfo.imagesFolder;
        HashMap<String,Integer> stats = characterInfo.stats;
        String battleImage = characterInfo.image;
        int[][] dialogs = characterInfo.dialogs;
        int actualDialog = characterInfo.actualDialog;
        boolean played = characterInfo.played;
        DialogSwitcher[] dialogSwitchers = characterInfo.dialogSwitchers;
        try {
            switch (characterInfo.charType) {
                case "Hero":
                    if (userSave){
                        Hero hero = new Hero(x, y, context, enemy);
                        hero.getImages(imagesFolder, false, battleImage);
                        return hero;
                    } else return new Hero(x, y, imagesFolder, context, enemy);
                case "RandomWalker":
                    if (userSave){
                        RandomWalker walker = new RandomWalker(x, y, context, enemy, stats);
                        walker.getImages(imagesFolder, false, battleImage);
                        return walker;
                    } else return new RandomWalker(
                            x, y, imagesFolder, context, enemy, battleImage,  stats
                            /*x, y, context, enemy, stats*/);
                case "StandingCharacter":
                    if (enemy){
                        if (userSave){
                            StandingCharacter standing = new StandingCharacter(x, y, context, stats);
                            standing.loadImage(imagesFolder, false, battleImage);
                            return standing;
                        } else return new StandingCharacter(x, y, imagesFolder, context, battleImage,  stats);
                    } else {
                        if (userSave){
                            StandingCharacter standing = new StandingCharacter(x, y, context, dialogs, actualDialog, played, dialogSwitchers);
                            standing.loadImage(imagesFolder, false);
                            return standing;
                        } else return new StandingCharacter(x, y, context, imagesFolder, dialogs, actualDialog, played, dialogSwitchers);
                    }
            }
        } catch (NullPointerException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        Log.e(this.getClass().getSimpleName(), "Error building character: This character doesn't exist. Building RandomWalker instead.");
        characterInfo.charType = "RandomWalker";
        return buildCharacter(characterInfo); // tries to build random walker instead
    }

    /**
     * Converts loaded file from json into LevelRepresentation
     */
    private LevelRepresentation extractLevelRepresentation() {
        json = FileManager.loadFile(filePath + "/" + lvlName);
        return new GsonBuilder().create().fromJson(json, LevelRepresentation.class);
    }

    /**
     * Loads inventory from its file.
     * @return inventory
     */
    private Inventory loadInventory() {
        Log.i(this.getClass().getSimpleName(), "Filepath " + filePath);
        String json = FileManager.loadFile(filePath + "/inventory.json");
        return new GsonBuilder().create().fromJson(json, Inventory.class);
    }
}
