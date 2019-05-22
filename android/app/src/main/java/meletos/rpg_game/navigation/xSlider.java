package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import meletos.rpg_game.text.Text;

public class xSlider extends Slider {
    private final int jump;
    private int value = 0;
    private final int ID;
    private final Text text;
    private final int yShift;
    private final Paint paint;
    private final Rect bounds = new Rect();
    private int textSize;

    public xSlider(int x, int y, Bitmap image, Bitmap slider, Text text, int ID, int scale) {
        super(x, y, image, slider, scale);
        jump = (imgWidth- sliderWidth)/scale;
        this.ID = ID;
        this.text = text;
        yShift = (imgHeigth - sliderHeight)/2;
        paint = new Paint();
        textSize = imgHeigth;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
        paint.setTextSize(textSize);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (ID != -1) {
            paint.getTextBounds(text.getText(ID), 0, text.getText(ID).length(), bounds);
            canvas.drawText(text.getText(ID), x, y - bounds.height(), paint);
            canvas.drawBitmap(slider,
                    positionInformation.mainCoord.x + value * jump + sliderWidth/3f,
                    positionInformation.mainCoord.y + yShift, null);
        } else {
            canvas.drawBitmap(slider,
                    positionInformation.mainCoord.x + value * jump + sliderWidth/3f,
                    positionInformation.mainCoord.y + yShift, null);
        }
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
