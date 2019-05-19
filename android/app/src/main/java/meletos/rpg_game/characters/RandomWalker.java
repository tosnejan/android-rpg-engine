package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;

/**
 * Most used class for moving characters.
 * Moves in random directions
 */
public class RandomWalker extends FatherCharacter {
    private int steps;

    public RandomWalker(int x, int y, String assetsFolder, Context context, boolean enemy, String battleImageFolder, HashMap<String, Integer> stats) {
        super(x, y, assetsFolder, context, enemy, battleImageFolder, stats);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    public RandomWalker(int x, int y, Context context, boolean enemy, HashMap<String,Integer> stats) {
        super(x, y, context, enemy, stats);
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

        Directions suggestedDirection = gameHandler.collisionDetector(
                this, new PositionInformation(
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
        if (enemy){
            if (stats.get("HP") < 1000){
                int hp = stats.get("HP") + 1;
                hp = hp > 1000 ? 1000 : hp;
                stats.put("HP", hp);
            }
        }
    }
}
