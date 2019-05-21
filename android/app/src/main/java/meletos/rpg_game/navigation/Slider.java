package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.Coordinates;

public abstract class Slider extends Button{
    final Bitmap slider;
    final int scale;
    final int sliderWidth;
    final int sliderHeight;

    Slider(int x, int y, Bitmap image, Bitmap slider, int scale) {
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

    protected abstract void onTouch(int x, int y);
}
