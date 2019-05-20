package meletos.rpg_game.characters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.Sprite;
import meletos.rpg_game.inventory.Inventory;

/**
 * Static chest that has items. When hero comes it gives
 * all these items to him and disappears.
 */
public class Chest extends Sprite {
    private int[] items;
    private int keyID;
    private boolean heroVisited = false;
    private transient GameHandler gameHandler;

    public Chest(int x, int y, int[] items, int keyID, GameHandler gameHandler) {
        super(x, y);
        this.items = items;
        this.keyID = keyID;
    }

    /**
     * Loads image.
     * @param context mainactivity context -- needed for assets
     * @see Context
     */
    public void loadImage(Context context) {
        AssetManager am = context.getAssets();
        try {
            image = BitmapFactory.decodeStream(am.open("characters/chest.png"));
            image = Bitmap.createScaledBitmap(image, 96, 108, false);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
        positionInformation = new PositionInformation(x, y, image.getHeight(), image.getWidth());
    }

    public void setGameHandler(GameHandler gh) {
        this.gameHandler = gh;
    }

    /**
     * Opens chest -- makes it disappear and all items get copied to inventory.
     * @param inventory inventory where items get copied
     */
    public void open (Inventory inventory) {
        Log.i(this.getClass().getSimpleName(), "Opening chest.");
        if (!heroVisited && (inventory.hasItem(keyID) || keyID == -1)) {
            heroVisited = true;
            // give him items
            for (int item: items) {
                inventory.putItem(item);
            }
            disappear();
            }
    }

    /**
     * Draws chest.
     * @param canvas canvas to draw on
     * @param x coordinate of upper left corner
     * @param y coordinate of upper left corner
     */
    public void draw(Canvas canvas, int x, int y) {
        if (!heroVisited) {
            canvas.drawBitmap(image, positionInformation.mainCoord.x + x,
                    positionInformation.mainCoord.y + y, null
            );
        }
    }

    /**
     * Makes chest disappear and removes it from chests.
     */
    private void disappear() {
        gameHandler.getChests().remove(this);
    }
}
