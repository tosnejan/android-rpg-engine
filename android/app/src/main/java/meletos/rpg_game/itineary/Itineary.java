package meletos.rpg_game.itineary;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.SparseArray;

import meletos.rpg_game.itineary.items.Item;


public class Itineary {
    private SparseArray<Item> items = new SparseArray<>();
    private Context context;

    public Itineary(String path, Context context) {
        this.context = context;
        load(path);
    }

    private void load(String path) {
        AssetManager am = context.getAssets();
        /*try {
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
