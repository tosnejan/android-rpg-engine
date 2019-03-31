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
    private Bitmap imageClicked;
    private Bitmap imageUnclicked;
    private Rect bounds = new Rect();
    private int textSize;
    private int yClick = 0;

    public MenuButton(int x, int y, Bitmap image, Bitmap imageClicked, Text text, int ID) {
        super(x,y,image);
        this.text = text;
        this.ID = ID;
        imageUnclicked = image;
        this.imageClicked = imageClicked;
        if (ID != -1) {
            paint = new Paint();
            textSize = (int) (imgHeigth / 1.5);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
            paint.setTextSize(textSize);
            setOptimalTextSize();
        }
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
        if(image != null){
            canvas.drawBitmap(image,
                    positionInformation.mainCoord.x, positionInformation.mainCoord.y + yClick, null);
        }
        if (ID != -1) {
            paint.getTextBounds(text.getText(ID), 0, text.getText(ID).length(), bounds);
            if (bounds.width() > 9 * imgWidth / 10) {
                setOptimalTextSize();
            }
            canvas.drawText(text.getText(ID),
                    (x + imgWidth / 2f) - bounds.width() / 2f - bounds.left,
                    (y + yClick + imgHeigth / 2f) + bounds.height() / 2f - bounds.bottom,
                    paint);
        }
    }
    private void setOptimalTextSize(){
        while (bounds.width() > 9*imgWidth/10){
            textSize -= 1;
            paint.setTextSize(textSize);
            paint.getTextBounds(text.getText(ID), 0, text.getText(ID).length(), bounds);
        }
    }

    public void changeImage(boolean isClicked){
        if (isClicked){
            image = imageClicked;
            yClick = 10;
        } else {
            image = imageUnclicked;
            yClick = 0;
        }
    }
}
