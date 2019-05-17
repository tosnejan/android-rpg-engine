package meletos.rpg_game.file_io;

import android.content.Context;
import android.os.Environment;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Follower;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.inventory.Inventory;
import meletos.rpg_game.spawning.SpawnDataEntry;

/**
 * Generates a level from json by reading it into level representation and
 * then building characters, spawns etc.
 * A one function class -- buildLevel
 */
public class LevelGenerator {
    private String filePath;
    private Context context;
    private LevelRepresentation levelRepresentation;
    private String json;
    private boolean userSave;


    public LevelGenerator(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    /**
     * Main function, builds level
     * @param userSave
     * @return
     * @throws UnsupportedTypeException
     */
    public GameHandler buildLevel (boolean userSave) throws UnsupportedTypeException {
        this.userSave = userSave;
        ArrayList<FatherCharacter> characters = new ArrayList<>();
        extractLevelRepresentation();
        ArrayList<CharacterRepresentation> characterInfo = levelRepresentation.getCharacters();
        System.out.println("no of characters:" + characterInfo.size());
        for (CharacterRepresentation characterI: characterInfo) {
            characters.add(buildCharacter(characterI));
        }
        HashMap<String, List<HashMap>> spawnStructure = levelRepresentation.getSpawnStructure();
        List<SpawnDataEntry> spawnInstructions;
        /*if (spawnStructure != null) {
            spawnInstructions = processSpawnStructure(spawnStructure);
        } else {
            spawnInstructions = null;
        }*/

        int[][] inventory = levelRepresentation.getInventory();
        HashMap equipped = levelRepresentation.getEquipped();
        Inventory inv = new Inventory(inventory, equipped);
        Hero hero = (Hero)buildCharacter(levelRepresentation.getHero());
        GameHandler gh = new GameHandler(
                characters,
                hero,
                context,
                levelRepresentation.getLvlName()
                /*spawnInstructions*/
        );
        gh.loadMap(levelRepresentation.getMapSource());
        gh.setInventory(inv);
        return gh;
    }

    /**
     * Transforms spawnStructure into full spawnInstructions used by the spawnHandler
     * @param spawnStructure -- hashmap saved in LevelRepresentation
     * @return
     */
    /*
    private List<SpawnDataEntry> processSpawnStructure(HashMap<String, List<HashMap>> spawnStructure) {
        List<SpawnDataEntry> spawnInstructions = new LinkedList<>();
        for (HashMap.Entry<String, List<HashMap>> entry : spawnStructure.entrySet()) {
            Double time = Double.parseDouble(entry.getKey());
            List<HashMap> chars = entry.getValue();
            List<FatherCharacter> spawnCharacters = new LinkedList<>();
            for (HashMap characterHashmap: chars) {
                try {
                    spawnCharacters.add(buildCharacter(characterHashmap));
                } catch (UnsupportedTypeException e) {
                    e.printStackTrace();
                }
            }
            spawnInstructions.add(new SpawnDataEntry(spawnCharacters, time));
        }
        return spawnInstructions;
    }*/

    /**
     * Function that constructs a character from characterHash.
     * @param characterInfo
     * @return
     * @throws UnsupportedTypeException
     */
    private FatherCharacter buildCharacter (CharacterRepresentation characterInfo) throws UnsupportedTypeException {

        boolean enemy = (boolean)characterInfo.isEnemy;
        int x = characterInfo.xCoord;
        int y = characterInfo.yCoord;
        String imagesFolder = characterInfo.imagesFolder;
        HashMap<String,Integer> stats = characterInfo.stats;
        String battleImage = characterInfo.image;
        try {
            switch (characterInfo.charType) {

                case "Hero"://TODO teoreticky lze úplně odendat, protože se to pak stejně mění
                    if (userSave){

                        Hero hero = new Hero(x, y, context, enemy);
                        hero.getImages(imagesFolder, false, battleImage);
                        return hero;
                    } else return new Hero(x, y, imagesFolder, context, enemy);
                case "RandomWalker":
                    if (userSave){
                        //RandomWalker(int x, int y, Context context, boolean enemy, HashMap<String,Integer> stats)
                        RandomWalker walker = new RandomWalker(x, y, context, enemy, stats);
                        walker.getImages(imagesFolder, false, battleImage);
                        return walker;
                    } else return new RandomWalker(
                            x, y, imagesFolder, context, enemy, battleImage,  stats
                            /*x, y, context, enemy, stats*/);
                // follower doesnt work yet, taky je ho potřeba předělat na ten userSave... viz nahoru :D Nechci ti do toho šahat :D
                /*case "Follower":
                    Object[] coord = new Object[10];
                    coord = ((ArrayList)characterInfo.get("followCoord")).toArray(coord);
                    for (int i = 0; i < coord.length; i++) { // typecast
                        coord[i] = (Coordinates)coord[i];
                    }
                    return new Follower(x, y, imagesFolder, context, (Coordinates[]) coord, enemy);*/
                default:
                    throw new UnsupportedTypeException("This character doesnt exist yet.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Converts loaded file from json into LevelRepresentation
     */
    private void extractLevelRepresentation() {
        json = loadFile(userSave, filePath, context);
        levelRepresentation = new GsonBuilder().create().fromJson(json, LevelRepresentation.class);
    }

    /**
     * Helper function, loads the file.
     */
    public static String loadFile (boolean userSave, String filePath, Context context) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            if (userSave) {
                reader = new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(
                            Environment.getExternalStorageDirectory().toString()
                                + "/rpg_game_data/" + filePath
                        ),
                        StandardCharsets.UTF_8
                    )
                );
            } else {
                reader = new BufferedReader(
                        new InputStreamReader(context.getAssets().open(filePath), StandardCharsets.UTF_8));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


}
