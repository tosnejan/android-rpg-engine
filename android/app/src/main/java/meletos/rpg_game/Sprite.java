package meletos.rpg_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Class that describes anything that appears on screen
 */
public abstract class Sprite {
    protected int x, y;
    protected Bitmap image;
    protected int imgHeigth, imgWidth;

    public Sprite(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
    }

    /**
     * we should probably move this method to the view class -- or not? :D
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
}
