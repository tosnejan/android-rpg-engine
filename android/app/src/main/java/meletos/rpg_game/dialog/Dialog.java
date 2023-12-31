package meletos.rpg_game.dialog;

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

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.text.Text;

/**
 * Class representing dialogues.
 */
public class Dialog {
    private final GameHandler gameHandler;
    private final GameView gameView;
    private Bitmap background;
    private final Text text;
    private Paint paint;
    private int actualSentece;
    private int[] dialog;
    private FatherCharacter character;
    private int side = -1;

    public Dialog(GameHandler gameHandler, GameView gameView, Text text) {
        this.gameHandler = gameHandler;
        this.gameView = gameView;
        this.text = text;
        load();
    }

    /**
     * Loads resources.
     */
    private void load() {
        AssetManager am = gameHandler.context.getAssets();
        try {
            background = BitmapFactory.decodeStream(am.open("menu/text.png"));
            background = Bitmap.createScaledBitmap(background, gameHandler.getScreenWidth(), gameHandler.getScreenHeight()/5, true);
            paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.BLACK);
            paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
            paint.setTextSize(gameHandler.getScreenHeight()/10f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 4*gameHandler.getScreenHeight()/5f,null);
        Rect bounds = new Rect();
        paint.getTextBounds(text.getDialog(dialog[actualSentece]), 0, text.getDialog(dialog[actualSentece]).length(), bounds);
        canvas.drawText(text.getDialog(dialog[actualSentece]), (gameHandler.getScreenWidth() - bounds.width())/2f, 9*gameHandler.getScreenHeight()/10f + bounds.height()/2f, paint);
    }

    /**
     * Reacts to touch. Used to scroll through dialogue.
     * @param x coord of touch
     * @param y coord of touch
     */
    public void touchDown(int x, int y) {
        if(x <= gameHandler.getScreenWidth()/2){
            side = 0;
        } else side = 1;
    }

    public void touchUp(int x, int y) {
        if (x <= gameHandler.getScreenWidth()/2 && side == 0){
            if (actualSentece - 1 >= 0){
                actualSentece--;
            }
        } else if (x > gameHandler.getScreenWidth()/2 && side == 1){
            if (actualSentece + 1 < dialog.length){
                actualSentece++;
                if (dialog[actualSentece] == -1){
                    gameView.getEndgame().setMessage(22, 23);
                    gameView.setState(State.ENDGAME);
                }
            } else {
                Log.i(this.getClass().getSimpleName(), "Returning from dialogue.");
                character.setPlayed(true);
                gameView.setState(State.MAP);
            }
        }
        side = -1;
    }

    public void init(FatherCharacter character) {
        this.dialog = character.getDialog();
        this.character = character;
        actualSentece = 0;
        if (dialog[0] == -1){
            gameView.getEndgame().setMessage(22, 23);
            gameView.setState(State.ENDGAME);
        }
    }
}
