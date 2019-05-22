package meletos.rpg_game.inventory.itinerary;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import meletos.rpg_game.text.Text;

/**
 * Represents itinerary in JSON.
 */
class ItineraryRepresentation {
    private final ArrayList<HashMap> items;

    public ItineraryRepresentation() {
        items = new ArrayList<>();
    }

    /**
     * Returns array of items
     * @param context of app
     * @param text Text of game
     * @return SparseArray of Items
     */
    SparseArray<Item> getArray(Context context, Text text) {
        SparseArray<Item> items = new SparseArray<>();
        for (HashMap hashItem: this.items) {
            Item item = buildItem(hashItem, context, text);
            if (item != null) {
                items.append(item.getID(), item);
            }
        }
        return items;
    }

    /**
     * Builds item from file.
     * @param hashItem hash of item
     * @param context of app
     * @param text that comes with itm
     * @return Item that is built
     */
    private Item buildItem(HashMap hashItem, Context context, Text text){
        //int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
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
            image = Bitmap.createScaledBitmap(image, screenHeight / 9, screenHeight / 9, true);

        } catch (IOException e) {
            try {
                image = BitmapFactory.decodeStream(am.open("itinerary/images/noImageFound.png"));
            } catch (IOException ex) {
                Log.e(this.getClass().getSimpleName(), ex.getLocalizedMessage());
                return null;
            }
            Log.e(this.getClass().getSimpleName(), e.getLocalizedMessage());
        }
        image = Bitmap.createScaledBitmap(image, screenHeight / 9, screenHeight/9, true);
        HashMap<String,Integer> stats = new HashMap<>();
        if (hashItem.containsKey("DMG")) stats.put("DMG",(int)(double)hashItem.get("DMG"));
        if (hashItem.containsKey("ARM")) stats.put("ARM",(int)(double)hashItem.get("ARM"));
        return new Item(image, text, ID, type, stats);
    }
}
