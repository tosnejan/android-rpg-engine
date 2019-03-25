package meletos.rpg_game.characters;

import android.graphics.Bitmap;

import meletos.rpg_game.Directions;
import meletos.rpg_game.navigation.JoyStick;

/**
 * This will be the hero class -- so far it does nothing
 */
public class Hero extends FatherCharacter {

    JoyStick js;

    public Hero(int x, int y, Bitmap image) {
        super(x, y, image);
        xSpeedConstant = 5;
        ySpeedConstant = 5;
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

    public void setJoystick (JoyStick joystick) {
        js = joystick;
    }


    @Override
    public void update(){
        heroicMove(js.getDirection());
    }
}
