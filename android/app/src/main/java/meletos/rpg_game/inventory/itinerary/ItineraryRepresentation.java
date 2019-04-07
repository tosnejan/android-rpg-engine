package meletos.rpg_game.inventory.itinerary;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

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
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        AssetManager am = context.getAssets();
        Bitmap image;
        String path = (String)hashItem.get("path");
        String typeString = (String)hashItem.get("type");
        ItemType type = ItemType.valueOf(typeString.toUpperCase(Locale.ENGLISH));
        int ID;
        if (hashItem.containsKey("ID"))ID = (int) (double) hashItem.get("ID");
        else ID = 0;
        try {
            image = BitmapFactory.decodeStream(am.open(path != null ? path : "itinerary/images/noImageFound.png"));
            image = Bitmap.createScaledBitmap(image, screenHeight / 9, (int)(screenHeight / 9), true);

        } catch (IOException e) {
            try {
                image = BitmapFactory.decodeStream(am.open("itinerary/images/noImageFound.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            e.printStackTrace();
        }
        image = Bitmap.createScaledBitmap(image, screenHeight / 9, (int)(screenHeight/9), true);
        HashMap<String,Integer> stats = new HashMap<>();
        if (hashItem.containsKey("DMG")) stats.put("DMG",(int)(double)hashItem.get("DMG"));
        if (hashItem.containsKey("INT")) stats.put("INT",(int)(double)hashItem.get("INT"));
        return new Item(image, text, ID, type, stats);
    }
}
