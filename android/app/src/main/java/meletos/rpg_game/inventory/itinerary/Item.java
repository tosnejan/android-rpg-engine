package meletos.rpg_game.inventory.itinerary;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashMap;

import meletos.rpg_game.text.Text;

public class Item {
    private final Text text;
    private final Bitmap image;
    private final int ID;
    private final ItemType type;
    private HashMap<String, Integer> stats;

    public Item(Bitmap image, Text text, int ID, ItemType type, HashMap<String, Integer> stats) {
        this.image = image;
        this.text = text;
        this.ID = ID;
        this.type = type;
        this.stats = stats;
    }

    public Item(Text text, Bitmap image, int ID, ItemType type) {
        this.text = text;
        this.image = image;
        this.ID = ID;
        this.type = type;
    }

    /**
     * Draw item icon.
     * @param x coordination where to draw
     * @param y coordination where to draw
     * @param canvas canvas to draw
     */
    public void draw(int x, int y, Canvas canvas){
        if(image != null){
            canvas.drawBitmap(image, x, y, null);
        }
    }

    public String getName(){
        return text.getItemName(ID);
    }

    public int getID() {
        return ID;
    }

    public ItemType getType() {
        return type;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }
}
