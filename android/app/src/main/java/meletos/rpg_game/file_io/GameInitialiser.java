package meletos.rpg_game.file_io;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import meletos.rpg_game.menu.HeroProperties;

/**
 * Intitialises the game. Copies everything into the save
 */
public class GameInitialiser {
    private final String SAVEFOLDER = "/rpg_game_data/save/"; // here the levels are saved
    private final String storyName;
    File dir;
    Context context;

    public GameInitialiser(String storyName, Context context) {
        this.storyName = storyName;
        this.context = context;
    }

    /**
     * Initialises new save -- copies everything into a folder
     */
    public void initialiseNewSave() {
        //getCurrentSaves();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        Date date = new Date();
        String currDate = formatter.format(date);
        try
        {
            dir = new File(
                    Environment.getExternalStorageDirectory().toString()
                            + SAVEFOLDER + storyName + currDate
            ); // saves it as storyname and current time
            if (!dir.mkdirs()) throw new Exception("Error creating directory.");

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        copyFolderFromAssets("lvl/" + storyName, dir.getPath());

    }

    /**
     * Copies whole folder from assets with everything it contains and copies
     * it into specified locatipn on SDcard
     * @param assetPath
     * @param SDPath
     */
    private void copyFolderFromAssets(String assetPath, String SDPath) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(assetPath);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(assetPath + "/" + filename);
                File outFile = new File(SDPath + "/" +  filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Helper function that copies files
     * @param in
     * @param out
     * @throws IOException
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
    
    public void saveHeroProperties(HeroProperties heroProperties) {
            try (
                    PrintStream out = new PrintStream(
                            new FileOutputStream(dir.getPath() + "/heroProperties.json")
                    )
            ) {
                String json = new Gson().toJson(heroProperties);
                out.print(json);
                out.flush();
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
    }

    public void startGameLoading(FileManager fm) {
        fm.setJob(dir.getPath());
    }
}
