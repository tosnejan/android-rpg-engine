package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Canvas;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.navigation.JoyStick;


/**
 * This will be the hero class -- so far it does nothing
 */
public class Hero extends FatherCharacter {

    private JoyStick js;

    public Hero(int x, int y, String assetsFolder, Context context) {
        super(x, y, assetsFolder, context);
        xSpeedConstant = 4;
        ySpeedConstant = 4;
    }

    /**
     * will be called when the arrows are touched
     * @param direction
     */
    public void heroicMove (Directions direction) {
        updateDirectionSpeed(direction);

        PositionInformation newPosition = new PositionInformation(
                positionInformation.mainCoord.x + xSpeed,
                positionInformation.mainCoord.y + ySpeed, imgHeigth, imgWidth
        );
        if (
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

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    public void updateXY() {
        positionInformation.heroAddSpeed(xSpeed, ySpeed, gameHandler);
        /*
        if (gameHandler.getxShift() == 0 && gameHandler.getyShift() == 0){
            if
            positionInformation.addSpeed(xSpeed, ySpeed, gameHandler);
        } else if (gameHandler.getxShift() + gameHandler.getMapWidth() == gameHandler.getMapWidth() &&
                gameHandler.getyShift()+ gameHandler.getMapHeight() == gameHandler.getMapHeight()) {

        }*/

    }
}
