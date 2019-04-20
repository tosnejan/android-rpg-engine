package meletos.rpg_game.menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HeroProperties {
    private String name;
    private String imagesFolder;
    private Bitmap image;
    private HashMap<String,Integer> stats;
    private boolean custom;

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    public static HeroProperties[] load(Context context, String path, boolean userSave) {
        Reader json = loadFile(context, path, userSave);
        if (json != null) {
            return new GsonBuilder().create().fromJson(json, HeroProperties[].class);
        } else return null;
    }

    private static Reader loadFile (Context context, String path, boolean userSave) {
        try {
            if (userSave){
                File file = Environment.getExternalStorageDirectory();
                file = new File(file, path + "/heroes.json");
                return new FileReader(file);
            } else {
                return new InputStreamReader(context.getAssets().open(path + "/heroes.json"),
                        StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                return new InputStreamReader(context.getAssets().open("lvl/heroes.json"),
                        StandardCharsets.UTF_8);//Tohle je prasečárna, ale lepší než aby to spadlo. Jestli se změní cesta, tak je třeba předělat!!!!!
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void loadImage(Context context, String path){
        try {
            if (custom){
                File file = Environment.getExternalStorageDirectory();
                image = BitmapFactory.decodeFile(file.getAbsolutePath() + path + "/" + imagesFolder + "/8.png");
            } else {
                AssetManager am = context.getAssets();
                image = BitmapFactory.decodeStream(am.open(imagesFolder + "/8.png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
