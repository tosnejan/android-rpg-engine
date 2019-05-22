package meletos.rpg_game.navigation;

import android.graphics.Bitmap;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.Sprite;

/**
 * Our own light button class.
 */
public class Button extends Sprite {
    private final int otherX;
    private final int otherY;
    private Coordinates touchCoord = new Coordinates(0,0);

    public Button(int x, int y, Bitmap image) {
        super(x, y, image);
        otherX = x + imgWidth;
        otherY = y + imgHeigth;
    }

    /**
     * Function that determines if the coordinate of touch is inside.
     * @param x coord
     * @param y coord
     * @return true if is touched
     * false otherwise
     */
    public boolean isTouched(int x, int y) {
        touchCoord.x = x;
        touchCoord.y = y;
        if (positionInformation.isCoordinateInside(touchCoord)) {
            return true;
        }
        return false;
    }
}
