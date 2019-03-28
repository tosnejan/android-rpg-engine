package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Locale;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.R;
import meletos.rpg_game.navigation.JoyStick;


/**
 * This will be the hero class -- so far it does nothing
 */
public class Hero extends FatherCharacter {

    private JoyStick js;

    public Hero(int x, int y, String assetsFolder, Context context) {
        super(x, y, assetsFolder, context);
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    /**
     * will be called when the arrows are touched
     * @param direction
     */
    public void heroicMove (Directions direction) {
        if (animation) {
            if (idx == 3) idx = 0;
            int i = 0;
            switch (direction) {
                case NONE:
                    i = 7;
                    break;
                case UP:
                    i = idx;
                    break;
                case DOWN:
                    i = idx + 6;
                    break;
                case DOWNLEFT:
                    i = idx + 9;
                    break;
                case LEFT:
                    i = idx + 9;
                    break;
                case UPLEFT:
                    i = idx + 9;
                    break;
                case DOWNRIGHT:
                    i = idx + 3;
                    break;
                case RIGHT:
                    i = idx + 3;
                    break;
                case UPRIGHT:
                    i = idx + 3;
                    break;
            }
            image = images.get(i);
            ++idx;
        }
        /*
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
        }*/
        updateDirectionSpeed(direction);

        PositionInformation newPosition = new PositionInformation(
                positionInformation.mainCoord.x + xSpeed,
                positionInformation.mainCoord.y + ySpeed, imgHeigth, imgWidth
        );
        if (
                gameHandler.suggestDirections(newPosition) == Directions.NONE &&
                gameHandler.collisionDetector(positionInformation, newPosition ) == Directions.NONE
        ) {
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
