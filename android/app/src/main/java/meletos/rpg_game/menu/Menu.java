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
import android.util.Log;

import java.io.IOException;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.file_io.FileScout;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;

/**
 * Menu GUI and logic.
 */
public class Menu {
    private final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap frame;
    private Bitmap up;
    private Bitmap down;
    private Bitmap buttonImage;
    private Bitmap buttonImageClicked;
    private Bitmap xButtonImage;
    private Bitmap xButtonImageClicked;
    private Bitmap storyButtonImage;
    private Bitmap storyButtonImageClicked;
    private int frameWidth;
    private int frameHeight;
    private final Settings settings;
    private final GameView gameView;
    private final Context context;
    private MenuStates state = MenuStates.MAIN;
    private final MenuButton[] buttons = new MenuButton[5];
    private final MenuButton[] storyButtons = new MenuButton[6];
    private Story[] saves;
    private MenuButton xButton;
    private boolean xButtonClicked = false;
    private final Text text;
    private final GameHandler gameHandler;
    private int clickedButton = -1;
    private int shift = 0;
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
    }

    /**
     * Loads images.
     */
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
            storyButtonImage = BitmapFactory.decodeStream(am.open("menu/storyButton.png"));
            storyButtonImage = Bitmap.createScaledBitmap(storyButtonImage, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            storyButtonImageClicked = BitmapFactory.decodeStream(am.open("menu/storyButtonClicked.png"));
            storyButtonImageClicked = Bitmap.createScaledBitmap(storyButtonImageClicked, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            xButtonImage = BitmapFactory.decodeStream(am.open("menu/x_button_round.png"));
            xButtonImage = Bitmap.createScaledBitmap(xButtonImage, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
            xButtonImageClicked = BitmapFactory.decodeStream(am.open("menu/x_button_round_clicked.png"));
            xButtonImageClicked = Bitmap.createScaledBitmap(xButtonImageClicked, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
            up = BitmapFactory.decodeStream(am.open("menu/slider_up.png"));
            up = Bitmap.createScaledBitmap(up, screenWidth/15, (int)(frameHeight/6.7), true);
            down = BitmapFactory.decodeStream(am.open("menu/slider_down.png"));
            down = Bitmap.createScaledBitmap(down, screenWidth/15, (int)(frameHeight/6.7), true);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Draw menu GUI.
     * @param canvas canvas to draw
     */
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
                xButton.draw(canvas);
                settings.draw(canvas);
                break;
            case LOAD:
                canvas.drawBitmap(frame, x, y, null);
                drawSaves(canvas);
                break;
        }
    }

    /**
     * Checking if buttons was clicked.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
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
                    for (int i = 0; i < storyButtons.length; i++) {

                        if ((i < 3 && i < saves.length)||i > 2) {
                            if (storyButtons[i].isTouched(x, y)) {
                                clickedButton = i;
                                storyButtons[i].changeImage(true, 10);
                                break;
                            }
                        }
                    }
                    break;
                case SAVE:
                    break;
            }
        }
    }

    /**
     * Checking if same button was clicked and do what it should do.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
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

                    /* Save game button */
                    case 0:
                        gameHandler.saveGameState();
                        gameView.setState(State.MAP);
                        gameView.takeScreenshot("file.jpg");
                        break;

                    /* Load game button */
                    case 1:
                        saves = FileScout.getSaves(context);
                        shift = 0;
                        state = MenuStates.LOAD;
                        break;

                    /* Settings */
                    case 2:
                        clickedButton = -1;
                        state = MenuStates.SETTINGS;
                        break;

                    /* Main menu */
                    case 3:
                        alertLeavingToMenu();
                        break;

                    /* Quit */
                    case 4:
                        alert();
                        break;
                }
                clickedButton = -1;
                break;
            case SETTINGS:
                if (settings.touchUp(x, y)){
                    state = MenuStates.MAIN;
                }
                break;
            case LOAD:
                if (clickedButton == -1) break;
                storyButtons[clickedButton].changeImage(false, 0);
                if (!storyButtons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton) {
                    case 0://First save
                        loadSave(shift);
                        break;
                    case 1://Second save
                        loadSave(shift+1);
                        break;
                    case 2://Third save
                        loadSave(shift+2);
                        break;
                    case 3://Back
                        state = MenuStates.MAIN;
                        break;
                    case 4://UP
                        if (shift != 0) shift--;
                        break;
                    case 5://DOWN
                        if (shift < saves.length - 3) shift++;
                        break;
                }
                break;
        }
    }

    /**
     * Creates buttons.
     */
    private void createButtons() {
        int buttonX = (screenWidth - buttonImage.getWidth())/2;
        int buttonY = (y + frameHeight/11);
        int Yspace = (buttonImage.getHeight() + frameHeight/25);
        buttons[0] = new MenuButton(buttonX, buttonY, buttonImage, buttonImageClicked, text, 8);
        buttons[1] = new MenuButton(buttonX, buttonY + Yspace, buttonImage, buttonImageClicked, text, 5);
        buttons[2] = new MenuButton(buttonX, buttonY + Yspace*2, buttonImage, buttonImageClicked, text, 6);
        buttons[3] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 9);
        buttons[4] = new MenuButton(buttonX, buttonY + Yspace*4, buttonImage, buttonImageClicked, text, 7);
        storyButtons[0] = new MenuButton(buttonX, buttonY, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[1] = new MenuButton(buttonX, buttonY + Yspace, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[2] = new MenuButton(buttonX, buttonY + Yspace*2, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[3] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 12);
        storyButtons[4] = new MenuButton(2*screenWidth/3, buttonY, up, up, text, -1);
        storyButtons[5] = new MenuButton(2*screenWidth/3, buttonY + Yspace*2, down, down, text, -1);
        xButton = new MenuButton(x + frameWidth - 2*xButtonImage.getWidth()/3, y - xButtonImage.getHeight()/3, xButtonImage, xButtonImageClicked, text, -1);
    }

    /**
     * Draw load GUI.
     * @param canvas canvas to draw.
     */
    private void drawSaves(Canvas canvas){
        for (int i = 0; i < 3 && i < saves.length; i++) {
            storyButtons[i].draw(canvas, saves[i+shift].getImage(), saves[i+shift].getName());
        }
        storyButtons[3].draw(canvas);
        if (shift != 0) storyButtons[4].draw(canvas);
        if (shift < saves.length - 3) storyButtons[5].draw(canvas);
    }

    /**
     * Loads story from file rpg_game_data/save.
     * @param shift what save
     */
    private void loadSave(int shift) {
        gameView.getFileManager().setJob(saves[shift].getPath());
        // extract this into a function
        gameView.setState(State.LOADING);
        while (!gameView.hasGameHandler()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }
        gameView.getGameHandler().setHero(gameView.getFileManager().loadHeroProperties());
        gameView.setState(State.MAP);
        state = MenuStates.MAIN;
        gameView.getGameHandler().startGame();
    }

    public MenuStates getState() {
        return state;
    }

    public void setState(MenuStates state){
        this.state = state;
    }

    /**
     * Popup window -- when user attempts to exit the game.
     */
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

    /**
     * Popup window -- when user attempts to go to main menu.
     */
    private void alertLeavingToMenu(){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(text.getText(2))
                .setMessage(text.getText(3))
                .setPositiveButton(text.getText(1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.exitLevel();
                        gameView.setState(State.MAIN_MENU);
                    }
                })
                .setNegativeButton(text.getText(0), null)
                .show();
    }
}
