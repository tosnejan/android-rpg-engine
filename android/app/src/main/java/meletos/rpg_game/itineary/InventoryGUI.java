package meletos.rpg_game.itineary;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;

public class InventoryGUI {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap frame;
    private int frameWidth;
    private int frameHeight;
    private int bagx;
    private int bagy;
    private boolean buttonTouched = false;
    private Text text;
    private MenuButton button;
    private GameView gameView;
    private Context context;
    private GameHandler gameHandler;
    private InventoryStates state = InventoryStates.SLEEPING;

    public InventoryGUI(GameView gameView, Context context, GameHandler gameHandler, Text text) {
        this.gameView = gameView;
        this.context = context;
        this.gameHandler = gameHandler;
        this.text = text;
        load();
    }

    private void load(){
        AssetManager am = context.getAssets();
        try {
            Bitmap image = BitmapFactory.decodeStream(am.open("inventory/bag.png"));
            image = Bitmap.createScaledBitmap(image, (int) ( screenWidth/ 10), (int) (screenWidth / 10), true);
            button = new MenuButton(screenWidth - image.getWidth(), 0, image, image, text, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        if (gameView.getState() == State.MAP){
            button.draw(canvas);
        } else {

        }
    }

    public boolean buttonTouchedDown(int x, int y){
        if (button.isTouched(x, y)){
            buttonTouched = true;
            return true;
        } else {
            buttonTouched = false;
            return false;
        }
    }

    public void buttonTouchedUp(int x, int y){
        if (button.isTouched(x, y) && buttonTouched){
            gameView.setState(State.INVENTORY);
            state = InventoryStates.SHOWN;
        } else {
            buttonTouched = false;
        }
    }
}
