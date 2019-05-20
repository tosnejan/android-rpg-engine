
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

/**
 * Loading screen.
 */
public class Loading {
    private int animationStage = 0; // used for animating
    private int animationSteps = 0;
    private GameView gameView;
    private Bitmap background;
    private Paint paint;
    private int textSize;
    private String text = "Loading"; // text that gets painted
    private String textToDraw;

    Loading(GameView gameView) {
        this.gameView = gameView;
        load();
    }

    /**
     * Loads needed resources from assets
     */
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
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Draws onto screen
     * @param canvas to draw on
     */
    public void draw(Canvas canvas){
        textToDraw = text;
        for (int i = 0; i < animationStage; i++) {
            textToDraw += ".";
        }
        if (animationSteps == 10) {
            animationSteps = 0;
            if (animationStage > 2) {
                animationStage = 0;
            } else {
                animationStage++;
            }
        }
        animationSteps++;

        canvas.drawBitmap(background, 0, 0, null);
        drawText(canvas);
    }

    /**
     * Helper function to draw text.
     * @param canvas to draw on
     */
    private void drawText(Canvas canvas){
        Rect bounds = new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds(textToDraw, 0, textToDraw.length(), bounds);
        canvas.drawText(textToDraw, (gameView.getScreenWidth() - 442)/2f, gameView.getScreenHeight()/2f, paint);
        paint.setTextSize(textSize/2f);
    }

}
