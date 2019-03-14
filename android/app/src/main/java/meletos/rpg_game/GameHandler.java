package meletos.rpg_game;

import android.content.res.Resources;
import android.graphics.Canvas;

/**
 * Class used to check whether the characters are updating properly
 */
public class GameHandler {
    private FatherCharacter[] characters;
    private int[][] mapMatrix; // matrix of the map availability
    private final int available = 0; // constant defining whether a pixel is available
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public GameHandler(FatherCharacter[] characters, int[][] mapMatrix) {
        this.characters = characters;
        this.mapMatrix = mapMatrix;
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public void drawGame (Canvas canvas) {
        for (FatherCharacter character: characters) {
            character.draw(canvas);
        }
    }

    public void updateGame () {
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
            mapMatrix[y][x] == available &&
            x + imgWidth < screenWidth &&
            y + imgHeight < screenHeight
        ) {
            return true;
            /* mohla by pak vracet string s presnou specifikaci problemu aka "prekazka nalevo"
             nebo bychom to mohli rozdelit do vice fci mozna :D*/
        }
        return false;
    }
}
