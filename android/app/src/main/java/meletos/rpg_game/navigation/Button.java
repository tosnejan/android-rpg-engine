package meletos.rpg_game.navigation;

import android.graphics.Bitmap;

import meletos.rpg_game.Sprite;

public class Button extends Sprite {
    protected int otherX, otherY;

    public Button(int x, int y, Bitmap image) {
        super(x, y, image);
        otherX = x + imgWidth;
        otherY = y + imgHeigth;
    }

    public boolean isTouched(int x, int y) {
        if (x >= this.x && x <= otherX &&
            y >= this.y && y <= otherY
        ) {
            onTouch();
            return true;
        }
        return false;
    }

    private void onTouch () {
        // Implement in individual buttons -- should be more for effects
    }
}
