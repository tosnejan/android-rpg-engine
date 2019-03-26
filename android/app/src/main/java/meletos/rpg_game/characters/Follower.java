package meletos.rpg_game.characters;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * This character follows points in space by taking the xpath and then the y path
 */
public class Follower extends FatherCharacter {
    private int followX = 0;
    private int followY = 0;
    private int[] followVector = new int[2];

    public Follower(int x, int y, Bitmap image) {
        super(x, y, image);
        followVector[0] = followX - this.x;
        followVector[1] = followY - this.y;
    }

    public Follower (int x, int y, String assetsFolder, Context context) {
        super(x, y, assetsFolder, context);
        followVector[0] = followX - this.x;
        followVector[1] = followY - this.y;

    }

    private void followPoint(int x, int y) {
        this.followX = x;
        this.followY = y;
        followVector[0] = followX - this.x;
        followVector[1] = followY - this.y;
    }

    @Override
    public void update() {

        if (x < followX + 10 && x > followX - 10) {
            if (y < followY + 10 && y > followY - 10) {
                return;
            }
            y += followVector[1]/200.0;
        } else {
            x += followVector[0]/200.0;
        }
    }
}
