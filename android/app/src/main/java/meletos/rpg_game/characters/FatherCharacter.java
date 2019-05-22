package meletos.rpg_game.characters;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.Sprite;
import meletos.rpg_game.dialog.DialogSwitcher;
import meletos.rpg_game.file_io.CharacterRepresentation;

/**
 * This character is the father of all the other characters.
 */
public abstract class FatherCharacter extends Sprite {
    // will be used for enemy spawning
    private final boolean spawned = true;

    boolean enemy;
    HashMap<String,Integer> stats;

    // animations
    transient ArrayList<Bitmap> images;
    private int idx = 0;
    private boolean animation = false;
    private int animationSpeed = 0;
    private final int ANIM_SPEED = 10; // sets after how many calls to draw does the image animate
    transient Bitmap characterImage;

    private Directions direction;
    int xSpeedConstant = 10;
    int ySpeedConstant = 10;

    /* ALWAYS CHANGE THROUGH updateDirectionSpeed() METHOD*/
    int xSpeed = xSpeedConstant;
    int ySpeed = ySpeedConstant;
    String assetsFolder;

    // screen info
    final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    transient GameHandler gameHandler; // the boss
    Context context;
    private String imagePath;

    // dialogs
    int[][] dialogs;
    int actualDialog;
    boolean played;
    DialogSwitcher[] dialogSwitchers;

    FatherCharacter(int x, int y, Bitmap image) {
        super(x, y, image);
        this.direction = Directions.UP;
    }

    FatherCharacter(int x, int y, String assetsFolder, Context context, boolean enemy, String imagePath, HashMap<String, Integer> stats) {
        super(x, y);
        this.stats = stats;
        this.direction = Directions.UP;
        this.assetsFolder = assetsFolder;
        this.context = context;
        this.enemy = enemy;
        this.imagePath = imagePath;
        getImages(assetsFolder, true, imagePath);
        animation = true;
    }

    FatherCharacter(int x, int y, Context context, boolean enemy, HashMap<String, Integer> stats) {
        super(x, y);
        this.stats = stats;
        this.direction = Directions.UP;
        this.context = context;
        this.enemy = enemy;
        animation = true;
    }

    FatherCharacter(int x, int y, Context context, boolean enemy) {
        super(x, y);
        this.context = context;
        this.enemy = enemy;
        animation = false;
    }

