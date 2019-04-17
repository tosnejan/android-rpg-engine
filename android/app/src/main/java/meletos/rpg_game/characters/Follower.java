package meletos.rpg_game.characters;

import android.content.Context;
import meletos.rpg_game.Coordinates;

/**
 * This character follows points in space by taking the xpath and then the y path
 * Work in progress
 */
public class Follower extends FatherCharacter {
    private Coordinates[] followCoord;
    private int idx = 0; // used to choose points to follow
    private double[] followVector = new double[2];
    Coordinates followedPoint;
    private int xSpeed, ySpeed;

    public Follower (int x, int y, String assetsFolder, Context context, Coordinates[] followCoord) {
        super(x, y, assetsFolder, context);
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
            y += ySpeed;
        } else {
            x += xSpeed;
        }
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
