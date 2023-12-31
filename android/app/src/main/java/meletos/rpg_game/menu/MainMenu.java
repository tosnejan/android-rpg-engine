package meletos.rpg_game.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import meletos.rpg_game.GameView;
import meletos.rpg_game.State;
import meletos.rpg_game.file_io.FileScout;
import meletos.rpg_game.file_io.GameInitialiser;
import meletos.rpg_game.navigation.Button;
import meletos.rpg_game.navigation.MenuButton;
import meletos.rpg_game.text.Text;

/**
 * Main menu GUI and logic class.
 */
public class MainMenu {
    private final int screenWidth;
    private final int screenHeight;
    private Bitmap frame;
    private Bitmap background;
    private Bitmap buttonImage;
    private Bitmap buttonImageClicked;
    private Bitmap storyButtonImage;
    private Bitmap storyButtonImageClicked;
    private Bitmap up;
    private Bitmap down;
    private Bitmap title;
    private Bitmap field;
    private final Settings settings;
    private int frameHeight;
    private int shift = 0;
    private ArrayList<Story> stories = new ArrayList<>();
    private Story[] saves;
    private final GameView gameView;
    private final Context context;
    private MainMenuStates state = MainMenuStates.MAIN;
    private final MenuButton[] buttons = new MenuButton[4];
    private final MenuButton[] storyButtons = new MenuButton[6];
    private MenuButton backButton;
    private final Text text;
    private HeroSelection heroSelection;
    private GameInitialiser gameInitialiser;

    private int clickedButton = -1;
    private int x;
    private int y;

    public MainMenu(GameView gameView, Context context, Text text, Settings settings) {
        screenWidth = gameView.getScreenWidth();
        screenHeight = gameView.getScreenHeight();
        this.gameView = gameView;
        this.context = context;
        this.text = text;
        this.settings = settings;
        loadImages();
        createButtons();
    }

