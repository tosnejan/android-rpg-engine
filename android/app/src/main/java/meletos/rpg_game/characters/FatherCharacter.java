package meletos.rpg_game.characters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.Sprite;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter extends Sprite implements Serializable {
    // will be used for enemy spawning
    protected boolean spawned = true;
    /**
     * A proposal -- lets use this construct for animations :D
     */
    protected  ArrayList<Bitmap> images;
    protected int idx = 0;
    protected boolean animation = false;
    protected int animationSpeed = 0;
    protected final int ANIM_SPEED = 10; // sets after how many calls to draw does the image animate

    protected Directions direction;
    protected int xSpeedConstant = 10;
    protected int ySpeedConstant = 10;

    /* ALWAYS CHANGE THROUGH updateDirectionSpeed() METHOD*/
    protected int xSpeed = xSpeedConstant;
    protected int ySpeed = ySpeedConstant;
    private String assetsFolder;

    protected GameHandler gameHandler; // the boss
    protected Context context;

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
     */
    public FatherCharacter(int x, int y, String assetsFolder, Context context) {
        super(x, y);
        this.direction = Directions.UP;
        this.assetsFolder = assetsFolder;
        this.context = context;
        getImages(assetsFolder, true);
        animation = true;
    }

    public FatherCharacter(int x, int y, Context context) {
        super(x, y);
        this.direction = Directions.UP;
        this.context = context;
        animation = true;
    }

    public FatherCharacter(int x, int y) {
        super(x, y);
        animation = true;
    }

    /**
     * Should work like this: gets name of the folder containing animation images.
     * Loads them all so it can animate the character
     * @param folder
     */
    public void getImages (String folder, boolean assets) {
        if (assets) {
            AssetManager am = context.getAssets();
        /*String[] files = new String[0];
        try {
            files = am.list(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
            images = new ArrayList<>();
            Bitmap temp;

            for (int i = 1; i < 13; i++) {
                String fileName = String.format(Locale.US, "%s/%s.png", folder, i);
                try {
                    temp = BitmapFactory.decodeStream(am.open(fileName));
                    images.add(Bitmap.createScaledBitmap(temp, 96, 108, true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {

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

    public void setY(int y) {
        this.y = y;
    }

    public String getAssetsFolder () {
        return assetsFolder;
    }
}
