package meletos.rpg_game.navigation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Menu {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap image;
    private int x;
    private int y;
    public Menu(Bitmap image) {
        this.image = image;
        x = (screenWidth/2) - (image.getWidth()/2);
        y = (screenHeight/2) - (image.getHeight()/2);
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