    /**
     * Gets name of the folder containing animation images.
     * Loads them all so it can animate the character
     * @param folder folder that contains character images.
     * @param assets true if the folder is from assets, false if the folder is from external storage.
     * @param imagePath path for battle image.
     */
    public void getImages (String folder, boolean assets, String imagePath) {
        images = new ArrayList<>();
        Bitmap temp;
        if (assets) {
            AssetManager am = context.getAssets();
            try {
                for (int i = 1; i < 13; i++) {
                    String fileName = String.format(Locale.US, "%s/%s.png", folder, i);
                    temp = BitmapFactory.decodeStream(am.open(fileName));
                    images.add(Bitmap.createScaledBitmap(temp, 96, 108, false));
                }
                if (imagePath != null){
                    temp = BitmapFactory.decodeStream(am.open(imagePath));
                    double ratio = temp.getHeight()/(double)temp.getWidth();
                    int w, h;
                    if (ratio >= 1){
                        h = screenHeight - 50;
                        w = (int) (h / ratio);
                    } else {
                        w = screenWidth / 4;
                        h = (int) (w * ratio);
                    }
                    characterImage = Bitmap.createScaledBitmap(temp, w, h, false);
                }
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        } else {
            File file = Environment.getExternalStorageDirectory();
            for (int i = 1; i < 13; i++) {
                String fileName = String.format(Locale.US, "%s/%s.png", folder, i);
                temp = BitmapFactory.decodeFile(file.getAbsolutePath() + fileName);
                images.add(Bitmap.createScaledBitmap(temp, 96, 108, false));
            }
            if (imagePath != null){
                temp = BitmapFactory.decodeFile(file.getAbsolutePath() + imagePath);
                double ratio = temp.getHeight()/(double)temp.getWidth();
                int w, h;
                if (ratio >= 1){
                    h = screenHeight - 50;
                    w = (int) (h / ratio);
                } else {
                    w = screenWidth / 4;
                    h = (int) (w * ratio);
                }
                characterImage = Bitmap.createScaledBitmap(temp, w, h, false);
            }
        }
        image = images.get(7);
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
        positionInformation = new PositionInformation(x, y, image.getHeight(), image.getWidth());
    }

    /**
     * Draws character.
     * @param canvas to draw on
     * @param x coord
     * @param y coord
     */
    public void draw (Canvas canvas, int x, int y) {
        if (animation && !gameHandler.isGamePaused() && spawned) {
            if (animationSpeed == ANIM_SPEED) {
                animationSpeed = 0;
                int i;
                ++idx;
                if (idx == 4) idx = 0;
                if (idx == 3) i = -2;
                else i = 0;
                switch (direction) {
                    case NONE:
                        i = 7;
                        idx = 0;
                        break;
                    case UP:
                        i += idx;
                        break;
                    case DOWN:
                        i += idx + 6;
                        break;
                    case DOWNLEFT:
                        i += idx + 9;
                        break;
                    case LEFT:
                        i += idx + 9;
                        break;
                    case UPLEFT:
                        i += idx + 9;
                        break;
                    case DOWNRIGHT:
                        i += idx + 3;
                        break;
                    case RIGHT:
                        i += idx + 3;
                        break;
                    case UPRIGHT:
                        i += idx + 3;
                        break;
                }
                image = images.get(i);
            } else {
                ++animationSpeed;
            }
        }
        canvas.drawBitmap(image, positionInformation.mainCoord.x + x,
                positionInformation.mainCoord.y + y, null
        );
    }

    /**
     * Lets the character know the gamehandler
     * Is boolean, because it is used by the hero to let the game handler know it is hero
     * @param gameHandler to know
     */
    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    /**
     * Main game logic method
     */
    public void update () {
        // has to be implemented in individual characters!!
    }

    /**
     * When direction is updated, the xSpeed and ySpeed are updated with it
     * @param newDirection to update
     */
    void updateDirectionSpeed(Directions newDirection) {
        direction = newDirection;
        switch (direction) {
            case LEFT:
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = 0;
                break;
            case RIGHT:
                xSpeed = xSpeedConstant;
                ySpeed = 0;
                break;
            case DOWN:
                xSpeed = 0;
                ySpeed = ySpeedConstant;
                break;
            case UP:
                xSpeed = 0;
                ySpeed = (-1)*ySpeedConstant;
                break;
            case DOWNLEFT:
                xSpeed = (-3)*xSpeedConstant/4;
                ySpeed = 3*ySpeedConstant/4;
                break;
            case DOWNRIGHT:
                xSpeed = 3*xSpeedConstant/4;
                ySpeed = 3*ySpeedConstant/4;
                break;
            case UPLEFT:
                xSpeed = (-3)*xSpeedConstant/4;
                ySpeed = (-3)*ySpeedConstant/4;
                break;
            case UPRIGHT:
                xSpeed = 3*xSpeedConstant/4;
                ySpeed = (-3)*ySpeedConstant/4;
                break;
            case NONE:
                xSpeed = 0;
                ySpeed = 0;
                break;
        }
    }

    /**
     * Method for updating x and y.
     */
    void updateXY() {
        positionInformation.addSpeed(xSpeed, ySpeed, gameHandler);
    }

    public int getX() {
        return positionInformation.mainCoord.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return positionInformation.mainCoord.y;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getCharacterImage() {
        return characterImage;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    /**
     * Used for JSON serialisation
     * @return CharacterRepresentation to be saved into JSON
     */
    public CharacterRepresentation putMyselfIntoCharRepresentation() {
        return new CharacterRepresentation(
            this.getClass().getSimpleName(), assetsFolder, imagePath,
        enemy, positionInformation.mainCoord.y, positionInformation.mainCoord.x, stats,
                dialogs, actualDialog,  played,  dialogSwitchers
        );
    }

    public int[] getDialog(){
        return dialogs[actualDialog];
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean isPlayed() {
        return played;
    }

    @Override
    public void recycleBitmaps() {
        if (images != null)
            for (Bitmap image : images) image.recycle();
        if (image != null) image.recycle();
    }
}
