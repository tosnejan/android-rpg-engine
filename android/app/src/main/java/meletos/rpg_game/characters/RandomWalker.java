package meletos.rpg_game.characters;

import android.graphics.Bitmap;

import meletos.rpg_game.Directions;

public class RandomWalker extends FatherCharacter {
    private int steps;
    public RandomWalker(int x, int y, Bitmap image) {
        super(x, y, image);
        steps = 0;
    }

    @Override
    public void update() {
        ++steps;
        if (steps == 50) {
            this.updateDirectionSpeed(Directions.randomDirection());
            steps = 0;
        }

        if (!gameHandler.isPositionAvailable(x + xSpeed, y + ySpeed, imgWidth, imgHeigth)) {
            gameHandler.setDirections(this);
        }

        updateXY();
    }
}
