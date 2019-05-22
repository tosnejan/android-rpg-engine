package meletos.rpg_game.characters;

import android.content.Context;

import java.util.HashMap;

import meletos.rpg_game.Directions;
import meletos.rpg_game.PositionInformation;

/**
 * Most used class for moving characters.
 * Moves in random directions
 */
public class RandomWalker extends FatherCharacter {
    private int steps;
    private PositionInformation wantedPosition;

    public RandomWalker(int x, int y, String assetsFolder, Context context, boolean enemy, String battleImageFolder, HashMap<String, Integer> stats) {
        super(x, y, assetsFolder, context, enemy, battleImageFolder, stats);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
        wantedPosition = new PositionInformation(x, y, imgHeigth, imgWidth);
    }

    public RandomWalker(int x, int y, Context context, boolean enemy, HashMap<String,Integer> stats) {
        super(x, y, context, enemy, stats);
        steps = 0;
        xSpeedConstant = 5;
        ySpeedConstant = 5;
    }

    /**
     * Update strategy - moves in random directions.
     */
    @Override
    public void update() {
        ++steps;
        if (steps == 50) {
            this.updateDirectionSpeed(Directions.randomDirection());
            steps = 0;
        }

        wantedPosition.updatePositionInformation(positionInformation.mainCoord.x + xSpeed, positionInformation.mainCoord.y + ySpeed);
        Directions suggestedDirection = gameHandler.collisionDetector(
                this, wantedPosition
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
