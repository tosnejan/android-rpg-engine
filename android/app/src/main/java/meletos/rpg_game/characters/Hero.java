package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.navigation.JoyStick;


/**
 * This is the hero class.
 */
public class Hero extends FatherCharacter {

    private JoyStick js;

    public Hero(int x, int y, String assetsFolder, Context context, boolean enemy) {
        super(x, y, assetsFolder, context, enemy, null, null);
        xSpeedConstant = 4;
        ySpeedConstant = 4;
    }

    public Hero(int x, int y, Context context, boolean enemy) {
        super(x, y, context, enemy, null);
        xSpeedConstant = 4;
        ySpeedConstant = 4;
    }

    /**
     * will be called when the arrows are touched
     * @param direction direction of joystick
     */
    private void heroicMove (Directions direction) {
        updateDirectionSpeed(direction);

        PositionInformation newPosition = new PositionInformation(
                positionInformation.mainCoord.x + xSpeed,
                positionInformation.mainCoord.y + ySpeed, imgHeigth, imgWidth
        );
        if (
                gameHandler.collisionDetector(this, newPosition ) == Directions.NONE
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
        //DEBUG PURPOSES
        /*Log.i("Hero", "Coordinates - x: " + positionInformation.mainCoord.x +
                " y: " + positionInformation.mainCoord.y
                );*/
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    protected void updateXY() {
        positionInformation.heroAddSpeed(xSpeed, ySpeed, gameHandler);
    }

    public Bitmap getImage(){
        return images.get(7);
    }
}
