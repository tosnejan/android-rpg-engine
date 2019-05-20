package meletos.rpg_game.inventory;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.io.IOException;
import java.util.HashMap;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.inventory.itinerary.Item;
import meletos.rpg_game.inventory.itinerary.ItemType;
import meletos.rpg_game.inventory.itinerary.Itinerary;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;
public class InventoryGUI {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap background;
    private Bitmap frame;
    private Bitmap grid;
    private Bitmap equipped;
    private Bitmap hero;
    private int backgroundX;
    private int backgroundY;
    private int gridX;
    private int gridY;
    private int equX;
    private int equY;
    private int heroX;
    private int heroY;
    private int equDrawX;
    private int selectedX = -1;
    private int selectedY = -1;
    private int newSelectedX = -1;
    private int newSelectedY = -1;
    private ItemType equSelected = null;
    private ItemType newEquSelected = null;
    private boolean buttonTouched = false;
    private boolean xButtonTouched = false;
    private boolean equButtonTouched = false;
    private Paint paint;
    private int nameSize;
    private int textSize;
    private Rect bounds = new Rect();
    private Text text;
    private MenuButton button;
    private MenuButton xButton;
    private MenuButton equButton;
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

    /**
     * Loads things what it needs.
     */
    private void load(){
        AssetManager am = context.getAssets();
        try {
            Bitmap image = BitmapFactory.decodeStream(am.open("inventory/bag.png"));
            image = Bitmap.createScaledBitmap(image, screenWidth / 10, screenWidth / 10, true);
            button = new MenuButton(screenWidth - image.getWidth(), 0, image, image, text, -1);
            background = BitmapFactory.decodeStream(am.open("inventory/bg_01.png"));
            background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);
            equipped = BitmapFactory.decodeStream(am.open("inventory/equipped.png"));
            equipped = Bitmap.createScaledBitmap(equipped, 16*screenHeight/27, 79*screenHeight/135, true);
            grid = BitmapFactory.decodeStream(am.open("inventory/grid.png"));
            grid = Bitmap.createScaledBitmap(grid, 143*screenHeight/135, 79*screenHeight/135, true);
            frame = BitmapFactory.decodeStream(am.open("inventory/select.png"));
            frame = Bitmap.createScaledBitmap(frame, (int)(screenHeight/67.5 + screenHeight/9), (int)(screenHeight/67.5 + screenHeight/9), true);
            backgroundX = 0;
            backgroundY = 0;
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
            textSize = (screenHeight - grid.getHeight())/10;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw inventory GUI.
     * @param canvas canvas to draw
     */
    public void draw(Canvas canvas){
        if (gameView.getState() == State.MAP){
            button.draw(canvas);
        } else if (gameView.getState() == State.INVENTORY){
            canvas.drawBitmap(background, backgroundX, backgroundY, null);
            canvas.drawBitmap(grid, 0, 0, null);
            canvas.drawBitmap(equipped, equDrawX, 0, null);
            xButton.draw(canvas);
            drawInventory(canvas);
            drawEquipped(canvas);
            canvas.drawBitmap(hero, heroX, heroY, null);
            writePlayerStats(canvas);
            if (selectedY != -1 && selectedX != -1){
                int ID = inventory.getInventoryItem(selectedY, selectedX);
                if (ID != -1) {
                    canvas.drawBitmap(frame, (gridX - screenHeight / 135f) + (((screenHeight / 9f) + (screenHeight / 135f)) * selectedX), gridY - screenHeight / 135f + (screenHeight / 9f + screenHeight / 135f) * selectedY, null);
                }
                if (ID != -1) {
                    if (itinerary.getItem(ID).getType() != ItemType.OTHER) {
                        equButton.draw(canvas);
                        writeDescription(canvas, ID, itinerary.getItem(ID).getType());
                    } else {
                        writeDescription(canvas, ID, itinerary.getItem(ID).getType());
                    }
                }
            } else if (equSelected != null){
                int ID = inventory.getEquipedItem(equSelected);
                if (ID != -1) {
                    drawEquippedFrame(canvas);
                    equButton.draw(canvas);
                    writeDescription(canvas, ID, itinerary.getItem(ID).getType());
                }
            }
        }
    }

    /**
     * Checking if buttons was clicked.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     * @return <code>true</code> if button touched and {@link GameView} {@link State}
     *          is <code>MAP</code>; <code>false</code> if {@link GameView} {@link State}
     *          is not <code>MAP</code>
     */
    public boolean touchDown(int x, int y){
        if (button.isTouched(x, y) && gameView.getState() == State.MAP){
            buttonTouched = true;
            return true;
        } else if (gameView.getState() == State.INVENTORY){
            buttonTouched = false;
            if (xButton.isTouched(x, y)){
                xButtonTouched = true;
                xButton.changeImage(true, 0);
            } else {
                itemsTouchedDown(x, y);
                equippedTouchedDown(x, y);
                if (equButton.isTouched(x, y)){
                    equButtonTouched = true;
                    equButton.changeImage(true, 10);
                }
            }
            return false;
        }
        return false;
    }

    /**
     * Checking if same button was clicked and do what it should do.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    public void touchUp(int x, int y){
        if (buttonTouched && gameView.getState() == State.MAP && button.isTouched(x, y)){
            gameView.setState(State.INVENTORY);
            inventory = gameHandler.getInventory();
            if (hero == null){
                Bitmap image = gameHandler.getHeroImage();
                double ratio = image.getHeight()/(double)image.getWidth();
                hero = Bitmap.createScaledBitmap(image,(int)(screenHeight/67.5 + screenHeight/9)*2 - screenHeight / 135,(int)(((screenHeight/67.5 + screenHeight/9)*2 - screenHeight / 135)*ratio),false);
                heroX = equX + screenHeight / 9 - screenHeight / 135;
                heroY = equY + ((screenHeight / 9 + screenHeight / 135) * 3 - screenHeight / 135)/2 - hero.getHeight()/2;
            }
        } else if (gameView.getState() == State.INVENTORY){
            if (xButtonTouched) xButton.changeImage(false, 0);
            if (xButtonTouched && xButton.isTouched(x, y)){
                xButtonTouched = false;
                gameView.setState(State.MAP);
            } else if (equButtonTouched){
                equButtonTouched = false;
                equButton.changeImage(false, 0);
                if (equButton.isTouched(x, y)){
                    if (selectedX != -1 && selectedY != -1) {
                        Item item = itinerary.getItem(inventory.getInventoryItem(selectedY, selectedX));
                        if (item.getType() != ItemType.OTHER) {
                            int ID = inventory.getEquipedItem(item.getType());
                            inventory.setEquipedItem(item.getType(), item.getID());
                            inventory.setInventoryItem(selectedY, selectedX, ID);
                        }
                    } else  if (equSelected != null){
                        inventory.unequipItem(equSelected);

                    }
                }
            } else {
                if (itemsTouchedUp(x, y)){
                    selectedX = newSelectedX;
                    selectedY = newSelectedY;
                    newSelectedX = -1;
                    newSelectedY = -1;
                    equSelected = null;
                    newEquSelected = null;
                    equButton.changeTextID(13);
                } else if (equippedTouchedUp(x, y) && !buttonTouched){
                    equSelected = newEquSelected;
                    newEquSelected = null;
                    selectedX = -1;
                    selectedY = -1;
                    newSelectedX = -1;
                    newSelectedY = -1;
                    equButton.changeTextID(14);
                }
            }
        }
        buttonTouched = false;
    }

    /**
     * Checking if item was selected.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    private void itemsTouchedDown(int x, int y) {
        for (int row = 0; row < 4; row++) {
            int up = (gridY + (screenHeight / 9 + screenHeight / 135) * row);
            int down = up + screenHeight / 9;
            if (up < y && y < down){
                newSelectedY = row;
                break;
            } else {
                newSelectedY = -1;
            }
        }
        if (newSelectedY == -1) {
            newSelectedX = -1;
            return;
        }
        for (int column = 0; column < 8; column++) {
            int left = gridX + (screenHeight / 9 + screenHeight / 135) * column;
            int right = left + screenHeight / 9;
            if (left < x && x < right){
                newSelectedX = column;
                return;
            } else {
                newSelectedX = -1;
            }
        }
    }

    /**
     * Checking if item was selected.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    private boolean itemsTouchedUp(int x, int y) {
        for (int row = 0; row < 4; row++) {
            int up = (gridY + (screenHeight / 9 + screenHeight / 135) * row);
            int down = up + screenHeight / 9;
            if (up < y && y < down){
                if (newSelectedY == row){
                    for (int column = 0; column < 8; column++) {
                        int left = gridX + (screenHeight / 9 + screenHeight / 135) * column;
                        int right = left + screenHeight / 9;
                        if (left < x && x < right) {
                            return newSelectedX == column;
                        }
                    }
                }
                break;
            }
        }
        return false;
    }

    /**
     * Checking if equipped item was selected.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    private void equippedTouchedDown(int x, int y) {
        if (equX + screenHeight / 9 + screenHeight / 135 < x && x < equX + 2*screenHeight / 9 + screenHeight / 135){
            if (equY + (screenHeight / 9 + screenHeight / 135) * 3 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9){
                newEquSelected = ItemType.WEAPON;
            }
        } else if (equX + (screenHeight / 9 + screenHeight / 135) * 2 < x && x < equX + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9){
            if (equY + (screenHeight / 9 + screenHeight / 135) * 3 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9){
                newEquSelected = ItemType.SHIELD;
            }
        } else if (equX < x && x < equX + screenHeight / 9){
            if (equY < y && y < equY + screenHeight / 9){
                newEquSelected = ItemType.HELMET;
            } else if (equY + screenHeight / 9 + screenHeight / 135 < y && y < equY + 2*screenHeight / 9 + screenHeight / 135){
                newEquSelected = ItemType.ARMOR;
            } else if (equY + (screenHeight / 9 + screenHeight / 135) * 2 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9){
                newEquSelected = ItemType.PANTS;
            } else if (equY + (screenHeight / 9 + screenHeight / 135) * 3 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9){
                newEquSelected = ItemType.SHOES;
            }
        } else if (equX + (screenHeight / 9 + screenHeight / 135) * 3 < x && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9){
            if (equY < y && y < equY + screenHeight / 9){
                newEquSelected = ItemType.NECKLACE;
            } else if (equY + screenHeight / 9 + screenHeight / 135 < y && y < equY + 2*screenHeight / 9 + screenHeight / 135){
                newEquSelected = ItemType.GLOVES;
            } else if (equY + (screenHeight / 9 + screenHeight / 135) * 2 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9){
                newEquSelected = ItemType.RING;
            } else if (equY + (screenHeight / 9 + screenHeight / 135) * 3 < y && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9){
                newEquSelected = ItemType.BELT;
            }
        } else {
            newEquSelected = null;
        }
    }

    /**
     * Checking if equipped item was selected.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    private boolean equippedTouchedUp(int x, int y) {
        if (newEquSelected != null){
            switch (newEquSelected) {
                case HELMET:
                    return equX < x
                            && x < equX + screenHeight / 9
                            && equY < y
                            && y < equY + screenHeight / 9;
                case ARMOR:
                    return equX < x
                            && x < equX + screenHeight / 9
                            && equY + screenHeight / 9 + screenHeight / 135 < y
                            && y < equY + 2*screenHeight / 9 + screenHeight / 135;
                case PANTS:
                    return equX < x
                            && x < equX + screenHeight / 9
                            && equY + (screenHeight / 9 + screenHeight / 135) * 2 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9;
                case SHOES:
                    return equX < x
                            && x < equX + screenHeight / 9
                            && equY + (screenHeight / 9 + screenHeight / 135) * 3 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9;
                case WEAPON:
                    return equX + screenHeight / 9 + screenHeight / 135 < x
                            && x < equX + 2*screenHeight / 9 + screenHeight / 135
                            && equY + (screenHeight / 9 + screenHeight / 135) * 3 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9;
                case SHIELD:
                    return equX + (screenHeight / 9 + screenHeight / 135) * 2 < x
                            && x < equX + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9
                            && equY + (screenHeight / 9 + screenHeight / 135) * 3 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9;
                case NECKLACE:
                    return equX + (screenHeight / 9 + screenHeight / 135) * 3 < x
                            && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9
                            && equY < y
                            && y < equY + screenHeight / 9;
                case GLOVES:
                    return equX + (screenHeight / 9 + screenHeight / 135) * 3 < x
                            && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9
                            && equY + screenHeight / 9 + screenHeight / 135 < y
                            && y < equY + 2*screenHeight / 9 + screenHeight / 135;
                case RING:
                    return equX + (screenHeight / 9 + screenHeight / 135) * 3 < x
                            && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9
                            && equY + (screenHeight / 9 + screenHeight / 135) * 2 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 2 + screenHeight / 9;
                case BELT:
                    return equX + (screenHeight / 9 + screenHeight / 135) * 3 < x
                            && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9
                            && equY + (screenHeight / 9 + screenHeight / 135) * 3 < y
                            && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9;
            }
        } else {
            return equX < x
                    && x < equX + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9
                    && equY < y
                    && y < equY + (screenHeight / 9 + screenHeight / 135) * 3 + screenHeight / 9;

        }
        return false;
    }

    /**
     * Draw items in inventory.
     * @param canvas canvas to draw
     */
    private void drawInventory(Canvas canvas){
        int ID;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 8; column++) {
                if (( ID = inventory.getInventoryItem(row, column)) != -1) {
                    itinerary.getItem(ID).draw(gridX + (screenHeight / 9 + screenHeight / 135) * column, gridY + (screenHeight / 9 + screenHeight / 135) * row, canvas);
                }
            }
        }
    }

    /**
     * Draws equipped items.
     * @param canvas canvas to draw
     */
    private void drawEquipped(Canvas canvas){
        for (ItemType type:ItemType.values()) {
            int ID = inventory.getEquipedItem(type);
            if (ID != -1){
                switch (type) {
                    case HELMET:
                        itinerary.getItem(ID).draw(equX, equY, canvas);
                        break;
                    case ARMOR:
                        itinerary.getItem(ID).draw(equX, equY + screenHeight / 9 + screenHeight / 135, canvas);
                        break;
                    case PANTS:
                        itinerary.getItem(ID).draw(equX, equY + (screenHeight / 9 + screenHeight / 135) * 2, canvas);
                        break;
                    case SHOES:
                        itinerary.getItem(ID).draw(equX, equY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case WEAPON:
                        itinerary.getItem(ID).draw(equX + screenHeight / 9 + screenHeight / 135,  equY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case SHIELD:
                        itinerary.getItem(ID).draw(equX + (screenHeight / 9 + screenHeight / 135) * 2,  equY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                    case NECKLACE:
                        itinerary.getItem(ID).draw(equX + (screenHeight / 9 + screenHeight / 135) * 3,  equY, canvas);
                        break;
                    case GLOVES:
                        itinerary.getItem(ID).draw(equX + (screenHeight / 9 + screenHeight / 135) * 3,  equY + screenHeight / 9 + screenHeight / 135, canvas);
                        break;
                    case RING:
                        itinerary.getItem(ID).draw(equX + (screenHeight / 9 + screenHeight / 135) * 3,  equY + (screenHeight / 9 + screenHeight / 135) * 2, canvas);
                        break;
                    case BELT:
                        itinerary.getItem(ID).draw(equX + (screenHeight / 9 + screenHeight / 135) * 3,  equY + (screenHeight / 9 + screenHeight / 135) * 3, canvas);
                        break;
                }
            }
        }
    }

    /**
     * Draws equipped items frame.
     * @param canvas canvas to draw
     */
    private void drawEquippedFrame(Canvas canvas){
        int ID = inventory.getEquipedItem(equSelected);
        if (ID != -1){
            switch (equSelected) {
                case HELMET:
                    canvas.drawBitmap(frame, equX - (int)(screenHeight / 135.0), equY - (int)(screenHeight / 135.0), null);
                    break;
                case ARMOR:
                    canvas.drawBitmap(frame, equX - screenHeight / 135f, equY + screenHeight / 9f , null);
                    break;
                case PANTS:
                    canvas.drawBitmap(frame, equX - screenHeight / 135f, equY + (screenHeight / 9f + screenHeight / 135f) * 2 - screenHeight / 135f, null);
                    break;
                case SHOES:
                    canvas.drawBitmap(frame, equX - screenHeight / 135f, equY + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f, null);
                    break;
                case WEAPON:
                    canvas.drawBitmap(frame, equX + screenHeight / 9f + screenHeight / 135f - screenHeight / 135f,  equY + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f, null);
                    break;
                case SHIELD:
                    canvas.drawBitmap(frame, equX + (screenHeight / 9f + screenHeight / 135f) * 2 - screenHeight / 135f,  equY + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f, null);
                    break;
                case NECKLACE:
                    canvas.drawBitmap(frame, equX + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f,  equY - screenHeight / 135f, null);
                    break;
                case GLOVES:
                    canvas.drawBitmap(frame, equX + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f,  equY + screenHeight / 9f, null);
                    break;
                case RING:
                    canvas.drawBitmap(frame, equX + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f,  equY + (screenHeight / 9f + screenHeight / 135f) * 2 - screenHeight / 135f, null);
                    break;
                case BELT:
                    canvas.drawBitmap(frame, equX + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f,  equY + (screenHeight / 9f + screenHeight / 135f) * 3 - screenHeight / 135f, null);
                    break;
            }
        }
    }

    /**
     * Writes item description.
     * @param canvas canvas to write
     * @param ID ID of item
     * @param itemType type of item
     */
    private void writeDescription(Canvas canvas, int ID, ItemType itemType){
        String string = text.getItemName(ID);
        paint.setTextSize(nameSize);
        paint.getTextBounds(string, 0, string.length(), bounds);
        int y = grid.getHeight() + 10 + bounds.height();
        int x = screenHeight/9 - bounds.left;
        canvas.drawText(string, x, y, paint);
        paint.setTextSize(textSize);
        string = text.getItemDescription(ID);
        if (string.length() <= 25) {
            paint.getTextBounds(string, 0, string.length(), bounds);
            y += bounds.height() + 5;
            x = screenHeight/9 - bounds.left;
            canvas.drawText(string, x, y, paint);
        }
        else {
            StringBuilder firstPart = new StringBuilder(), secondPart = new StringBuilder();
            String[] split = string.split("\\s+");
            boolean second = false;
            for (String word:split) {
                if (firstPart.length() + word.length() <= 30 && !second){
                    firstPart.append(" ");
                    firstPart.append(word);
                } else if (secondPart.length() + word.length() <= 30) {
                    secondPart.append(" ");
                    secondPart.append(word);
                    second = true;
                }
            }
            string = firstPart.toString();
            paint.getTextBounds(string, 0, string.length(), bounds);
            y += bounds.height() + 5;
            x = screenHeight/9 - bounds.left;
            canvas.drawText(string, x, y, paint);
            string = secondPart.toString();
            paint.getTextBounds(string, 0, string.length(), bounds);
            y += bounds.height() + 5;
            x = screenHeight/9 - bounds.left;
            canvas.drawText(string, x, y, paint);
        }
        y += 5;
        if (itemType != ItemType.OTHER){
            HashMap<String,Integer> stats = itinerary.getItem(ID).getStats();
            for (String key:stats.keySet()) {
                string = key +": " + stats.get(key);
                paint.getTextBounds(string, 0, string.length(), bounds);
                paint.setTextSize(textSize-1);
                y += bounds.height() + 5;
                x = screenHeight/9 - bounds.left;
                canvas.drawText(string, x, y, paint);
            }
        }
    }

    /**
     * Writes player
     * @param canvas
     */
    private void writePlayerStats(Canvas canvas){
        HashMap<String,Integer> stats = inventory.getStats(gameHandler);
        int y = equipped.getHeight() + 5,x;
        for (String key:stats.keySet()) {
            String string = key +": " + stats.get(key);
            paint.getTextBounds(string, 0, string.length(), bounds);
            paint.setTextSize(textSize-1);
            y += bounds.height() + 5;
            x = equDrawX + 10 - bounds.left;
            canvas.drawText(string, x, y, paint);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

}
