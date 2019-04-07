package meletos.rpg_game.inventory.itinerary;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashMap;

import meletos.rpg_game.text.Text;

public class Item {
    private Text text;
    private Bitmap image;
    private int ID;
    private ItemType type;
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
}