    /**
     * Loads images.
     */
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
            title = BitmapFactory.decodeStream(am.open("menu/title.png"));
            title = Bitmap.createScaledBitmap(title, (int)(screenHeight/8*(title.getWidth()/(double)title.getHeight())), screenHeight/8, true);
            field = BitmapFactory.decodeStream(am.open("menu/field.png"));
            field = Bitmap.createScaledBitmap(field, (int)(screenHeight/1.75), (int)(screenHeight/3.25), true);
            up = BitmapFactory.decodeStream(am.open("menu/slider_up.png"));
            up = Bitmap.createScaledBitmap(up, screenWidth/15, (int)(frameHeight/6.7), true);
            down = BitmapFactory.decodeStream(am.open("menu/slider_down.png"));
            down = Bitmap.createScaledBitmap(down, screenWidth/15, (int)(frameHeight/6.7), true);
            stories = new ArrayList<>(Arrays.asList(FileScout.getStories(context)));
            for (Story story : stories) {
                story.setImage(
                    Bitmap.createScaledBitmap(
                            story.getImage(), (int)(frameHeight/10.7),
                            (int)(frameHeight/10.7), true
                    )
                );
            }
            getCustomMaps();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * Draw main menu GUI on canvas.
     * @param canvas canvas to draw
     * @see Canvas
     */
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
                drawSaves(canvas);
                break;
            case STORY_SELECTION:
                canvas.drawBitmap(frame, x, y, null);
                drawStories(canvas);
                break;
            case HERO_SELECTION:
                canvas.drawBitmap(field,screenWidth - field.getWidth() - screenHeight/8f,screenHeight - field.getHeight() - screenHeight/10f,null);
                heroSelection.draw(canvas);
                backButton.draw(canvas);
                break;
        }
    }

    /**
     * Checking if buttons was clicked.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
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
                for (int i = 0; i < storyButtons.length; i++) {

                    if ((i < 3 && i < saves.length)||i > 2) {
                        if (storyButtons[i].isTouched(x, y)) {
                            clickedButton = i;
                            storyButtons[i].changeImage(true, 10);
                            break;
                        }
                    }
                }
                break;
            case STORY_SELECTION:
                for (int i = 0; i < storyButtons.length; i++) {

                    if ((i < 3 && i < stories.size())||i > 2) {
                        if (storyButtons[i].isTouched(x, y)) {
                            clickedButton = i;
                            storyButtons[i].changeImage(true, 10);
                            break;
                        }
                    }
                }
                break;
            case HERO_SELECTION:
                if (heroSelection.isNotMoving()) heroSelection.touchDown(x, y);
                if (backButton.isTouched(x, y)) {
                    backButton.changeImage(true, 10);
                    clickedButton = 1;
                }
                break;
        }
    }

    /**
     * Checking if same button was clicked and do what it should do.
     * @param x coordination where click was detected
     * @param y coordination where click was detected
     */
    public void touchUp(int x, int y) {
        switch (state) {
            case MAIN:
                if (clickedButton == -1) break;
                buttons[clickedButton].changeImage(false, 0);
                if (!buttons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton){
                    case 0://New Game
                        shift = 0;
                        state = MainMenuStates.STORY_SELECTION;
                        break;
                    case 1://Load Game
                        saves = FileScout.getSaves(context);
                        shift = 0;
                        state = MainMenuStates.LOAD;
                        break;
                    case 2://Settings
                        state = MainMenuStates.SETTINGS;
                        break;
                    case 3://Quit
                        ((Activity)context).finish();
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
                if (clickedButton == -1) break;
                storyButtons[clickedButton].changeImage(false, 0);
                if (!storyButtons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton) {
                    case 0://First save
                        loadSave(shift);
                        break;
                    case 1://Second save
                        loadSave(shift+1);
                        break;
                    case 2://Third save
                        loadSave(shift+2);
                        break;
                    case 3://Back
                        state = MainMenuStates.MAIN;
                        break;
                    case 4://UP
                        if (shift != 0) shift--;
                        break;
                    case 5://DOWN
                        if (shift < saves.length - 3) shift++;
                        break;
                }
                break;
            case STORY_SELECTION:
                if (clickedButton == -1) break;
                storyButtons[clickedButton].changeImage(false, 0);
                if (!storyButtons[clickedButton].isTouched(x, y)) clickedButton = -1;
                switch (clickedButton) {
                    case 0://First story
                        loadStory(shift);
                        break;
                    case 1://Second story
                        loadStory(shift + 1);
                        break;
                    case 2://Third story
                        loadStory(shift + 2);
                        break;
                    case 3://Back
                        state = MainMenuStates.MAIN;
                        break;
                    case 4://UP
                        if (shift != 0) shift--;
                        break;
                    case 5://DOWN
                        if (shift < stories.size() - 3) shift++;
                        break;
                }
                clickedButton = -1;
                break;
            case HERO_SELECTION:
                if (heroSelection.isNotMoving()) heroSelection.touchUp(x, y);
                if (clickedButton == 1 && backButton.isTouched(x, y)) {
                    state = MainMenuStates.STORY_SELECTION;
                    gameView.setLoadingCheck(-1);
                    heroSelection.kys();
                }
                backButton.changeImage(false, 0);
                clickedButton = -1;
                break;
        }
    }

    /**
     * Loads selected story.
     * @param shift what story
     */
    private void loadStory(int shift) {
        Log.i("SHIFT", shift+"");
        Log.i("LOADING", stories.get(shift).getPath());
        gameInitialiser = new GameInitialiser(stories.get(shift).getPath(), context);
        gameInitialiser.initialiseNewSave(stories.get(shift).isUserSave()); // makes new save
        gameInitialiser.startGameLoading(gameView.getFileManager());
        loadHeroes(stories.get(shift));
        state = MainMenuStates.HERO_SELECTION;
    }

    /**
     * Loads story from file rpg_game_data/save.
     * @param shift what save
     */
    private void loadSave(int shift) {
        gameView.getFileManager().setJob(saves[shift].getPath());
        gameView.setState(State.LOADING);
        while (!gameView.hasGameHandler()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
        }
        gameView.getGameHandler().setHero(gameView.getFileManager().loadHeroProperties());
        gameView.setState(State.MAP);
        state = MainMenuStates.MAIN;
        gameView.getGameHandler().startGame();
    }

    /**
     * Creates buttons.
     */
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
        storyButtons[4] = new MenuButton(2*screenWidth/3, buttonY, up, up, text, -1);
        storyButtons[5] = new MenuButton(2*screenWidth/3, buttonY + Yspace*2, down, down, text, -1);
        backButton = new MenuButton((screenWidth/2 - buttonImage.getWidth())/2, screenHeight - 2 * buttonImage.getHeight(), buttonImage, buttonImageClicked, text, 12);
    }

    /**
     * Starts the game level if allowed.
     * @param hero chosen role
     */
    void heroSelected(final HeroProperties hero){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(text.getText(15))
                .setMessage(text.getText(16))
                .setPositiveButton(text.getText(1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            public void run() {
                                if (!gameView.hasGameHandler()) gameView.setState(meletos.rpg_game.State.LOADING);
                                while (!gameView.hasGameHandler()) {
                                    try {
                                        sleep(5);
                                    } catch (InterruptedException e) {
                                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                                    }
                                }
                                heroSelection.kys();
                                gameView.getGameHandler().setHero(hero);
                                gameInitialiser.saveHeroProperties(hero);
                                gameView.setState(meletos.rpg_game.State.MAP);
                                state = MainMenuStates.MAIN;
                                gameView.getGameHandler().startGame();
                            }
                        }.start();

                    }
                })
                .setNegativeButton(text.getText(0), null)
                .show();
    }

    /**
     * Loads custom maps.
     */
    private void getCustomMaps(){
        File sdCard = Environment.getExternalStorageDirectory();
        File mapsDir = new File(sdCard, "rpg_game_data/CustomMaps");
        if (!mapsDir.exists()) {
            if (!mapsDir.mkdirs()){
                Log.e(this.getClass().getSimpleName(), "File directory wasn't created!");
            }
        } else if (mapsDir.list() != null) {
            for (File f : mapsDir.listFiles()) {
                if (f.isDirectory()) {
                    Bitmap icon = BitmapFactory.decodeFile(f.getAbsolutePath() + "/icon.png");
                    icon = Bitmap.createScaledBitmap(icon, (int)(frameHeight/10.7), (int)(frameHeight/10.7), true);
                    stories.add(new Story(icon, f.getName(), f.getName(), true));
                }
            }
        }
    }

    /**
     * Draw stories GUI.
     * @param canvas canvas to draw.
     */
    private void drawStories(Canvas canvas){
        for (int i = 0; i < 3 && i < stories.size(); i++) {
            Story story = stories.get(i + shift);
            storyButtons[i].draw(canvas, story.getImage(), story.getName());
        }
        storyButtons[3].draw(canvas);
        if (shift != 0) storyButtons[4].draw(canvas);
        if (shift < stories.size() - 3) storyButtons[5].draw(canvas);
    }

    /**
     * Draw load GUI.
     * @param canvas canvas to draw.
     */
    private void drawSaves(Canvas canvas){
        for (int i = 0; i < 3 && i < saves.length; i++) {
            storyButtons[i].draw(canvas, saves[i+shift].getImage(), saves[i+shift].getName());
        }
        storyButtons[3].draw(canvas);
        if (shift != 0) storyButtons[4].draw(canvas);
        if (shift < saves.length - 3) storyButtons[5].draw(canvas);
    }

    /**
     * Loads Heroes what story uses.
     * @param story chosen story
     */
    private void loadHeroes(Story story){
        String path = story.isUserSave() ? "/rpg_game_data/CustomMaps/" + story.getPath() : "lvl/" + story.getPath();
        HeroProperties[] heroes = HeroProperties.load(context, path, story.isUserSave());
        for (HeroProperties hero : heroes) {
            hero.loadImage(context, path);
        }
        heroSelection = new HeroSelection(heroes, this, title, screenWidth - field.getWidth() - screenHeight/8, screenHeight - field.getHeight() - screenHeight/10);
        heroSelection.start();
    }
}
