package meletos.rpg_game.menu;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.IOException;

import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.navigation.Slider;
import meletos.rpg_game.navigation.xSlider;
import meletos.rpg_game.sound.Sound;
import meletos.rpg_game.text.Language;
import meletos.rpg_game.text.Text;

public class Settings {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Text text;
    private Sound sound;
    private Context context;
    private SharedPreferences settings;
    private Bitmap frame;
    private Bitmap buttonImage;
    private Bitmap buttonImageClicked;
    private Bitmap sliderImage;
    private Bitmap slider_thing;
    private xSlider slider;
    private int frameWidth;
    private int frameHeight;
    private int x;
    private int y;
    private MenuButton[] buttons = new MenuButton[3];
    private int clickedButton;


    public Settings(Text text, Sound sound, Context context) {
        this.text = text;
        this.sound = sound;
        this.context = context;
        settings = context.getSharedPreferences("settings", 0);
        load();
        loadImages();
        createButtons();
        slider.setValue(sound.getVolume());
    }

    public void load() {
        text.setLang(Language.getLanguage(settings.getInt("language", 0)));
        sound.setVolume(settings.getInt("volume", 5));
    }

    private void loadImages() {
        AssetManager am = context.getAssets();
        try {
            frame = BitmapFactory.decodeStream(am.open("menu/frame.png"));
            frame = Bitmap.createScaledBitmap(frame, (int)(screenWidth/2.75), (int)(screenHeight/1.15), true);
            frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();
            x = (screenWidth - frameWidth)/2;
            y = (screenHeight - frameHeight)/2;
            buttonImage = BitmapFactory.decodeStream(am.open("menu/button.png"));
            buttonImage = Bitmap.createScaledBitmap(buttonImage, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            buttonImageClicked = BitmapFactory.decodeStream(am.open("menu/button_clicked.png"));
            buttonImageClicked = Bitmap.createScaledBitmap(buttonImageClicked, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            sliderImage = BitmapFactory.decodeStream(am.open("menu/slider.png"));
            sliderImage = Bitmap.createScaledBitmap(sliderImage, (int)(6*frameWidth/10), (int)(frameHeight/18), true);
            slider_thing = BitmapFactory.decodeStream(am.open("menu/slider_thing.png"));
            slider_thing = Bitmap.createScaledBitmap(slider_thing, (int)(frameWidth/10), (int)(frameHeight/8), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createButtons(){
        int buttonX = (screenWidth - buttonImage.getWidth())/2;
        int buttonY = (y + frameHeight/9);
        int Yspace = (buttonImage.getHeight() + frameHeight/12);
        buttons[2] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 12);
        buttonImage = Bitmap.createScaledBitmap(buttonImage, (int)(frameWidth/3), (int)(frameHeight/7.6), true);
        buttonImageClicked = Bitmap.createScaledBitmap(buttonImageClicked, (int)(frameWidth/3), (int)(frameHeight/7.6), true);
        buttons[0] = new MenuButton(x + frameWidth/8, buttonY, buttonImage, buttonImageClicked, text, 10);
        buttons[1] = new MenuButton(x + frameWidth - buttonImage.getWidth() - frameWidth/8, buttonY, buttonImage, buttonImageClicked, text, 11);
        if (text.getLang() == Language.ENG){
            buttons[0].changeImage(true, 0);
        } else { buttons[1].changeImage(true, 0); }
        slider = new xSlider(x + 2*frameWidth/10,buttonY + Yspace,sliderImage,slider_thing,text,13,10);
    }

    public void draw(Canvas canvas) {
        for (Button button:buttons) {
            button.draw(canvas);
        }
        slider.draw(canvas);
    }

    public void touchDown(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isTouched(x, y)) {
                clickedButton = i;
                buttons[i].changeImage(true, 10);
            }
        }
        if (slider.isTouched(x,y)) sound.setVolume(slider.getValue());
    }

    public boolean touchUp(int x, int y) {
        boolean ret = false;
        if (clickedButton != -1) {
            buttons[2].changeImage(false, 10);
            if (!buttons[clickedButton].isTouched(x, y)) clickedButton = -1;
            switch (clickedButton) {
                case 0:
                    buttons[0].changeImage(true, 0);
                    buttons[1].changeImage(false, 0);
                    text.changeLang(Language.ENG);
                    clickedButton = -1;
                    break;
                case 1:
                    buttons[0].changeImage(false, 0);
                    buttons[1].changeImage(true, 0);
                    text.changeLang(Language.CZE);
                    clickedButton = -1;
                    break;
                case 2:
                    ret = true;
                    clickedButton = -1;
                    break;
            }
        }
        return ret;
    }
}
