package meletos.rpg_game.navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import meletos.rpg_game.R;

/**
 * not ready at all :D -- wont be needed at all :D
 */
public class NavigationArrows  implements ButtonContainer {
    private int x, y;
    private Button arrowUP, arrowDOWN, arrowLEFT, arrowRIGHT;

    public NavigationArrows (View view) {
        Bitmap image = BitmapFactory.decodeResource(view.getResources(), R.drawable.coin);
        arrowUP = new Button(113, 696, image);
        arrowDOWN = new Button(350 ,900, image);
        arrowLEFT = new Button(113, 900, image);
        arrowRIGHT = new Button(660, 900,image);

    }

    public void areButtonsTouched(int x, int y) {
        arrowRIGHT.isTouched(x, y);
        arrowDOWN.isTouched(x, y);
        arrowUP.isTouched(x, y);
        arrowLEFT.isTouched(x, y);
    }

    public void draw (Canvas canvas) {
        arrowRIGHT.draw(canvas);
        arrowDOWN.draw(canvas);
        arrowUP.draw(canvas);
        arrowLEFT.draw(canvas);
    }
}
