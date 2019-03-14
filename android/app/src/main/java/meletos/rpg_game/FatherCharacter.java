package meletos.rpg_game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter {
    private Bitmap image;
    private int x, y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isGoingRight = true;
    private boolean isGoingDown = false;
    private int xSpeed = 5; // maybe we should put speed into constructor?
    private int ySpeed = 5;
    private int imgHeigth;
    private int imgWidth;
    private GameHandler gameHandler; // the boss

    public FatherCharacter(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update () {
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
        if (x + imgWidth >= screenWidth) {
            isGoingRight = false;
        } else if (x == 0) {
            isGoingRight = true;
        }
        if (y + imgHeigth >= screenHeight) {
            isGoingDown = false;
        } else if (y == 0) {
            isGoingDown = true;
        }
    }
}
