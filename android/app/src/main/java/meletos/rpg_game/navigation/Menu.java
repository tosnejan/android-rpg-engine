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
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.State;
import meletos.rpg_game.text.Text;

public class Menu {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap frame;
    private Bitmap buttonImage;
    private Bitmap buttonImageClicked;
    private Bitmap xButtonImage;
    private Bitmap xbuttonImageClicked;
    private int frameWidth;
    private int frameHeight;
    private PositionInformation positionInformation;
    private GameView gameView;
    private Context context;
    private MenuStates state = MenuStates.MAIN;
    private MenuButton[] buttons = new MenuButton[6];
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
        positionInformation = new PositionInformation(x, y, frameHeight,frameWidth);
    }

    private void loadImages(){
        AssetManager am = context.getAssets();
        try {
            frame = BitmapFactory.decodeStream(am.open("menu/frame.png"));
            frame = Bitmap.createScaledBitmap(frame, (int)(screenWidth/2.75), (int)(screenHeight/1.15), true);
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();
            x = (screenWidth - frameWidth)/2;
            y = (screenHeight - frameHeight)/2;
            buttonImage = BitmapFactory.decodeStream(am.open("menu/button.png"));
            buttonImage = Bitmap.createScaledBitmap(buttonImage, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            buttonImageClicked = BitmapFactory.decodeStream(am.open("menu/button_clicked.png"));
            buttonImageClicked = Bitmap.createScaledBitmap(buttonImageClicked, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            xButtonImage = BitmapFactory.decodeStream(am.open("menu/x_button_round.png"));
            xButtonImage = Bitmap.createScaledBitmap(xButtonImage, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
            xbuttonImageClicked = BitmapFactory.decodeStream(am.open("menu/x_button_round_clicked.png"));
            xbuttonImageClicked = Bitmap.createScaledBitmap(xbuttonImageClicked, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        switch (state) {
            case MAIN:
                canvas.drawBitmap(frame, x, y, null);
                for (Button button:buttons) {
                    button.draw(canvas);
                }
                break;
            case SETTINGS:
                canvas.drawBitmap(frame, x, y, null);
                break;
            case LOAD:
                canvas.drawBitmap(frame, x, y, null);
                break;
        }
    }

    public void touchDown(int x, int y) {
        switch (state) {
            case MAIN:
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        clickedButton = i;
                        buttons[i].changeImage(true);
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
                if (clickedButton != -1) buttons[clickedButton].changeImage(false);
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        if (i != this.clickedButton) clickedButton = -1;
                        break;
                    }
                    if(i == buttons.length - 1) clickedButton = -1;
                }
                switch (clickedButton){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        clickedButton = -1;
                        gameView.setState(State.MAP);
                        gameHandler.resumeGame();
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
        int buttonY = (y + frameHeight/11);
        int Yspace = (buttonImage.getHeight() + frameHeight/25);
        buttons[0] = new MenuButton(buttonX, buttonY, buttonImage, buttonImageClicked, text, 8);
        buttons[1] = new MenuButton(buttonX, buttonY + Yspace, buttonImage, buttonImageClicked, text, 5);
        buttons[2] = new MenuButton(buttonX, buttonY + Yspace*2, buttonImage, buttonImageClicked, text, 6);
        buttons[3] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 9);
        buttons[4] = new MenuButton(buttonX, buttonY + Yspace*4, buttonImage, buttonImageClicked, text, 7);
        buttons[5] = new MenuButton(x + frameWidth - 2*xButtonImage.getWidth()/3, y - xButtonImage.getHeight()/3, xButtonImage, xbuttonImageClicked, text, -1);
    }
}
