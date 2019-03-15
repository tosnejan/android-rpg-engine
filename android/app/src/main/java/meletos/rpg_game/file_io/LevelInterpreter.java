package meletos.rpg_game.file_io;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import meletos.rpg_game.FatherCharacter;

/**
 * Will be used to load the objects from the level file
 * returns an array of objects to be used by the game
 */
public abstract class LevelInterpreter {


    public static FatherCharacter[] getLevel (String filename) {
        String gs = readFromFile(filename);
        Gson gson = new Gson();
        Type fatherType = new TypeToken<FatherCharacter[]>() {}.getType();
        FatherCharacter[] gameObjects;
        gameObjects = gson.fromJson(gs, fatherType);
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
