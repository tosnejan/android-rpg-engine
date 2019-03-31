package meletos.rpg_game.navigation;

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
import meletos.rpg_game.text.Text;

public class Menu {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap image;
    private Bitmap buttonImage;
    private GameView gameView;
    private Context context;
    private MenuStates state = MenuStates.MAIN;
    private MenuButton[] buttons = new MenuButton[4];
    private Text text;
    private GameHandler gameHandler;
    private int clickedButton = -1;
    private int x;
    private int y;

    public Menu(GameView gameView, Context context, Text text, GameHandler gameHandler) {
        this.gameView = gameView;
        this.context = context;
        this.text = text;
        this.gameHandler = gameHandler;
        loadImages();
        createButtons();
    }

    private void loadImages(){
        AssetManager am = context.getAssets();
        try {
            image = BitmapFactory.decodeStream(am.open("menu/frame.png"));
            x = (screenWidth - image.getWidth())/2;
            y = (screenHeight - image.getHeight())/2;
            buttonImage = BitmapFactory.decodeStream(am.open("menu/button.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        switch (state) {
            case MAIN:
                canvas.drawBitmap(image, x, y, null);
                for (Button button:buttons) {
                    button.draw(canvas);
                }
                break;
            case SETTINGS:
                canvas.drawBitmap(image, x, y, null);
                break;
            case LOAD:
                canvas.drawBitmap(image, x, y, null);
                break;
        }
    }

    public void touchDown(int x, int y) {
        switch (state) {
            case MAIN:
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        clickedButton = i;
                    }
                }
                break;
            case SETTINGS:
                break;
            case LOAD:
                break;
        }
    }

    public void touchUp(int x, int y) {
        switch (state) {
            case MAIN:
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        if (i != this.clickedButton) clickedButton = -1;
                        break;
                    }
                    if(i == buttons.length - 1) clickedButton = -1;
                }
                switch (clickedButton){
                    case 0:
                        clickedButton = -1;
                        gameView.setState(State.MAP);
                        gameHandler.resumeGame();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                break;
            case SETTINGS:
                break;
            case LOAD:
                break;
        }
    }

    private void createButtons() {
        int buttonX = (screenWidth - buttonImage.getWidth())/2;
        buttons[0] = new MenuButton(buttonX, y + 105, buttonImage, text, 8);
        buttons[1] = new MenuButton(buttonX, y + 310, buttonImage, text, 5);
        buttons[2] = new MenuButton(buttonX, y + 515, buttonImage, text, 6);
        buttons[3] = new MenuButton(buttonX, y + 720, buttonImage, text, 7);
    }
}
