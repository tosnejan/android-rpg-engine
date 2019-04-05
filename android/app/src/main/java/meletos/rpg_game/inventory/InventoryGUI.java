package meletos.rpg_game.inventory;

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
import meletos.rpg_game.inventory.itinerary.ItemType;
import meletos.rpg_game.inventory.itinerary.Itinerary;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;

public class InventoryGUI {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap frame;
    private int frameX;
    private int frameY;
    private int gridX;
    private int gridY;
    private int equX;
    private int equY;
    private boolean buttonTouched = false;
    private boolean xButtonTouched = false;
    private Text text;
    private MenuButton button;
    private MenuButton xButton;
    private GameView gameView;
    private Context context;
    private GameHandler gameHandler;
    private Itinerary itinerary;
    private Inventory inventory;

    public InventoryGUI(GameView gameView, Context context, GameHandler gameHandler, Text text, Itinerary itinerary) {
        this.gameView = gameView;
        this.context = context;
        this.gameHandler = gameHandler;
        this.itinerary = itinerary;
        this.text = text;
        load();
    }

    public void setInventory(Inventory inventory){
        this.inventory = inventory;
    }

    private void load(){
        AssetManager am = context.getAssets();
        try {
            Bitmap image = BitmapFactory.decodeStream(am.open("inventory/bag.png"));
            image = Bitmap.createScaledBitmap(image, (int) ( screenWidth / 10), (int) (screenWidth / 10), true);
            button = new MenuButton(screenWidth - image.getWidth(), 0, image, image, text, -1);
            frame = BitmapFactory.decodeStream(am.open("inventory/inventory_v2.png"));
            frame = Bitmap.createScaledBitmap(frame, screenWidth, screenHeight, true);
            frameX = 0;     //(screenWidth - frameWidth)/2;
            frameY = 0;     //(screenHeight - frameHeight)/2;
            gridX = screenWidth / 16;
            gridY = screenHeight / 9;
            equX = 11 * screenWidth / 16;
            equY = screenHeight / 9;
            /*Bitmap xButtonImage = BitmapFactory.decodeStream(am.open("menu/x_button_round.png"));
            xButtonImage = Bitmap.createScaledBitmap(xButtonImage, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
            Bitmap xbuttonImageClicked = BitmapFactory.decodeStream(am.open("menu/x_button_round_clicked.png"));
            xbuttonImageClicked = Bitmap.createScaledBitmap(xbuttonImageClicked, (int)(frameWidth/6.7), (int)(frameWidth/6.7), true);
            xButton = new MenuButton(frameX + frameWidth - 2*xButtonImage.getWidth()/3, frameY - xButtonImage.getHeight()/3, xButtonImage, xbuttonImageClicked, text, -1);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        if (gameView.getState() == State.MAP){
            button.draw(canvas);
        } else {
            canvas.drawBitmap(frame, frameX, frameY, null);
            //xButton.draw(canvas);
            drawInventory(canvas);
            drawEquipped(canvas);
        }
    }

    public boolean buttonTouchedDown(int x, int y){
        if (button.isTouched(x, y) && gameView.getState() == State.MAP){
            buttonTouched = true;
            return true;
        } else {
            buttonTouched = false;
            /*if (xButton.isTouched(x, y)){
                xButtonTouched = true;
                xButton.changeImage(true, 0);
            }*/
            return false;
        }
    }

    public void buttonTouchedUp(int x, int y){
        if (buttonTouched && gameView.getState() == State.MAP && button.isTouched(x, y)){
            gameView.setState(State.INVENTORY);
            inventory = gameHandler.getInventory();
        } else {
            buttonTouched = false;
           /* if (xButtonTouched) xButton.changeImage(false, 0);
            if (xButtonTouched && xButton.isTouched(x, y)){
                xButtonTouched = false;
                gameView.setState(State.MAP);
            }*/
        }
    }

    private void drawInventory(Canvas canvas){
        int ID;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 8; column++) {
                if (( ID = inventory.getInventoryItem(row, column)) != -1) {
                    itinerary.getItem(ID).draw(gridX + (screenWidth / 16 + screenWidth / 240) * column, gridY + (screenHeight / 9 + screenHeight / 135) * row, canvas);
                }
            }
        }
    }

    private void drawEquipped(Canvas canvas){
        for (ItemType type:ItemType.values()) {
            int ID = inventory.getEquipedItem(type);
            if (ID != -1){
                switch (type) {
                    case HELMET:
                        itinerary.getItem(ID).draw(equX, equY, canvas);
                        break;
                    case CHESTPLATE:
                        itinerary.getItem(ID).draw(equX, gridY + screenHeight / 9 + screenHeight / 135, canvas);
                        break;
                    case TROUSERS:
                        itinerary.getItem(ID).draw(equX, gridY + (screenHeight / 9 + screenHeight / 135) * 2, canvas);
                        break;
                    case SHOES:
                        itinerary.getItem(ID).draw(equX, gridY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case WEAPON:
                        itinerary.getItem(ID).draw(equX + screenWidth / 16 + screenWidth / 240,  gridY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case SHIELD:
                        itinerary.getItem(ID).draw(equX + (screenWidth / 16 + screenWidth / 240) * 2,  gridY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case NECKLACE:
                        itinerary.getItem(ID).draw(equX + (screenWidth / 16 + screenWidth / 240) * 3,  gridY, canvas);
                        break;
                    case GLOVES:
                        itinerary.getItem(ID).draw(equX + (screenWidth / 16 + screenWidth / 240) * 3,  gridY + screenHeight / 9 + screenHeight / 135, canvas);
                        break;
                    case RING:
                        itinerary.getItem(ID).draw(equX + (screenWidth / 16 + screenWidth / 240) * 3,  gridY + (screenHeight / 9 + screenHeight / 135) * 2, canvas);
                        break;
                    case BELT:
                        itinerary.getItem(ID).draw(equX + (screenWidth / 16 + screenWidth / 240) * 3,  gridY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                }
            }
        }

    }
}
