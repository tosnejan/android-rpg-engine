package meletos.rpg_game.navigation;

import android.graphics.Bitmap;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.Sprite;

public class Button extends Sprite {
    private final int otherX;
    private final int otherY;

    public Button(int x, int y, Bitmap image) {
        super(x, y, image);
        otherX = x + imgWidth;
        otherY = y + imgHeigth;
    }

    public boolean isTouched(int x, int y) {
        Coordinates touchCoord = new Coordinates(x, y);
        if (positionInformation.isCoordinateInside(touchCoord)) {
            onTouch();
            return true;
        }
        return false;
    }

    private void onTouch () {
        // Implement in individual buttons -- should be more for effects
    }

}
