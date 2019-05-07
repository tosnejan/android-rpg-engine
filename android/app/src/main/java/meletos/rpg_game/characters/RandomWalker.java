package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Bitmap;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;

public class RandomWalker extends FatherCharacter {
    private int steps;
    public RandomWalker(int x, int y, Bitmap image) {
        super(x, y, image);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    public RandomWalker(int x, int y, String assetsFolder, Context context, boolean enemy) {
        super(x, y, assetsFolder, context, enemy);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    public RandomWalker(int x, int y, Context context, boolean enemy) {
        super(x, y, context, enemy);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    public RandomWalker(int x, int y) {
        super(x, y);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    @Override
    public void update() {
        ++steps;
        if (steps == 50) {
            this.updateDirectionSpeed(Directions.randomDirection());
            steps = 0;
        }

        //Directions suggestedDirection = gameHandler.suggestDirections( positionInformation);

        //if (suggestedDirection != Directions.NONE) {
            //updateDirectionSpeed(suggestedDirection);
        //}

        Directions suggestedDirection = gameHandler.collisionDetector(
                positionInformation, new PositionInformation(
                        positionInformation.mainCoord.x + xSpeed,
                        positionInformation.mainCoord.y + ySpeed,
                        imgHeigth, imgWidth
                )
        );

        if (suggestedDirection != Directions.NONE) {
            updateDirectionSpeed(suggestedDirection);
        }

        if (gameHandler.isPositionAvailable(
                positionInformation.mainCoord.x,
                positionInformation.mainCoord.y,
                imgWidth, imgHeigth)) {
            updateXY();
        }
    }
}
