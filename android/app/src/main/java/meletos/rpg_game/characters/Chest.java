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

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.PositionInformation;
import meletos.rpg_game.Sprite;
import meletos.rpg_game.inventory.Inventory;

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

    public void open (Inventory inventory) {
        System.out.println("OPENING");
        if (!heroVisited && (inventory.hasItem(keyID) || keyID == -1)) {
            heroVisited = true;
            // give him items
            for (int item: items) {
                inventory.putItem(item);
            }
            disappear();
            }
    }

    public void draw(Canvas canvas, int x, int y) {
        if (!heroVisited) {
            canvas.drawBitmap(image, positionInformation.mainCoord.x + x,
                    positionInformation.mainCoord.y + y, null
            );
        }
    }

    private void disappear() {
        x = -100;
        y = -100;
        positionInformation = new PositionInformation(-100, -100, 1, 1);
    }
}
