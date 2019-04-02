package meletos.rpg_game.itineary.items;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.text.Text;

public abstract class Item {
    private String description;
    private Text text;
    private Bitmap image;
    private int ID;


    public Item(Bitmap image, Text text, int ID) {
        this.image = image;
        this.text = text;
        this.ID = ID;
    }

    public void draw(int x, int y, Canvas canvas){
        if(image != null){
            canvas.drawBitmap(image, x, y, null);
        }
    }

    public String getName(){
        return text.getItemName(ID);
    }
}
