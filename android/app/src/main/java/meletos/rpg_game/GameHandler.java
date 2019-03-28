package meletos.rpg_game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private Context context;
    private Bitmap map;

    public GameHandler(FatherCharacter[] characters, Context context, int[][] mapMatrix) {
        this.characters = characters;
        this.mapMatrix = mapMatrix;
        this.context = context;
        map = BitmapFactory.decodeResource(context.getResources(),R.drawable.testing_map);
        map = Bitmap.createScaledBitmap(map, 1920, 1080, true);
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public GameHandler(FatherCharacter[] characters, Context context) {
        this.characters = characters;
        this.context = context;
        map = BitmapFactory.decodeResource(context.getResources(),R.drawable.testing_map);
        map = Bitmap.createScaledBitmap(map, 1920, 1080, true);
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public void drawGame (Canvas canvas) {
        //String threadName = Thread.currentThread().getName();
        //System.out.println("This is view logic here on thread: " + threadName);
        canvas.drawBitmap(map, 0, 0, null);
        for (FatherCharacter character: characters) {
            character.draw(canvas);
        }
    }

    public void updateGame () {
        if (isGamePaused) {
            return;
        }
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
     * @param p
     */
    public Directions suggestDirections(PositionInformation p) {
        Directions yDirection = Directions.NONE;
        Directions xDirection = Directions.NONE;
        Directions finalDirection = Directions.NONE;

        if (p.upperRightCorner.x >= screenWidth) {
            xDirection = Directions.LEFT;
            finalDirection = xDirection;
        } else if (p.mainCoord.x <= 0) {
            xDirection = Directions.RIGHT;
            finalDirection = xDirection;
        }
        if (p.lowerRightCorner.y >= screenHeight) {
            yDirection = Directions.UP;
            finalDirection = yDirection;
        } else if (p.mainCoord.y <= 0) {
            yDirection = Directions.DOWN;
            finalDirection = yDirection;
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
        return finalDirection;
    }

    /**
     * Collision detector typu easy
     * @param currPosition
     * @param newPosition
     * @return
     */
    public Directions collisionDetector (PositionInformation currPosition, PositionInformation newPosition) {

        for (FatherCharacter character : characters) {
            if (currPosition.equals(character.getPositionInformation())) {
                continue; // probably is the same character
            }
            PositionInformation otherPositionInfo = character.getPositionInformation();

            Directions result = newPosition.collidesWith(otherPositionInfo);
            if (result != Directions.NONE) {
                System.out.println(result);
                return result;
            }
        }
        return Directions.NONE;
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        isGamePaused = false;
    }


}
