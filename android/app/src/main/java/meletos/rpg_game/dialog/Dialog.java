package meletos.rpg_game.dialog;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.IOException;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.text.Text;

public class Dialog {
    private GameHandler gameHandler;
    private GameView gameView;
    private Bitmap background;
    private Text text;
    private Paint paint;
    private int textSize;

    public Dialog(GameHandler gameHandler, GameView gameView, Text text) {
        this.gameHandler = gameHandler;
        this.gameView = gameView;
        this.text = text;
        load();
    }

    private void load() {
        AssetManager am = gameHandler.context.getAssets();
        try {
            background = BitmapFactory.decodeStream(am.open("battle/background.png"));
            background = Bitmap.createScaledBitmap(background, gameHandler.getScreenWidth(), gameHandler.getScreenHeight()/5, true);
            paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
            textSize = gameHandler.getScreenHeight()/10;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 4*gameHandler.getScreenHeight()/5f,null);
    }

    public void touchDown(int x, int y) {

    }

    public void touchUp(int x, int y) {

    }
}
