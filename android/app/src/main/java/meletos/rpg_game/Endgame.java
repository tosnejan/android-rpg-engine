package meletos.rpg_game;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.io.IOException;

import meletos.rpg_game.navigation.MenuButton;

public class Endgame {
    private GameHandler gameHandler;
    private Bitmap background;
    private MenuButton button;
    private Paint paint;
    private boolean buttonClicked;
    private int textSize;

    public Endgame(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        load();
    }

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
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 0, null);
        button.draw(canvas);
        drawText(canvas);
    }

    public void touchDown(int x, int y) {
        if (button.isTouched(x, y)) {
            button.changeImage(true, 10);
            buttonClicked = true;
        }
    }

    public void touchUp(int x, int y) {
        if (buttonClicked && button.isTouched(x, y)) {
            gameHandler.getGameView().exitLevel();
            gameHandler.getGameView().sound.play(State.MAIN_MENU);
        }
        button.changeImage(false, 0);
        buttonClicked = false;
    }

    private void drawText(Canvas canvas){
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(gameHandler.getText().getText(20), 0, gameHandler.getText().getText(20).length(), bounds);
        canvas.drawText(gameHandler.getText().getText(20), (gameHandler.getScreenWidth() - bounds.width())/2f, 2*gameHandler.getScreenHeight()/6f, paint);
        paint.setTextSize(textSize-2);
        paint.getTextBounds(gameHandler.getText().getText(21), 0, gameHandler.getText().getText(21).length(), bounds);
        canvas.drawText(gameHandler.getText().getText(21), (gameHandler.getScreenWidth() - bounds.width())/2f, 3*gameHandler.getScreenHeight()/6f, paint);
    }

}
