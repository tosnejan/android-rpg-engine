package meletos.rpg_game.characters;

import android.content.Context;

import java.util.HashMap;

import meletos.rpg_game.Coordinates;

/**
 * This character follows points in space by taking the xpath and then the y path
 * Work in progress
 * TODO ERASE
 */
public class Follower extends FatherCharacter {
    private Coordinates[] followCoord;
    private int idx = 0; // used to choose points to follow
    private double[] followVector = new double[2];
    private Coordinates followedPoint;
    private int xSpeed, ySpeed;

    public Follower (int x, int y, String assetsFolder, Context context, Coordinates[] followCoord, boolean enemy, String battleImageFolder, HashMap<String, Integer> stats) {
        super(x, y, assetsFolder, context, enemy, battleImageFolder, stats);
        this.followCoord = followCoord;
        followedPoint = followCoord[idx];

    }

    @Override
    public void update() {
        if (positionInformation.isCoordinateInside(followedPoint)) {
            ++idx;
            if (idx == followCoord.length) {
                idx = 0;
            }
            followedPoint = followCoord[idx];
            setFollow();
        }
        if (
                positionInformation.mainCoord.x <= followedPoint.x + 10 &&
                positionInformation.mainCoord.x >= followedPoint.x - 10
        ) {
            System.out.println("Y direction");
            positionInformation.addSpeed(0, ySpeed);
            //y += ySpeed;
        } else {
            System.out.println("X direction");
            positionInformation.addSpeed(xSpeed, 0);
            //x += xSpeed;
        }
        System.out.println(positionInformation.mainCoord);
    }

    private void setFollow () {
        if (positionInformation.mainCoord.x < followedPoint.x)
            xSpeed = 5;
        else xSpeed = -5;

        if (positionInformation.mainCoord.y < followedPoint.y)
            ySpeed = 5;
        else ySpeed = -5;
    }
}
