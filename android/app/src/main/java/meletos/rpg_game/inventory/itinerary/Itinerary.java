package meletos.rpg_game.inventory.itinerary;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import meletos.rpg_game.text.Text;


public class Itinerary {
    private final SparseArray<Item> items;

    private Itinerary(SparseArray<Item> items) {
        this.items = items;
    }

    public static Itinerary load(Context context, Text text, String path) {
        String json = loadFile(context, path);
        ItineraryRepresentation itinRepre = new GsonBuilder().create().fromJson(json, ItineraryRepresentation.class);
        SparseArray<Item> items = itinRepre.getArray(context, text);
        return new Itinerary(items);
    }

    private static String loadFile (Context context, String path) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(path),
                            StandardCharsets.UTF_8));
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

    public Item getItem(int ID){
        return items.get(ID);
    }
}
