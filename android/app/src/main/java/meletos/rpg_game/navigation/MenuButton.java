package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.text.Text;

public class MenuButton extends Button {
    private int ID;
    private Paint paint;
    private Text text;

    public MenuButton(int x, int y, Bitmap image, Text text, int ID) {
        super(x,y,image);
        this.text = text;
        this.ID = ID;
        paint = new Paint();
    }

    @Override
    public boolean isTouched(int x, int y) {
        Coordinates touchCoord = new Coordinates(x, y);
        if (positionInformation.isCoordinateInside(touchCoord)) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect bounds = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create("Arial",Typeface.ITALIC));
        paint.setTextSize(90);
        paint.getTextBounds(text.getText(ID), 0, text.getText(ID).length(), bounds);
        canvas.drawText(text.getText(ID),
                (x + imgWidth/2f) - bounds.width()/2f - bounds.left,
                (y + imgHeigth/2f) + bounds.height()/2f - bounds.bottom,
                paint);
    }
}
