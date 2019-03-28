package meletos.rpg_game.file_io;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

import static android.content.Context.MODE_PRIVATE;

/**
 * should save an array of characters into a json -- doesnt work yet probably :D
 */
public class LevelGenerator {
    private String filename;
    private Context context;
    private File file;

    public LevelGenerator() {
        this.context = context;
        this.filename = filename;

        // gets the directory with files
        File directory;
        directory = context.getDir("levels", MODE_PRIVATE);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.toString() == filename) {
                this.file = file;
            }
        }
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
