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

import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.Sprite;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter extends Sprite implements Serializable {

    /**
     * A proposal -- lets use this construct for animations :D
     */
    protected  ArrayList<Bitmap> images;
    int idx = 0;
    private boolean animation = false;
    private int animationSpeed = 0;
    private final int ANIM_SPEED = 10; // sets after how many calls to draw does the image animate
    // lets the animation know whether the character is moving -- if not, animation stops
    protected boolean isMoving = true;

    protected Directions direction;
    protected int xSpeedConstant = 10;
    protected int ySpeedConstant = 10;

    /* ALWAYS CHANGE THROUGH updateDirectionSpeed() METHOD*/
    protected int xSpeed = xSpeedConstant;
    protected int ySpeed = ySpeedConstant;

    protected GameHandler gameHandler; // the boss

    public FatherCharacter(int x, int y, Bitmap image) {
        super(x, y, image);
        this.direction = Directions.UP;
    }

    public FatherCharacter(int x, int y, String assetsFolder, Context context) {
        super(x, y);
        this.direction = Directions.UP;
        getImages(assetsFolder, context);
        animation = true;
    }

    public FatherCharacter(int x, int y) {
        super(x, y);
        animation = true;
    }

    /**
     * Should work like this: gets name of the folder containing animation images.
     * Loads them all so it can animate the character
     * @param assetsFolder
     * @param context
     */
    private void getImages (String assetsFolder, Context context) {
        AssetManager am = context.getAssets();
        String[] files = new String[0];
        try {
            files = am.list("images");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream istr = null;
        images = new ArrayList<>();

        for (String file : files) {
            images.add(BitmapFactory.decodeFile(file));
        }
    }

    @Override
    public void draw (Canvas canvas) {
        if (animation) {
            if (idx == images.size()) {
                idx = 0;
            }
            canvas.drawBitmap(images.get(idx), positionInformation.mainCoord.x,
                    positionInformation.mainCoord.y, null
            );
            if (animationSpeed == ANIM_SPEED && isMoving) { //animates, if the character is moving
                animationSpeed = 0;
                ++idx;
            }
            ++animationSpeed;
        } else {
            super.draw(canvas);
        }
    }

    /**
     * Lets the character know the gamehandler
     * @param gameHandler
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
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = ySpeedConstant;
                break;
            case DOWNRIGHT:
                xSpeed = xSpeedConstant;
                ySpeed = ySpeedConstant;
                break;
            case UPLEFT:
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = (-1)*ySpeedConstant;
                break;
            case UPRIGHT:
                xSpeed = xSpeedConstant;
                ySpeed = (-1)*ySpeedConstant;
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
        positionInformation.addSpeed(xSpeed, ySpeed);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
