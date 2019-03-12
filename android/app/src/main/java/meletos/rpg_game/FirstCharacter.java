package meletos.rpg_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FirstCharacter {
    private Bitmap image;
    private int x, y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public FirstCharacter(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update (int xSpeed, int ySpeed) {
        y += ySpeed;
        x += xSpeed;
        if (x + image.getWidth() == screenWidth) {
            xSpeed *= -1;
        }
        if (y + image.getHeight() == screenHeight) {
            ySpeed *= -1;
        }
    }


}
