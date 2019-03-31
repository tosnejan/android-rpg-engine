package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.Coordinates;

public abstract class Slider extends Button{
    protected Bitmap slider;
    protected int scale;
    protected int sliderWidth;
    protected int sliderHeight;

    public Slider(int x, int y, Bitmap image, Bitmap slider, int scale) {
        super(x, y, image);
        this.slider = slider;
        this.scale = scale;
        sliderWidth = slider.getWidth();
        sliderHeight = slider.getHeight();
    }

    @Override
    public boolean isTouched(int x, int y) {
        Coordinates touchCoord = new Coordinates(x, y);
        if (positionInformation.isCoordinateInside(touchCoord)) {
            onTouch(x, y);
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public abstract void onTouch(int x, int y);
}
