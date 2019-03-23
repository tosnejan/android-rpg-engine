package meletos.rpg_game.characters;

import android.graphics.Bitmap;

import meletos.rpg_game.Directions;

/**
 * This will be the hero class -- so far it does nothing
 */
public class Hero extends FatherCharacter {


    public Hero(int x, int y, Bitmap image) {
        super(x, y, image);
    }

    /**
     * will be called when the arrows are touched
     * @param direction
     */
    public void heroicMove (Directions direction) {
        updateDirectionSpeed(direction);
        if (gameHandler.isPositionAvailable(x + xSpeed, y + ySpeed, imgWidth, imgHeigth)) {
            updateXY();
        }
    }
}
