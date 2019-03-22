package meletos.rpg_game.file_io;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * should save an array of characters into a json -- doesnt work yet probably :D
 */
public abstract class LevelGenerator {

    public static void generateAndSaveLevel (FatherCharacter[] characters, String filename) throws IOException {
        Gson gson = new Gson();
        String jsonLevel = gson.toJson("string this is!");
        /*File path = getFilesDir();
        File file = new File(path, "my-file-name.txt");

        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(jsonLevel.getBytes());
        }catch (IOException e ){
            e.printStackTrace();
        } finally {
            stream.close();
        }*/

        try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
            out.print(jsonLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
