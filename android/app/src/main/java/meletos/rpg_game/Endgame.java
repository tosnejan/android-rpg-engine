package meletos.rpg_game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import java.io.IOException;

import meletos.rpg_game.navigation.MenuButton;

/**
 * GUI class of game end.
 */
public class Endgame {
    private GameHandler gameHandler;
    private Bitmap background;
    private MenuButton button;
    private Paint paint;
    private boolean buttonClicked;
    private int textSize;
    private int header;
    private int text;

    Endgame(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        load();
    }

    /**
     * Loads resources
     */
    private void load(){
        AssetManager am = gameHandler.context.getAssets();
        int screenHeight = gameHandler.getScreenHeight();
        int screenWidth = gameHandler.getScreenWidth();
        try {
            Bitmap image = BitmapFactory.decodeStream(am.open("menu/button.png"));
            image = Bitmap.createScaledBitmap(image, screenWidth/4, screenHeight/6, true);
            Bitmap imageClicked = BitmapFactory.decodeStream(am.open("menu/button_clicked.png"));
            imageClicked = Bitmap.createScaledBitmap(imageClicked, (int)(screenWidth/4), (int)(screenHeight/6), true);
            button = new MenuButton((screenWidth - image.getWidth())/2, 3*screenHeight/5, image, imageClicked, gameHandler.getText(), 9);
            background = BitmapFactory.decodeStream(am.open("menu/background.png"));
            background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);
            paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
            textSize = screenHeight/10;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Draws onto canvas.
     * @param canvas to draw on
     */
    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 0, null);
        button.draw(canvas);
        drawText(canvas);
    }

    void touchDown(int x, int y) {
        if (button.isTouched(x, y)) {
            button.changeImage(true, 10);
            buttonClicked = true;
        }
    }

    void touchUp(int x, int y) {
        if (buttonClicked && button.isTouched(x, y)) {
            gameHandler.getGameView().exitLevel();
        }
        button.changeImage(false, 0);
        buttonClicked = false;
    }

    /**
     * Draws text onto canvas
     */
    private void drawText(Canvas canvas){
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(gameHandler.getText().getText(header), 0, gameHandler.getText().getText(header).length(), bounds);
        canvas.drawText(gameHandler.getText().getText(header), (gameHandler.getScreenWidth() - bounds.width())/2f, 2*gameHandler.getScreenHeight()/6f, paint);
        paint.setTextSize(textSize/2f);
        paint.getTextBounds(gameHandler.getText().getText(text), 0, gameHandler.getText().getText(text).length(), bounds);
        canvas.drawText(gameHandler.getText().getText(text), (gameHandler.getScreenWidth() - bounds.width())/2f, 3*gameHandler.getScreenHeight()/6f, paint);
    }

    public void setMessage(int header, int text){
        this.header = header;
        this.text = text;
    }

}
