package meletos.rpg_game.characters;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter {
    protected Directions direction;
    protected Bitmap image;
    protected int x, y;

    protected int xSpeedConstant = 10;
    protected int ySpeedConstant = 10;

    /* ALWAYS CHANGE THROUGH updateDirectionSpeed() METHOD*/
    protected int xSpeed = xSpeedConstant;
    protected int ySpeed = ySpeedConstant;

    protected int imgHeigth;
    protected int imgWidth;
    protected GameHandler gameHandler; // the boss

    public FatherCharacter(int x, int y, Bitmap image) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.direction = Directions.UP;
        imgHeigth = image.getHeight();
        imgWidth = image.getWidth();
    }

    /**
     * Lets the character know the gamehandler
     * @param gameHandler
     */
    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update () {

    }

    /**
     * When direction is updated, the xSpeed and ySpeed are updated with it
     * @param newDirection
     */
    public void updateDirectionSpeed (Directions newDirection) {
        direction = newDirection;
        switch (direction) {
            case LEFT:
                xSpeed = xSpeedConstant;
                ySpeed = 0;
                System.out.println("left");
                break;
            case RIGHT:
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = 0;
                System.out.println("right");
                break;
            case UP:
                xSpeed = 0;
                ySpeed = ySpeedConstant;
                System.out.println("up");
                break;
            case DOWN:
                xSpeed = 0;
                ySpeed = (-1)*ySpeedConstant;
                System.out.println("down");
                break;
            case UPLEFT:
                xSpeed = xSpeedConstant;
                ySpeed = ySpeedConstant;
                System.out.println("upleft");
                break;
            case UPRIGHT:
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = ySpeedConstant;
                System.out.println("upright");
                break;
            case DOWNLEFT:
                xSpeed = (-1)*xSpeedConstant;
                ySpeed = (-1)*ySpeedConstant;
                System.out.println("downleft");
                break;
            case DOWNRIGHT:
                xSpeed = xSpeedConstant;
                ySpeed = (-1)*ySpeedConstant;
                System.out.println("downright");
                break;
        }
        System.out.println("xSpeed " + xSpeed +  " ySpeed " + ySpeed);
    }

    /**
     * Method for updating x and y
     */
    protected void updateXY () {
        x += xSpeed;
        y += ySpeed;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
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

    public int getImgHeigth() {
        return imgHeigth;
    }

    public int getImgWidth() {
        return imgWidth;
    }
}
