package meletos.rpg_game.file_io;

import android.content.Context;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Will be used to load the objects from the level file
 * returns an array of objects to be used by the game
 */
public class LevelHandler {
    private String filename;
    private Map<String, Object> gameData = new HashMap<String, Object>();
    private Context context;
    private File file;

    /**
    * Level variables
    */


    public LevelHandler(String filename, Context context) {
        this.context = context;
        this.filename = filename;

        // gets the directory with files
        File directory;
        directory = context.getDir("lvl", MODE_PRIVATE);
        File[] files = directory.listFiles();
        System.out.println(files[0]);
        for (File file : files) {
            System.out.println(file);
            if (file.toString() == filename) {

                this.file = file;
                break;
            }
        }
    }

    public GameHandler deserialiseLevel () {
        GameHandler gameHandler = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);
            gameHandler = (GameHandler) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameHandler;
    }

    public void serialiseLevel (GameHandler gameHandler) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(gameHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
