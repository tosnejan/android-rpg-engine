package meletos.rpg_game.file_io;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import meletos.rpg_game.menu.Story;

/**
 * Class that is used to find all files
 */
public class FileScout {
    private static String storyLocation = "lvl"; // in assets

    private static String saveLocation = "/rpg_game_data/save"; // in sdcard

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

    public static String[] getAllStoryLocations(Context context) {
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

    public static Story[] getSaves(Context context) {
        Story[] stories;
        String storagePath = Environment.getExternalStorageDirectory().toString();
        String fullSavePath = storagePath + saveLocation;
        File rootDir = new File(fullSavePath);
        String[] directories = rootDir.list();
        stories = new Story[directories.length];
        System.out.println("DIRECTORIES:");
        for (String directory1 : directories) {
            System.out.println(directory1);
        }
        System.out.println("Length of directories: " + directories.length);
        String readFile;
        int i = 0;
        for (String directory : directories) {
            readFile = LevelGenerator.loadFile(false, fullSavePath + "/" + directory + "/story.json", context);
            try {
                Story story = new Story(
                        readFile,
                        BitmapFactory.decodeStream(new FileInputStream(fullSavePath + "/" + directory + "/icon.png")
                        )
                );
                story.setName(story.getName() + " " + (i + 1));
                story.setPath(fullSavePath + "/" + directory);
                stories[i] = story;
                ++i;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stories;

    }

    /**
     * This function deletes the whole story
     * @param fullPath
     */
    private static void deleteStory(String fullPath) {
        File file = new File(fullPath);
        deleteDirectory(file);
    }

    /**
     * Recursively deletes whole directory
     * @param file
     */
    private static void deleteDirectory (File file) {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        if (!file.delete()) {
            try {
                throw new IOException("Failed to delete " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
