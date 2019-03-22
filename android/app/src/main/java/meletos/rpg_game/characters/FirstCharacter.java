package meletos.rpg_game.characters;

import android.graphics.Bitmap;

import meletos.rpg_game.Directions;


public class FirstCharacter extends FatherCharacter {
    private int steps = 0;
    protected int xSpeed = 0;
    protected int ySpeed = 10;
    private Directions directions = Directions.DOWN;

    public FirstCharacter(int x, int y, Bitmap image) {
        super(x, y, image);
    }

    public void update() {
        //while (!gameHandler.isPositionAvailable(x+xSpeed, y + ySpeed, imgWidth, imgHeigth )) {
        //    switchDirections();
        //}
        if (steps == 10) {
            steps = 0;
            switchDirections();
        }
        x += xSpeed;
        y += ySpeed;
        ++steps;
        System.out.println(steps);
    }

    private void switchDirections () { // goes in a rectangle
        switch(directions) {
            case DOWN:
                directions = Directions.RIGHT;
                xSpeed = 10;
                ySpeed = 0;
                break;
            case UP:
                directions = Directions.LEFT;
                xSpeed = -10;
                ySpeed = 0;
                break;
            case RIGHT:
                directions = Directions.UP;
                xSpeed = 0;
                ySpeed = 10;
            case LEFT:
                directions = Directions.DOWN;
                xSpeed = 0;
                ySpeed = -10;
        }
    }
}
