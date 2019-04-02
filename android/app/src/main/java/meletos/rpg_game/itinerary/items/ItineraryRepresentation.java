package meletos.rpg_game.itinerary.items;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.SparseArray;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import meletos.rpg_game.text.Text;

public class ItineraryRepresentation implements Serializable {
    private ArrayList<HashMap> items;

    public ItineraryRepresentation() {
        items = new ArrayList<>();
    }


    public SparseArray<Item> getArray(Context context, Text text) {
        SparseArray<Item> items = new SparseArray<>();
        for (HashMap hashItem: this.items) {
            Item item = buildItem(hashItem, context, text);
            if (item != null) {
                items.append(item.getID(), item);
            }
        }
        return items;
    }

    private Item buildItem(HashMap hashItem, Context context, Text text){
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        AssetManager am = context.getAssets();
        Bitmap image;
        String path = (String)hashItem.get("path");
        String type = (String)hashItem.get("type");
        int ID;
        if (hashItem.containsKey("ID"))ID = (int) (double) hashItem.get("ID");
        else ID = 0;
        try {
            image = BitmapFactory.decodeStream(am.open(path != null ? path : "itinerary/images/noImageFound.png"));
            image = Bitmap.createScaledBitmap(image, screenWidth/10, screenWidth/10, true);
            switch (type != null ? type : "NonEquipable") {
                case "Equipable":
                    HashMap<String,Integer> stats = new HashMap<>();
                    stats.put("DMG",(int)(double)hashItem.get("DMG"));
                    return new Equipable(image, text, ID, stats);
                case "NonEquipable":
                    return new NonEquipable(image, text, ID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
