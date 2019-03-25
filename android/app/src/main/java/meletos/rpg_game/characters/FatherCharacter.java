package meletos.rpg_game.characters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import meletos.rpg_game.Directions;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.Sprite;

/**
 * This character should be the father of all the other characters.
 * This should allow us to group them into an array of type FatherCharacter[]
 * -- all of them will have function update - implementing their own strategy
 */
public abstract class FatherCharacter extends Sprite {
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
