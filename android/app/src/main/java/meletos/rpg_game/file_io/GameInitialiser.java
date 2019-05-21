package meletos.rpg_game.file_io;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
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
    private File dir;
    private final Context context;

    public GameInitialiser(String storyName, Context context) {
        this.storyName = storyName;
        this.context = context;
    }

    /**
     * Initialises new save -- copies everything into a folder
     */
    public void initialiseNewSave(boolean custom) {
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
        if (!custom) {
            Log.i(this.getClass().getSimpleName(), "Going to copy from assets.");
            copyFolderFromAssets("lvl/" + storyName, dir.getPath());
        } else {
            Log.i(this.getClass().getSimpleName(), "Going to copy from SD.");
            copyFolderSD(Environment.getExternalStorageDirectory().toString() + "/rpg_game_data/CustomMaps/" + storyName, dir.getPath());
        }
    }

    /**
     * Copies whole folder from assets with everything it contains and copies
     * it into specified locatipn on SDcard
     * @param assetPath to assets
     * @param SDPath path in SD
     */
    private void copyFolderFromAssets(String assetPath, String SDPath) {
        AssetManager assetManager = context.getAssets();
        Log.i(this.getClass().getSimpleName(), "Asset path to copy from: " + assetPath);
        Log.i(this.getClass().getSimpleName(), "SD path to copy to " + SDPath);
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
     * Copies whole folder from assets with everything it contains and copies
     * it into specified locatipn on SDcard
     * @param pathFrom whole path to dir
     * @param pathTo whole path to dir
     */
    private void copyFolderSD(String pathFrom, String pathTo) {
        String[] files = null;
        files = new File(pathFrom).list();
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(pathFrom + "/" + filename);
                File outFile = new File(pathTo + "/" +  filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy file: " + filename, e);
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
     * @param in inputstream to copy form
     * @param out outputstream to copy to
     * @throws IOException when read doesn't succeed
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /**
     * Saves heroProperties
     * @param heroProperties to be saved
     */
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

    /**
     * Sets job to FileManager
     * @param fm FileManager
     */
    public void startGameLoading(FileManager fm) {
        fm.setJob(dir.getPath());
    }
}
