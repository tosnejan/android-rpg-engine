
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

public class Loading {
    private GameView gameView;
    private Bitmap background;
    private Paint paint;
    private int textSize;
    private String text = "Loading...";

    public Loading(GameView gameView) {
        this.gameView = gameView;
        load();
    }

    private void load() {
        AssetManager am = gameView.getContext().getAssets();
        int screenHeight = gameView.getScreenHeight();
        int screenWidth = gameView.getScreenWidth();
        try {
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
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, (gameView.getScreenWidth() - bounds.width())/2f, gameView.getScreenHeight()/2f, paint);
        paint.setTextSize(textSize/2f);
    }

}
