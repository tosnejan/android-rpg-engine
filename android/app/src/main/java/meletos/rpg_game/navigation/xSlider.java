package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class xSlider extends Slider {
    private int jump;
    private int variable;

    public xSlider(int x, int y, Bitmap image, Bitmap slider, int scale) {
        super(x, y, image, slider, scale);
        jump = imgWidth/scale;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(slider,
                positionInformation.mainCoord.x, positionInformation.mainCoord.y, null);
    }

    @Override
    public void onTouch(int x, int y) {

    }
}
