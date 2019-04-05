package meletos.rpg_game.file_io;

import android.content.Context;
import android.os.Environment;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Follower;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.inventory.Inventory;

/**
 * should save an array of characters into a json -- doesnt work yet probably :D
 */
public class LevelGenerator {
    private String filePath;
    private Context context;
    private File file;
    private LevelRepresentation levelRepresentation;
    private String json;
    private boolean userSave;


    public LevelGenerator(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;
    }

    public GameHandler buildLevel (boolean userSave) throws UnsupportedTypeException {
        this.userSave = userSave;
        ArrayList<FatherCharacter> characters = new ArrayList<>();
        loadFromJson();
        ArrayList<HashMap> charStrings = levelRepresentation.getCharacters();
        for (HashMap characterHash: charStrings) {
            characters.add(buildCharacter(characterHash));
        }
        int[][] inventory = levelRepresentation.getInventory();
        HashMap equipped = levelRepresentation.getEquipped();
        Inventory inv = new Inventory(inventory, equipped);
        FatherCharacter[] fatherCharacters = new FatherCharacter[characters.size()];
        GameHandler gh = new GameHandler(characters.toArray(fatherCharacters), context, levelRepresentation.getLvlName());
        gh.loadMap(levelRepresentation.getMapSource());
        gh.setInventory(inv);
        return gh; //TODO
    }

    private FatherCharacter buildCharacter (HashMap characterHash) throws UnsupportedTypeException {
        System.out.println(characterHash);
        System.out.println(characterHash.get("xCoord"));
        double xDoub = (double)characterHash.get("xCoord");
        double yDoub = (double)characterHash.get("yCoord");
        int x = (int) xDoub;
        int y = (int) yDoub;

        String assetsFolder = (String)characterHash.get("assetsFolder");
        try {
            switch ((String) characterHash.get("charType")) {

                case "Hero":
                    return new Hero(x, y, assetsFolder, context);
                case "RandomWalker":
                    return new RandomWalker(x, y, assetsFolder, context);
                case "Follower":
                    Coordinates[] coord = (Coordinates[])characterHash.get("followCoord");
                    return new Follower(x, y, assetsFolder, context, coord);
                default:
                    throw new UnsupportedTypeException("This character doesnt exist yet.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;

    }

    private void loadFromJson () {
        loadFile();
        System.out.println(json);
        levelRepresentation = new GsonBuilder().create().fromJson(json, LevelRepresentation.class);
    }

    private void loadFile () {
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
        json = sb.toString();
    }


}
