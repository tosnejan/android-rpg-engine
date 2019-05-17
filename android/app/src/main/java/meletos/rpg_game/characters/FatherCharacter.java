package meletos.rpg_game.characters;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.Sprite;
import meletos.rpg_game.file_io.CharacterRepresentation;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter extends Sprite implements Serializable {
    // will be used for enemy spawning
    protected boolean spawned = true;

    protected boolean enemy;
    public HashMap<String,Integer> stats;
    /**
     * A proposal -- lets use this construct for animations :D
     */
    protected  ArrayList<Bitmap> images;
    protected int idx = 0;
    protected boolean animation = false;
    protected int animationSpeed = 0;
    protected final int ANIM_SPEED = 10; // sets after how many calls to draw does the image animate
    protected Bitmap characterImage;

    protected Directions direction;
    protected int xSpeedConstant = 10;
    protected int ySpeedConstant = 10;

    /* ALWAYS CHANGE THROUGH updateDirectionSpeed() METHOD*/
    protected int xSpeed = xSpeedConstant;
    protected int ySpeed = ySpeedConstant;
    private String assetsFolder;

    // screen info
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    protected GameHandler gameHandler; // the boss
    protected Context context;
    protected String imagePath;

    public FatherCharacter(int x, int y, Bitmap image) {
        super(x, y, image);
        this.direction = Directions.UP;
    }

    /**
     * The constructor for animated character
     * @param x
     * @param y
     * @param assetsFolder
     * @param context
     * @param stats
     */
    public FatherCharacter(int x, int y, String assetsFolder, Context context, boolean enemy, String imagePath, HashMap<String, Integer> stats) {
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

    public FatherCharacter(int x, int y, Context context, boolean enemy, HashMap<String, Integer> stats) {
        super(x, y);
        this.stats = stats;
        this.direction = Directions.UP;
        this.context = context;
        this.enemy = enemy;
        animation = true;
    }

    public FatherCharacter(int x, int y) {
        super(x, y);
        animation = true;
    }

    /**
     * Should work like this: gets name of the folder containing animation images.
     * Loads them all so it can animate the character
     * @param folder folder that contains character images.
     * @param assets true if the folder is from assets, false if the folder is from external storage.
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
                e.printStackTrace();
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

    @Override
    public void draw (Canvas canvas) {
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
        canvas.drawBitmap(image, positionInformation.mainCoord.x + gameHandler.getxShift(),
                positionInformation.mainCoord.y + gameHandler.getyShift(), null
        );
    }

    /**
     * Lets the character know the gamehandler
     * Is boolean, because it is used by the hero to let the game handler know it is hero
     * @param gameHandler
     */
    public boolean setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        return false;
    }

    /**
     * Main game logic method
     */
    public void update () {
        // has to be implemented in individual characters!!
    }

    /**
     * When direction is updated, the xSpeed and ySpeed are updated with it
     * @param newDirection
     */
    public void updateDirectionSpeed (Directions newDirection) {
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
     * Method for updating x and y
     */
    protected void updateXY () {
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

    public String getAssetsFolder () {
        return assetsFolder;
    }

    public Bitmap getCharacterImage() {
        return characterImage;
    }

    public HashMap<String, Integer> getStats() {
        return stats;
    }

    public CharacterRepresentation putMyselfIntoCharRepresentation() {
        return new CharacterRepresentation(
            this.getClass().getSimpleName(), assetsFolder,
            imagePath, enemy, y, x, stats
        );
    }

}
