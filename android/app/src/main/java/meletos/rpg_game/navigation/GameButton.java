package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import meletos.rpg_game.Coordinates;

/**
 * Game button. Implements change of image upon click.
 */
public class GameButton extends Button {
    private final Bitmap imageClicked;
    private final Bitmap imageUnclicked;
    private Bitmap icon;
    private Rect bounds = new Rect();
    private int textSize;
    private int yClick = 0;

    public GameButton(int x, int y, Bitmap imageUnclicked, Bitmap imageClicked) {
        super(x,y,imageUnclicked);
        this.imageUnclicked = imageUnclicked;
        this.imageClicked = imageClicked;
    }

    @Override
    public void draw(Canvas canvas) {
        if(image != null){
            canvas.drawBitmap(image,
                    positionInformation.mainCoord.x, positionInformation.mainCoord.y + yClick, null);
            canvas.drawBitmap(icon,
                    positionInformation.mainCoord.x + (imgWidth - icon.getWidth())/2f, positionInformation.mainCoord.y + yClick + (imgHeigth - icon.getHeight())/2, null);
        }
    }

    public void changeImage(boolean isClicked, int yShift){
        if (isClicked){
            image = imageClicked;
            yClick = yShift;
        } else {
            image = imageUnclicked;
            yClick = 0;
        }
    }

    public void setIcon(Bitmap icon){
        this.icon = icon;
    }
}
