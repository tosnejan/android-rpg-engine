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
            followVector[0] = followedPoint.x - positionInformation.mainCoord.x;
            followVector[1] = followedPoint.y - positionInformation.mainCoord.y;
            followVector = normaliseFollowVector(followVector);
        }
        positionInformation.addSpeed((int)Math.round(followVector[0]), (int)Math.round(followVector[0]), gameHandler);
    }

    private double[] normaliseFollowVector (double[] followVector) {
        double vectorLength = Math.sqrt(Math.pow(followVector[0], 2) + Math.pow(followVector[1], 2));
        followVector[0] = followVector[0]*xSpeed/vectorLength;
        followVector[1] = followVector[1]*ySpeed/vectorLength;
        return followVector;
    }
}
