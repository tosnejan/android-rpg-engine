package meletos.rpg_game.file_io;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * Will be used to load the objects from the level file
 * returns an array of objects to be used by the game
 */
public abstract class LevelInterpreter {


    public static FatherCharacter[] getLevel (String filename) {
        String gs = readFromFile(filename);
        Gson gson = new Gson();
        //Type fatherType = new TypeToken<FatherCharacter[]>() {}.getType();
        FatherCharacter[] gameObjects = new FatherCharacter[1]; // just to get it to shut up
        //gameObjects = gson.fromJson(gs, fatherType);
        String[] project_specifications = gson.fromJson(gs, String[].class); // decodes the json array into a normal array

        return gameObjects;
    }

    private static String readFromFile (String filename) {
        String data = "";
        try {
            data = Files.asCharSource(new File(filename), Charsets.UTF_8).read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



}
