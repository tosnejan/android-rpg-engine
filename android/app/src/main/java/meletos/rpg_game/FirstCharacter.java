package meletos.rpg_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FirstCharacter {
    private Bitmap image;
    private int x, y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isGoingRight = true;
    private boolean isGoingDown = false;

    public FirstCharacter(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update (int xSpeed, int ySpeed) {
        if (isGoingRight){
            x += xSpeed;
        } else {
            x -= xSpeed;
        }
        if (isGoingDown){
            y += ySpeed;
        } else {
            y -= ySpeed;
        }
        if (x + image.getWidth() >= screenWidth) {
            isGoingRight = false;
        } else if (x == 0) {
            isGoingRight = true;
        }
        if (y + image.getHeight() >= screenHeight) {
            isGoingDown = false;
        } else if (y == 0) {
            isGoingDown = true;
        }
    }
}
