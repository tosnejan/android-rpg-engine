package meletos.rpg_game.menu;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

class HeroPropertiesJson {
    public  String name;
    public String imagesFolder;
    public double damageMultiplayer;
    public double inteligenceMultiplayer;
    public HashMap<String,Integer> stats;
    public boolean custom;
}

public class HeroProperties {
    private String name;
    private String imagesFolder;
    private transient Bitmap image; // makes gson ignore this :D
    private double damageMultiplayer;
    private double inteligenceMultiplayer;
    private HashMap<String,Integer> stats;
    private boolean custom;

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getImagesFolder() {
        return imagesFolder;
    }

    public boolean isCustom() {
        return custom;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    public static HeroProperties[] load(Context context, String path, boolean userSave) {
        Reader json = loadFile(context, path, userSave);
        if (json != null) {
            return new GsonBuilder().create().fromJson(json,HeroProperties[].class);
        } else return new HeroProperties[0];
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
            Log.e("HeroProperties", e.getMessage());
            try {
                return new InputStreamReader(context.getAssets().open("lvl/faigled/heroes.json"),
                        StandardCharsets.UTF_8);//Tohle je prasečárna, ale lepší než aby to spadlo. Jestli se změní cesta, tak je třeba předělat!!!!!
            } catch (IOException ex) {
                Log.e("HeroProperties", ex.getMessage());
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
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    public double getDamageMultiplayer() {
        return damageMultiplayer;
    }

    public double getInteligenceMultiplayer() {
        return inteligenceMultiplayer;
    }
}
