package meletos.rpg_game.battle;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.util.HashMap;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.inventory.InventoryGUI;
import meletos.rpg_game.navigation.GameButton;
import meletos.rpg_game.navigation.MenuButton;

public class BattleGUI {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private GameHandler gameHandler;
    private GameView gameView;
    private FatherCharacter enemy;
    private Bitmap background;
    private Bitmap enemyImage;
    private Context context;
    private InventoryGUI inventory;
    private GameButton[] buttons = new GameButton[4];
    private int clickedButton = -1;

    public BattleGUI(GameHandler gameHandler, GameView gameView, Context context, InventoryGUI inventory) {
        this.gameHandler = gameHandler;
        this.gameView = gameView;
        this.inventory = inventory;
        this.context = context;
        load();
    }

    private void load(){
        AssetManager am = context.getAssets();
        try {
            background = BitmapFactory.decodeStream(am.open("battle/background.png"));
            background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);
            Bitmap image = BitmapFactory.decodeStream(am.open("battle/button1.png"));
            image = Bitmap.createScaledBitmap(image, screenHeight / 4, screenHeight / 4, true);
            buttons[0] = new GameButton(0, 0, image, image);
            buttons[3] = new GameButton(0, 3*image.getWidth(), image, image);
            image = BitmapFactory.decodeStream(am.open("battle/button2.png"));
            image = Bitmap.createScaledBitmap(image, screenHeight / 4, screenHeight / 4, true);
            buttons[1] = new GameButton(0, image.getWidth(), image, image);
            image = BitmapFactory.decodeStream(am.open("battle/button3.png"));
            image = Bitmap.createScaledBitmap(image, screenHeight / 4, screenHeight / 4, true);
            buttons[2] = new GameButton(0, 2*image.getWidth(), image, image);
            image = BitmapFactory.decodeStream(am.open("battle/sword.png"));
            buttons[0].setIcon(Bitmap.createScaledBitmap(image, screenHeight / 6, screenHeight / 6, true));
            image = BitmapFactory.decodeStream(am.open("battle/shield.png"));
            buttons[1].setIcon(Bitmap.createScaledBitmap(image, screenHeight / 6, screenHeight / 6, true));
            image = BitmapFactory.decodeStream(am.open("battle/potion.png"));
            buttons[2].setIcon(Bitmap.createScaledBitmap(image, screenHeight / 6, screenHeight / 6, true));
            image = BitmapFactory.decodeStream(am.open("battle/surrender.png"));
            buttons[3].setIcon(Bitmap.createScaledBitmap(image, screenHeight / 6, screenHeight / 6, true));
            /*equipped = BitmapFactory.decodeStream(am.open("inventory/equipped.png"));
            equipped = Bitmap.createScaledBitmap(equipped, 16*screenHeight/27, 79*screenHeight/135, true);
            grid = BitmapFactory.decodeStream(am.open("inventory/grid.png"));
            grid = Bitmap.createScaledBitmap(grid, 143*screenHeight/135, 79*screenHeight/135, true);
            frame = BitmapFactory.decodeStream(am.open("inventory/select.png"));
            frame = Bitmap.createScaledBitmap(frame, (int)(screenHeight/67.5 + screenHeight/9), (int)(screenHeight/67.5 + screenHeight/9), true);
            backgroundX = 0;     //(screenWidth - frameWidth)/2;
            backgroundY = 0;     //(screenHeight - frameHeight)/2;
            gridX = screenHeight / 9;
            gridY = screenHeight / 9;
            equX = screenWidth - equipped.getWidth() + screenHeight / 135;
            equY = screenHeight / 9;
            equDrawX = screenWidth - equipped.getWidth();
            image = BitmapFactory.decodeStream(am.open("menu/button.png"));
            image = Bitmap.createScaledBitmap(image, screenWidth/5, screenHeight/10, true);
            Bitmap imageClicked = BitmapFactory.decodeStream(am.open("menu/button_clicked.png"));
            imageClicked = Bitmap.createScaledBitmap(imageClicked, screenWidth/5, screenHeight/10, true);
            equButton = new MenuButton(gridX + screenWidth/2 + screenWidth/30 - image.getWidth(), screenHeight - screenHeight/9 - image.getHeight(), image, imageClicked, text, 13);
            Bitmap xButtonImage = BitmapFactory.decodeStream(am.open("menu/x_button_round.png"));
            xButtonImage = Bitmap.createScaledBitmap(xButtonImage, screenHeight/11, screenHeight/11, true);
            Bitmap xbuttonImageClicked = BitmapFactory.decodeStream(am.open("menu/x_button_round_clicked.png"));
            xbuttonImageClicked = Bitmap.createScaledBitmap(xbuttonImageClicked, screenHeight/11, screenHeight/11, true);
            xButton = new MenuButton(screenWidth - 9*xButtonImage.getWidth()/8, xButtonImage.getHeight()/8, xButtonImage, xbuttonImageClicked, text, -1);
            paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.BLACK);
            paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
            nameSize = (screenHeight - grid.getHeight())/8;
            textSize = (screenHeight - grid.getHeight())/10;*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(enemyImage, 4*screenWidth/7f - enemyImage.getWidth()/2f, screenHeight/2f - enemyImage.getHeight()/2f, null);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].draw(canvas);
        }
        gameHandler.getBattle().draw(canvas);
    }

    public void touchDown(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isTouched(x, y)) {
                clickedButton = i;
                buttons[i].changeImage(true, 10);
            }
        }
    }

    public void touchUp(int x, int y) {
        if (clickedButton != -1){
            buttons[clickedButton].changeImage(false, 0);
            if (!buttons[clickedButton].isTouched(x, y)) clickedButton = -1;
            switch (clickedButton){
                case 0://attack
                    gameHandler.removeCharacter(enemy);
                    gameView.setState(State.MAP);
                    break;
                case 1://shield
                    gameHandler.getBattle().setShield();
                    break;
                case 2://potion
                    gameHandler.getBattle().healChar();
                    break;
                case 3://escape
                    gameView.setState(State.MAP);
                    break;
            }
            clickedButton = -1;
        }
    }

    public void init(){
        enemy = gameHandler.getFighting();
        enemyImage = enemy.getCharacterImage();
    }

}
