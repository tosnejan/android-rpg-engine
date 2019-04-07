package meletos.rpg_game.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.MenuButton;
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
    private Settings settings;
    private PositionInformation positionInformation;
    private GameView gameView;
    private Context context;
    private MenuStates state = MenuStates.MAIN;
    private MenuButton[] buttons = new MenuButton[5];
    private MenuButton xButton;
    private boolean xButtonClicked = false;
    private Text text;
    private GameHandler gameHandler;
    private int clickedButton = -1;
    private int x;
    private int y;

    public Menu(GameHandler gameHandler, GameView gameView, Context context, Text text, Settings settings) {
        this.gameView = gameView;
        this.context = context;
        this.text = text;
        this.gameHandler = gameHandler;
        this.settings = settings;
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
                xButton.draw(canvas);
                for (Button button:buttons) {
                    button.draw(canvas);
                }
                break;
            case SETTINGS:
                canvas.drawBitmap(frame, x, y, null);
                settings.draw(canvas);
                break;
            case LOAD:
                canvas.drawBitmap(frame, x, y, null);
                break;
        }
    }

    public void touchDown(int x, int y) {
        if (xButton.isTouched(x, y)){
            xButtonClicked = true;
            xButton.changeImage(true, 0);
        } else {
        switch (state) {
            case MAIN:
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        clickedButton = i;
                        buttons[i].changeImage(true, 10);
                    }
                }
                break;
            case SETTINGS:

                settings.touchDown(x, y);
                break;
            case LOAD:
                break;
            case SAVE:
                break;
        }
        }
    }

    public void touchUp(int x, int y) {
        if (xButtonClicked) xButton.changeImage(false, 0);
        if (xButtonClicked && xButton.isTouched(x, y)){
            xButtonClicked = false;
            state = MenuStates.MAIN;
            gameView.setState(State.MAP);
        }
        switch (state) {
            case MAIN:
                if (clickedButton == -1) break;
                buttons[clickedButton].changeImage(false, 0);
                if (!buttons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton){
                    case 0:
                        gameHandler.saveGameState();
                        gameView.setState(State.MAP);
                        gameView.takeScreenshot("file.jpg");
                        clickedButton = -1;
                        break;
                    case 1:
                        clickedButton = -1;
                        break;
                    case 2:
                        clickedButton = -1;
                        state = MenuStates.SETTINGS;
                        break;
                    case 3:
                        gameView.setState(State.MAIN_MENU);
                        clickedButton = -1;
                        break;
                    case 4:
                        alert();
                        clickedButton = -1;
                        break;
                }
                break;
            case SETTINGS:
                if (settings.touchUp(x, y)){
                    state = MenuStates.MAIN;
                }
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
        xButton = new MenuButton(x + frameWidth - 2*xButtonImage.getWidth()/3, y - xButtonImage.getHeight()/3, xButtonImage, xbuttonImageClicked, text, -1);
    }

    public MenuStates getState() {
        return state;
    }

    private void alert(){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(text.getText(2))
                .setMessage(text.getText(3))
                .setPositiveButton(text.getText(1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                    }
                })
                .setNegativeButton(text.getText(0), null)
                .show();
    }
}
