package meletos.rpg_game.itinerary.items;

import android.graphics.Bitmap;

import meletos.rpg_game.text.Text;

public class NonEquipable extends Item {

    public NonEquipable(Bitmap image, Text text, int ID) {
        super(image, text, ID);
    }

    @Override
    public boolean isEquipable() {
        return false;
    }
}
