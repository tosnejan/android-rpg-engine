package meletos.rpg_game.file_io;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import meletos.rpg_game.menu.Story;

/**
 * Class that is used to find all files
 */
public class FileScout {
    private static String storyLocation = "lvl"; // in assets
    public static Story[] getStories(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] directories = null;
        try {
            directories = assetManager.list(storyLocation);
        } catch (IOException e) {
            Log.e("FileScout", "Assets error");
        }
        String readFile;
        Story[] stories = new Story[directories.length];
        int i = 0;
        for (String directory : directories) {
            readFile = readFromAssets(storyLocation + "/" + directory + "/story.json", assetManager);
            try {
                Story story = new Story(
                        readFile,
                        BitmapFactory.decodeStream(assetManager.open(storyLocation + "/" + directory + "/icon.png")
                        )
                );
                stories[i] = story;
                ++i;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stories;
    }

    public static String[] getAllLvls(Context context) {
        AssetManager assetManager = context.getAssets();
        String[] directories = null;
        try {
            directories = assetManager.list(storyLocation);
        } catch (IOException e) {
            Log.e("FileScout", "Assets error");
        }
        return directories;
    }

    private static String readFromAssets(String assetPath, AssetManager assetManager) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                                    assetManager.open(assetPath), StandardCharsets.UTF_8
                    )
            );
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
        return sb.toString();
    }
}
