package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.text.Text;

public class xSlider extends Slider {
    private final int jump;
    private int value = 0;
    private final int ID;
    private final Text text;
    private final int yShift;

    public xSlider(int x, int y, Bitmap image, Bitmap slider, Text text, int ID, int scale) {
        super(x, y, image, slider, scale);
        jump = (imgWidth- sliderWidth)/scale;
        this.ID = ID;
        this.text = text;
        yShift = (imgHeigth - sliderHeight)/2;

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(slider,
                positionInformation.mainCoord.x + value * jump + sliderWidth/3f,
                positionInformation.mainCoord.y + yShift, null);
    }

    @Override
    public void onTouch(int x, int y) {
        value = (x - this.x - sliderWidth)/jump;
        if (value < 0) value = 0;
        if (value >= scale) value = scale - 1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
