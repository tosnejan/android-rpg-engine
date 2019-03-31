package meletos.rpg_game.file_io;

import android.content.Context;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Follower;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;

/**
 * should save an array of characters into a json -- doesnt work yet probably :D
 */
public class LevelGenerator {
    private String filePath;
    private Context context;
    private File file;
    private LevelRepresentation levelRepresentation;
    private String json;


    public LevelGenerator(Context context, String filePath) {
        this.context = context;
        this.filePath = filePath;

    }

    public void serialiseLevel (GameHandler gameHandler) {
        // probably will be in another class
    }

    public GameHandler buildLevel () throws UnsupportedTypeException {
        ArrayList<FatherCharacter> characters = new ArrayList<>();
        loadFromJson();
        ArrayList<HashMap> charStrings = levelRepresentation.getCharacters();
        for (HashMap characterHash: charStrings) {
            characters.add(buildCharacter(characterHash));
        }
        FatherCharacter[] fatherCharacters = new FatherCharacter[characters.size()];
        GameHandler gh = new GameHandler(characters.toArray(fatherCharacters), context);
        gh.loadMap(levelRepresentation.getMapSource());
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
                    return new Follower(x, y, assetsFolder, context);
                default:
                    throw new UnsupportedTypeException();
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
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filePath), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
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
