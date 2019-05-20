package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.navigation.JoyStick;


/**
 * This will be the hero class -- so far it does nothing
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
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
    }

    @Override
    public void updateXY() {
        positionInformation.heroAddSpeed(xSpeed, ySpeed, gameHandler);
    }

    public Bitmap getImage(){
        return images.get(7);
    }
}
