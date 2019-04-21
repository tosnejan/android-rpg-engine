package meletos.rpg_game.menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import meletos.rpg_game.navigation.Button;

public class HeroSelection extends Thread {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private MainMenu mainMenu;
    private HeroProperties[] heroes;
    private Button[] buttons = new Button[4];
    private Bitmap title;
    private Paint paint;
    private Rect bounds = new Rect();
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moving;
    private boolean isAlive = true;
    private int selected = -1;
    private int theOne = 0;
    private int baseWidth;
    private int baseHeight = screenHeight/4;
    private int baseX = screenWidth/4;
    private int baseY = screenHeight/3;
    private int titleWidth;
    private int titleHeight;
    private int textSize;
    private double ratio;

    public HeroSelection(HeroProperties[] heroes, MainMenu mainMenu, Bitmap title) {
        this.heroes = heroes;
        this.title = title;
        titleWidth = title.getWidth();
        titleHeight = title.getHeight();
        Bitmap image = heroes[heroes.length - 1].getImage();
        ratio = image.getWidth()/(double)image.getHeight();
        baseWidth = (int)(baseHeight*ratio);
        image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
        buttons[0] = new Button( baseX - image.getWidth()/2, baseY - image.getHeight()/2, image);
        image = heroes[0].getImage();
        image = Bitmap.createScaledBitmap(image,(int)(baseWidth * Math.log10(100)), (int)(baseHeight * Math.log10(100)), false);
        buttons[1] = new Button(baseX * 2 - image.getWidth()/2, baseY - image.getHeight()/2, image);
        image = heroes[heroes.length > 1 ? 1 : 0].getImage();
        image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
        buttons[2] = new Button(baseX * 3 - image.getWidth()/2, baseY - image.getHeight()/2, image);
        this.mainMenu = mainMenu;
        paint = new Paint();
        textSize = (int) (title.getHeight() / 1.5);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create("Arial", Typeface.ITALIC));
        paint.setTextSize(textSize);
    }

    @Override
    public void run() {
        Bitmap image;
        while (isAlive){
            if (moveLeft){
                moving = true;
                for (int i = 1; i < 9; i++) {
                    /*image = heroes[theOne - 1 < 0 ? heroes.length - 1 : theOne - 1].getImage();
                    image = Bitmap.createScaledBitmap(image, (int) (baseWidth * Math.log10(10 - i)), (int) (baseHeight * Math.log10(10 - i)), false);
                    buttons[0] = new Button((int) (baseX * Math.log10(10 - 0.75*i)) - image.getWidth() / 2, baseY - image.getHeight() / 2, image);
                    image = heroes[theOne].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(100 - 10*i)), (int)(baseHeight * Math.log10(100 - 10*i)), false);
                    buttons[1] = new Button((int)(baseX * Math.log10(100 - 10*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne + 1 < heroes.length ? theOne + 1 : 0].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(10 + 10*i)), (int)(baseHeight * Math.log10(10 + 10*i)), false);
                    buttons[2] = new Button((int)(baseX * Math.log10(1000 - 100*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne + 1 < heroes.length ? (theOne + 2 < heroes.length ? theOne + 2 : 0) : (1 == heroes.length ? 0 : 1)].getImage(); //Tfuj to je prasečárna... ale tak je to potřebal, kdyby někdo udělal story pouze s jedním hrdinou...
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(1 + i)), (int)(baseHeight * Math.log10(1 + i)), false);
                    buttons[3] = new Button((int)(baseX * Math.log10(3250 - 250*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);*/
                    image = heroes[theOne - 1 < 0 ? heroes.length - 1 : theOne - 1].getImage();
                    image = Bitmap.createScaledBitmap(image, (int) (baseWidth * Math.log10(10 - i)), (int) (baseHeight * Math.log10(10 - i)), false);
                    buttons[0] = new Button(screenWidth - (int) (baseX * Math.log10(1000 + 250*i)) - image.getWidth() / 2, baseY - image.getHeight() / 2, image);
                    image = heroes[theOne].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(100 - 10*i)), (int)(baseHeight * Math.log10(100 - 10*i)), false);
                    buttons[1] = new Button(screenWidth - (int)(baseX * Math.log10(100 + 100*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne + 1 < heroes.length ? theOne + 1 : 0].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(10 + 10*i)), (int)(baseHeight * Math.log10(10 + 10*i)), false);
                    buttons[2] = new Button(screenWidth - (int)(baseX * Math.log10(10 + 10*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne + 1 < heroes.length ? (theOne + 2 < heroes.length ? theOne + 2 : 0) : (1 == heroes.length ? 0 : 1)].getImage(); //Tfuj to je prasečárna... ale tak je to potřebal, kdyby někdo udělal story pouze s jedním hrdinou...
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(1 + i)), (int)(baseHeight * Math.log10(1 + i)), false);
                    buttons[3] = new Button(screenWidth - (int)(baseX * Math.log10(3.25 + 0.75*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                theOne = theOne + 1 < heroes.length ? theOne + 1 : 0;
                image = heroes[theOne - 1 < 0 ? heroes.length - 1 : theOne - 1].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
                buttons[0] = new Button(baseX - image.getWidth() / 2, baseY - image.getHeight() / 2, image);
                image = heroes[theOne].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth * 2, baseHeight * 2, false);
                buttons[1] = new Button(baseX * 2 - image.getWidth()/2, baseY - image.getHeight()/2, image);
                image = heroes[theOne + 1 < heroes.length ? theOne + 1 : 0].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
                buttons[2] = new Button(baseX * 3 - image.getWidth()/2, baseY - image.getHeight()/2, image);
                buttons[3] = null;
                moveLeft = false;
                moving = false;
                textSize = (int) (title.getHeight() / 1.5);
            } else if (moveRight){
                moving = true;
                for (int i = 1; i < 9; i++) {
                    image = heroes[theOne - 1 < 0 ? heroes.length - 1 : theOne - 1].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(10 + 10*i)), (int)(baseHeight * Math.log10(10 + 10*i)), false);
                    buttons[0] = new Button((int)(baseX * Math.log10(10 + 10*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(100 - 10*i)), (int)(baseHeight * Math.log10(100- 10*i)), false);
                    buttons[1] = new Button((int)(baseX * Math.log10(100 + 100*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    image = heroes[theOne + 1 < heroes.length ? theOne + 1 : 0].getImage();
                    image = Bitmap.createScaledBitmap(image, (int) (baseWidth * Math.log10(10 - i)), (int) (baseHeight * Math.log10(10 - i)), false);
                    buttons[2] = new Button((int) (baseX * Math.log10(1000 + 250*i)) - image.getWidth() / 2, baseY - image.getHeight() / 2, image);
                    image = heroes[theOne - 1 < 0 ? (heroes.length > 1 ? heroes.length - 2 : 0): (theOne - 2 < 0 ? heroes.length - 1 : theOne - 2)].getImage();
                    image = Bitmap.createScaledBitmap(image, (int)(baseWidth * Math.log10(1 + i)), (int)(baseHeight * Math.log10(1 + i)), false);
                    buttons[3] = new Button((int)(baseX * Math.log10(3.25 + 0.75*i)) - image.getWidth()/2, baseY - image.getHeight()/2, image);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                theOne = theOne - 1 < 0 ? heroes.length - 1 : theOne - 1;
                image = heroes[theOne - 1 < 0 ? heroes.length - 1 : theOne - 1].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
                buttons[0] = new Button(baseX - image.getWidth() / 2, baseY - image.getHeight() / 2, image);
                image = heroes[theOne].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth * 2, baseHeight * 2, false);
                buttons[1] = new Button(baseX * 2 - image.getWidth()/2, baseY - image.getHeight()/2, image);
                image = heroes[theOne + 1 < heroes.length ? theOne + 1 : 0].getImage();
                image = Bitmap.createScaledBitmap(image, baseWidth, baseHeight, false);
                buttons[2] = new Button(baseX * 3 - image.getWidth()/2, baseY - image.getHeight()/2, image);
                buttons[3] = null;
                moveRight = false;
                moving = false;
                textSize = (int) (title.getHeight() / 1.5);
            }
        }
    }

    public void draw(Canvas canvas){
        for (Button button : buttons) {
            if (button != null){
                button.draw(canvas);
            }
        }
        canvas.drawBitmap(title, (screenWidth - title.getWidth())/2f, 0, null);
        paint.getTextBounds(heroes[theOne].getName(), 0, heroes[theOne].getName().length(), bounds);
        if (bounds.width() > 9 * titleWidth / 10 || bounds.height() > titleHeight / 2) {
            setOptimalTextSize();
        }
        canvas.drawText(heroes[theOne].getName(),
                screenWidth/2f - bounds.width()/2f - bounds.left,
                titleHeight/4f + bounds.height()/2f - bounds.bottom, paint);
    }

    private void setOptimalTextSize(){
        while (bounds.width() > 9 * titleWidth/10 || bounds.height() > titleHeight / 2){
            textSize -= 1;
            paint.setTextSize(textSize);
            paint.getTextBounds(heroes[theOne].getName(), 0, heroes[theOne].getName().length(), bounds);
        }
    }

    public void touchDown(int x, int y){
        for (int i = 0; i < 3; i++) {
            if (buttons[i].isTouched(x, y)){
                selected = i;
                return;
            }
        }
    }

    public void touchUp(int x, int y) {
        if (selected == -1) return;
        switch (selected){
            case 0:
                if (buttons[0].isTouched(x, y)) moveRight = true;
                break;
            case 1:
                if (buttons[1].isTouched(x, y)) mainMenu.heroSelected(heroes[theOne]);
                break;
            case 2:
                if (buttons[2].isTouched(x, y)) moveLeft = true;
                break;
        }
        selected = -1;
    }

    public HeroProperties[] getHeroes() {
        return heroes;
    }

    public boolean isNotMoving() {
        return !moving;
    }

    public void kys() {
        isAlive = false;
    }
}
