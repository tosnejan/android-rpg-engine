package meletos.rpg_game;

import android.content.res.Resources;
import android.graphics.Canvas;

import java.io.Serializable;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * Class used to check whether the characters are updating properly
 */
public class GameHandler implements Serializable {
    private FatherCharacter[] characters;
    private int[][] mapMatrix; // matrix of the map availability
    private final int available = 0; // constant defining whether a pixel is available
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isGamePaused = false;

    public GameHandler(FatherCharacter[] characters, int[][] mapMatrix) {
        this.characters = characters;
        this.mapMatrix = mapMatrix;
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public GameHandler(FatherCharacter[] characters) {
        this.characters = characters;
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public void drawGame (Canvas canvas) {
        //String threadName = Thread.currentThread().getName();
        //System.out.println("This is view logic here on thread: " + threadName);
        for (FatherCharacter character: characters) {
            character.draw(canvas);
        }
    }

    public void updateGame () {
        if (isGamePaused) {
            return;
        }
        //String threadName = Thread.currentThread().getName();
        //System.out.println("This is game logic here on thread: " + threadName);
        for (FatherCharacter character: characters) {
            character.update();
        }
    }

    /**
     * Checks whether the position is available -- a sketch
     * @param x
     * @param y
     * @param imgWidth
     * @param imgHeight
     * @return
     */
    public boolean isPositionAvailable (int x, int y, int imgWidth, int imgHeight) {
        if (
            /*mapMatrix[y][x] == available &&*/
            x + imgWidth < screenWidth &&
            y + imgHeight < screenHeight &&
            x > 0 && y > 0
        ) {
            return true;
            /* mohla by pak vracet string s presnou specifikaci problemu aka "prekazka nalevo"
             nebo bychom to mohli rozdelit do vice fci mozna :D*/
        }
        return false;
    }

    /**
     * Sets character on a new course after it hits some wall
     * -- is quite ugly at the moment :D
     * @param character
     */
    public void setDirections(FatherCharacter character) {
        int x = character.getX();
        int y = character.getY();
        int imgHeight = character.getImgHeight();
        int imgWidth = character.getImgWidth();

        Directions yDirection = null;
        Directions xDirection = null;
        Directions finalDirection = Directions.UP;

        if (x + imgWidth >= screenWidth) {
            xDirection = Directions.RIGHT;
            finalDirection = xDirection;
            x = screenWidth - imgWidth;
        } else if (x <= 0) {
            xDirection = Directions.LEFT;
            finalDirection = xDirection;
            x = 0;
        }
        if (y + imgHeight >= screenHeight) {
            yDirection = Directions.UP;
            finalDirection = yDirection;
            y = screenHeight - imgHeight;
        } else if (y <= 0) {
            yDirection = Directions.DOWN;
            finalDirection = yDirection;
            y = 0;
        }


        if (xDirection == Directions.RIGHT && yDirection == Directions.UP) {
            finalDirection = Directions.UPRIGHT;
        } else if (xDirection == Directions.RIGHT && yDirection == Directions.DOWN) {
            finalDirection = Directions.DOWNRIGHT;
        } else if (xDirection == Directions.LEFT && yDirection == Directions.UP) {
            finalDirection = Directions.UPLEFT;
        } else if (xDirection == Directions.LEFT && yDirection == Directions.DOWN) {
            finalDirection = Directions.DOWNLEFT;
        }
        character.updateDirectionSpeed(finalDirection);
        character.setX(x);
        character.setY(y);
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        isGamePaused = false;
    }


}
