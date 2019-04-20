package meletos.rpg_game.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;

public class MainMenu {
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap frame;
    private Bitmap background;
    private Bitmap buttonImage;
    private Bitmap buttonImageClicked;
    private Bitmap storyButtonImage;
    private Bitmap storyButtonImageClicked;
    private Settings settings;
    //private int frameWidth;
    private int frameHeight;
    private int shift = 0;
    private ArrayList<Story> stories = new ArrayList<>();
    private HeroProperties[] heroes;
    private GameView gameView;
    private Context context;
    private MainMenuStates state = MainMenuStates.MAIN;
    private MenuButton[] buttons = new MenuButton[4];
    private MenuButton[] storyButtons = new MenuButton[4];
    private Text text;
    private HeroSelection heroSelection;

    private int clickedButton = -1;
    private int x;
    private int y;

    public MainMenu(GameView gameView, Context context, Text text, Settings settings) {
        this.gameView = gameView;
        this.context = context;
        this.text = text;
        this.settings = settings;
        loadImages();
        createButtons();
    }

    private void loadImages(){
        AssetManager am = context.getAssets();
        try {
            frame = BitmapFactory.decodeStream(am.open("menu/frame.png"));
            frame = Bitmap.createScaledBitmap(frame, (int)(screenWidth/2.75), (int)(screenHeight/1.15), true);
            int frameWidth = frame.getWidth();
            frameHeight = frame.getHeight();
            x = (screenWidth - frameWidth)/2;
            y = (screenHeight - frameHeight)/2;
            buttonImage = BitmapFactory.decodeStream(am.open("menu/button.png"));
            buttonImage = Bitmap.createScaledBitmap(buttonImage, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            buttonImageClicked = BitmapFactory.decodeStream(am.open("menu/button_clicked.png"));
            buttonImageClicked = Bitmap.createScaledBitmap(buttonImageClicked, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            storyButtonImage = BitmapFactory.decodeStream(am.open("menu/storyButton.png"));
            storyButtonImage = Bitmap.createScaledBitmap(storyButtonImage, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            storyButtonImageClicked = BitmapFactory.decodeStream(am.open("menu/storyButtonClicked.png"));
            storyButtonImageClicked = Bitmap.createScaledBitmap(storyButtonImageClicked, (int)(frameWidth/1.3), (int)(frameHeight/7.6), true);
            background = BitmapFactory.decodeStream(am.open("menu/background.png"));
            background = Bitmap.createScaledBitmap(background, screenWidth, screenHeight, true);
            Bitmap icon = BitmapFactory.decodeStream(am.open("lvl/icon.png"));
            icon = Bitmap.createScaledBitmap(icon, (int)(frameHeight/10.7), (int)(frameHeight/10.7), true);
            stories.add(new Story(icon, "#Faigled", "lvl", false));
            getCustomMaps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, 0, 0, null);
        switch (state) {
            case MAIN:
                canvas.drawBitmap(frame, x, y, null);
                for (Button button:buttons) {
                    button.draw(canvas);
                }
                break;
            case SETTINGS:
                canvas.drawBitmap(frame, x, y, null);
                settings.draw(canvas);
                break;
            case LOAD:
                canvas.drawBitmap(frame, x, y, null);
                break;
            case STORY_SELECTION://TODO scrollovací tlačítka
                canvas.drawBitmap(frame, x, y, null);
                drawStories(canvas);
                break;
            case HERO_SELECTION:
                heroSelection.draw(canvas);
                break;
        }
    }

    public void touchDown(int x, int y) {
        switch (state) {
            case MAIN:
                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].isTouched(x, y)) {
                        clickedButton = i;
                        buttons[i].changeImage(true, 10);
                    }
                }
                break;
            case SETTINGS:
                settings.touchDown(x, y);
                break;
            case LOAD:
                break;
            case STORY_SELECTION:
                for (int i = 0; i < storyButtons.length; i++) {
                    if (storyButtons[i].isTouched(x, y)) {
                        clickedButton = i;
                        storyButtons[i].changeImage(true, 10);
                    }
                }
                break;
            case HERO_SELECTION:
                if (!heroSelection.isMoving()) heroSelection.touchDown(x, y);
                break;
        }
    }

