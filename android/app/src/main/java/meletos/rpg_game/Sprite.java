package meletos.rpg_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Objects;

/**
 * Class that describes anything that appears on screen
 */
public abstract class Sprite {
    protected PositionInformation positionInformation;
    protected int x, y;
    protected int imgHeigth, imgWidth;
    protected Bitmap image;


    public Sprite(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
        this.positionInformation = new PositionInformation(x, y, image.getHeight(), image.getWidth());
    }

    /**
     * Used for father character class and its descendants
     * @param x
     * @param y
     */
    public Sprite (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PositionInformation getPositionInformation() {
        return positionInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprite sprite = (Sprite) o;
        return Objects.equals(positionInformation, sprite.positionInformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionInformation);
    }

    /**
     * we should probably move this method to the view class -- or not? :D
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if(image != null){
        canvas.drawBitmap(image,
                positionInformation.mainCoord.x, positionInformation.mainCoord.y, null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
