package meletos.rpg_game.characters;

import android.content.res.Resources;
import android.graphics.Bitmap;

/**
 * One of first characters -- only bounces from edge to edge
 */
public class BouncingCharacter extends FatherCharacter {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isGoingRight = true;
    private boolean isGoingDown = false;


    public BouncingCharacter(int x, int y, Bitmap image) {
        super(x, y, image);
        xSpeed = 25;
        ySpeed = 15;
    }

    @Override
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
            x = screenWidth - imgWidth;
        } else if (x <= 0) {
            isGoingRight = true;
            x = 0;
        }
        if (y + imgHeigth >= screenHeight) {
            isGoingDown = false;
            y = screenHeight - imgHeigth;
        } else if (y <= 0) {
            isGoingDown = true;
            y = 0;
        }
    }

}