    public void touchUp(int x, int y) {
        switch (state) {
            case MAIN:
                if (clickedButton == -1) break;
                buttons[clickedButton].changeImage(false, 0);
                if (!buttons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton){
                    case 0://New Game
                        state = MainMenuStates.STORY_SELECTION;
                        break;
                    case 1://Load Game
                        // view load options
                        break;
                    case 2://Settings
                        state = MainMenuStates.SETTINGS;
                        break;
                    case 3://Quit
                        alert();
                        break;
                }
                clickedButton = -1;
                break;
            case SETTINGS:
                if (settings.touchUp(x, y)){
                    state = MainMenuStates.MAIN;
                }
                break;
            case LOAD:
                break;
            case STORY_SELECTION:
                if (clickedButton == -1) break;
                storyButtons[clickedButton].changeImage(false, 0);
                if (!storyButtons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton) {
                    case 0://First story
                        gameView.loadLevel(stories.get(shift).getPath() + "/second_lvl.json", stories.get(shift).isUserSave());
                        loadHeroes(stories.get(shift));
                        state = MainMenuStates.HERO_SELECTION;//TODO tohle zakomentovat a ty dva řádky pod tím odkomentovat pokud chceš na mapu
                        //while (!gameView.hasGameHandler()) System.out.println("loading");
                        //gameView.setState(State.MAP);
                        break;
                    case 1://Second story
                        gameView.loadLevel(stories.get(shift + 1).getPath(), stories.get(shift).isUserSave());
                        state = MainMenuStates.HERO_SELECTION;
                        //gameView.setState(State.MAP);
                        break;
                    case 2://Third story
                        gameView.loadLevel(stories.get(shift + 2).getPath(), stories.get(shift).isUserSave());
                        state = MainMenuStates.HERO_SELECTION;
                        //gameView.setState(State.MAP);
                        break;
                    case 3://Back
                        state = MainMenuStates.MAIN;
                        break;
                }
                clickedButton = -1;
                break;
            case HERO_SELECTION:
                if (!heroSelection.isMoving()) heroSelection.touchUp(x, y);
                break;
        }
    }

    private void createButtons() {
        int buttonX = (screenWidth - buttonImage.getWidth())/2;
        int buttonY = (y + frameHeight/9);
        int Yspace = (buttonImage.getHeight() + frameHeight/12);
        buttons[0] = new MenuButton(buttonX, buttonY, buttonImage, buttonImageClicked, text, 4);
        buttons[1] = new MenuButton(buttonX, buttonY + Yspace, buttonImage, buttonImageClicked, text, 5);
        buttons[2] = new MenuButton(buttonX, buttonY + Yspace*2, buttonImage, buttonImageClicked, text, 6);
        buttons[3] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 7);
        storyButtons[0] = new MenuButton(buttonX, buttonY, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[1] = new MenuButton(buttonX, buttonY + Yspace, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[2] = new MenuButton(buttonX, buttonY + Yspace*2, storyButtonImage, storyButtonImageClicked, text, -1);
        storyButtons[3] = new MenuButton(buttonX, buttonY + Yspace*3, buttonImage, buttonImageClicked, text, 12);
    }

    private void alert(){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(text.getText(2))
                .setMessage(text.getText(3))
                .setPositiveButton(text.getText(1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity)context).finish();
                    }
                })
                .setNegativeButton(text.getText(0), null)
                .show();
    }

    private void getCustomMaps(){
        File sdCard = Environment.getExternalStorageDirectory();
        /*File ourFile = new File(sdCard, "rpg_game_data");
        if (!ourFile.exists()) {
            ourFile.mkdirs();
        }*/
        File mapsDir = new File(sdCard, "rpg_game_data/CustomMaps");
        if (!mapsDir.exists()) {
            if (!mapsDir.mkdirs()){
                System.out.println("File directory wasn't created!");
            }
        } else if (mapsDir.list() != null) {
            for (File f : mapsDir.listFiles()) {
                if (f.isDirectory()) {
                    Bitmap icon = BitmapFactory.decodeFile(f.getAbsolutePath() + "icon.png");
                    icon = Bitmap.createScaledBitmap(icon, (int)(frameHeight/10.7), (int)(frameHeight/10.7), true);
                    stories.add(new Story(icon, f.getName(), "rpg_game_data/CustomMaps/"+f.getName(), true));//TODO Pak sem přidat správný název .json, který se přečte v tom souboru, který říká pořadí map.
                }
            }
        }
    }

    private void drawStories(Canvas canvas){
        for (int i = 0; i < 3 && i < stories.size(); i++) {
            Story story = stories.get(i + shift);
            storyButtons[i].draw(canvas, story.getImage(), story.getName());
        }
        storyButtons[3].draw(canvas);
    }

    private void loadHeroes(Story story){
        heroes = HeroProperties.load(context, story.getPath(), story.isUserSave());
        for (HeroProperties hero : heroes) {
            hero.loadImage(context, story.getPath());
        }
        heroSelection = new HeroSelection(heroes);
        heroSelection.start();
    }
}
