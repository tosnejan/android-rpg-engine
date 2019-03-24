package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.Sprite;

/**
 * Not ready at all. :D
 */
public class JoyStick {

    private int radius;
    private int x, y;
    private Bitmap circle, ring;
    private boolean used = false;

    public JoyStick(Bitmap circle, Bitmap ring) {
        this.circle = circle;
        this.ring = ring;
        radius = circle.getWidth()/2;
    }

    void setBase(int x, int y){
        this.x = x;
        this.y = y;
    }

    void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean getDirection(int x, int y) {
        if (x >= this.x - radius && x <= this.x + radius &&
                y >= this.y  - radius && y <= this.y + radius
        ) {
            return true;
        }
        return false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(circle, x - radius, y - radius, null);
    }
}
