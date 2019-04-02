package meletos.rpg_game.itinerary.items;

import android.graphics.Bitmap;

import java.util.HashMap;

import meletos.rpg_game.text.Text;

public class Equipable extends Item {
    private HashMap<String,Integer> stats;

    public Equipable(Bitmap image, Text text, int ID, HashMap<String, Integer> stats) {
        super(image, text, ID);
        this.stats = stats;
    }


    @Override
    public boolean isEquipable() {
        return true;
    }
}
