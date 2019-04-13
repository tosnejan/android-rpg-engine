package meletos.rpg_game.file_io;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Representation of the level structure
 * -- holds information of all the levels,
 * how they follow.
 * Takes care of playing right levels in succession.
 * Also can be used to visualise the levels
 */
public class WorldRepresentation {
    private String name = "worldRepresentation";
    // stores level names in order
    private ArrayList<String> levels = new ArrayList<>();
    // stores level details for display
    private HashMap<String, HashMap<String, String>> lvlDetails = new HashMap<>();
    //used for unlocking levels
    private int currLvlNumber = 0;
    File folder;

    public WorldRepresentation() {
    }

    /**
     * Generates world representation for a folder and saves it there
     * --- idea -- users will save their level info -- it will be scanned by the game and
     * it will automatically create worldrepresentation for it and include it
     * in the menu
     * A one time operation -- saves resources in the runtime
     */
    public void generateWorldRepresentation (String path) {
        String currFilename;
        File folder = new File(path);
        this.folder = folder;
        File[] fileArray = folder.listFiles();
        LevelRepresentation lr;

        // loading the level names
        for (File file: fileArray) {
            currFilename = file.getName();
            levels.add(currFilename);
            // decoding files
            try (
                    BufferedReader r = new BufferedReader(new FileReader(file))
                    ) {
                String line;
                StringBuilder whole = new StringBuilder();
                while ((line = r.readLine()) != null) {
                    whole.append(line);
                }
                lr = new GsonBuilder().create().fromJson(whole.toString(), LevelRepresentation.class);
                HashMap<String, String> lvlInfo = new HashMap<>();
                lvlInfo.put("lvlName", lr.getLvlName());
                lvlDetails.put(currFilename, lvlInfo);
                // etc
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(levels); // sorts levels alphabetically
        saveMyselfIntoJson();
    }

    public void saveMyselfIntoJson () {
        try (
                PrintStream out = new PrintStream(
                        new FileOutputStream(folder.getAbsolutePath() + "/" + name + ".json")
                )
        ) {
            String json = new Gson().toJson(this);
            out.print(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
