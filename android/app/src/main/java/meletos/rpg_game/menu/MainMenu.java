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

public class MainMenu {
    private int screenWidth;
    private int screenHeight;
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
    private Settings settings;
    private int frameHeight;
    private int shift = 0;
    private ArrayList<Story> stories = new ArrayList<>();
    private GameView gameView;
    private Context context;
    private MainMenuStates state = MainMenuStates.MAIN;
    private MenuButton[] buttons = new MenuButton[4];
    private MenuButton[] storyButtons = new MenuButton[6];
    private MenuButton backButton;
    private Text text;
    private HeroSelection heroSelection;
    private GameInitialiser gameInitialiser;

    private int clickedButton = -1;
    private int x;
    private int y;

    public MainMenu(GameView gameView, Context context, Text text, Settings settings) {
        screenWidth = gameView.getScreenWidth(); // tyhle veci by pak nemel potrebovat -- jsou v gameHandlerovi
        screenHeight = gameView.getScreenHeight();
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
            title = BitmapFactory.decodeStream(am.open("menu/title.png"));
            title = Bitmap.createScaledBitmap(title, (int)(screenHeight/8*(title.getWidth()/(double)title.getHeight())), screenHeight/8, true);
            field = BitmapFactory.decodeStream(am.open("menu/field.png"));
            field = Bitmap.createScaledBitmap(field, (int)(screenHeight/1.75), (int)(screenHeight/3.25), true);
            up = BitmapFactory.decodeStream(am.open("menu/slider_up.png"));
            up = Bitmap.createScaledBitmap(up, (int)(screenWidth/15), (int)(frameHeight/6.7), true);
            down = BitmapFactory.decodeStream(am.open("menu/slider_down.png"));
            down = Bitmap.createScaledBitmap(down, (int)(screenWidth/15), (int)(frameHeight/6.7), true);
            //Bitmap icon = BitmapFactory.decodeStream(am.open("lvl/icon.png"));
            //icon = Bitmap.createScaledBitmap(icon, (int)(frameHeight/10.7), (int)(frameHeight/10.7), true);

            //stories.add(new Story(icon, "#Faigled", "lvl", false));
            stories = new ArrayList<Story>(Arrays.asList(FileScout.getStories(context)));
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

                    if ((i < 3 && i < stories.size())||i > 2) {
                        if (storyButtons[i].isTouched(x, y)) {
                            clickedButton = i;
                            storyButtons[i].changeImage(true, 10);
                            break;
                        }
                    }
                }
                /*if (storyButtons[storyButtons.length-1].isTouched(x, y)){
                    clickedButton = storyButtons.length-1;
                    storyButtons[storyButtons.length-1].changeImage(true, 10);
                }*/
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
                        // EXPERIMENT -- DOES WORK ONLY FOR A SPECIFIC SAVE :DD
                        // uz to umit najit vsechny savy a vyvolat jeden z nich :D
                        // potrebovali bychom, aby sel save smazat -- tlacitko v GUI
                        Story[] saves = FileScout.getSaves(context);
                        System.out.println("Number of stories" + saves.length);

                        gameView.getFileManager().setJob(saves[0].getPath());
                        // extract this into a function
                        gameView.setState(State.LOADING);
                        while (!gameView.hasGameHandler()) {
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //heroSelection.kys();
                        //gameView.getGameHandler().setHero(hero);
                        //gameInitialiser.saveHeroProperties(hero);
                        gameView.getGameHandler().setHero(gameView.getFileManager().loadHeroProperties());
                        gameView.setState(State.MAP);
                        //gameView.sound.play(State.MAP);
                        state = MainMenuStates.MAIN;
                        gameView.getGameHandler().startGame();

                        //----END OF EXPERIMENT
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
                break;
            case STORY_SELECTION:
                if (clickedButton == -1) break;
                storyButtons[clickedButton].changeImage(false, 0);
                if (!storyButtons[clickedButton].isTouched(x, y)) clickedButton = -1;
                String[] lvls = FileScout.getAllStoryLocations(context); // finds all the level folders
                switch (clickedButton) {
                    case 0://First story
                        gameInitialiser = new GameInitialiser(lvls[shift], context);
                        gameInitialiser.initialiseNewSave(); // makes new save
                        gameInitialiser.startGameLoading(gameView.getFileManager());
                        //gameView.loadLevel(stories.get(shift).getPath() + "/second_lvl.json", stories.get(shift).isUserSave());
                        loadHeroes(stories.get(shift));
                        state = MainMenuStates.HERO_SELECTION;
                        break;
                    case 1://Second story
                        gameInitialiser = new GameInitialiser(lvls[shift + 1], context);
                        gameInitialiser.initialiseNewSave(); // makes new save
                        gameInitialiser.startGameLoading(gameView.getFileManager());
                        //gameView.loadLevel(stories.get(shift).getPath() + "/second_lvl.json", stories.get(shift).isUserSave());
                        loadHeroes(stories.get(shift + 1));
                        state = MainMenuStates.HERO_SELECTION;
                        break;
                    case 2://Third story
                        gameInitialiser = new GameInitialiser(lvls[shift + 1], context);
                        gameInitialiser.initialiseNewSave(); // makes new save
                        gameInitialiser.startGameLoading(gameView.getFileManager());
                        loadHeroes(stories.get(shift + 2));
                        state = MainMenuStates.HERO_SELECTION;
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
                    heroSelection.kys();
                }
                backButton.changeImage(false, 0);
                clickedButton = -1;
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
        storyButtons[4] = new MenuButton(2*screenWidth/3, buttonY, up, up, text, -1);
        storyButtons[5] = new MenuButton(2*screenWidth/3, buttonY + Yspace*2, down, down, text, -1);
        backButton = new MenuButton((screenWidth/2 - buttonImage.getWidth())/2, screenHeight - 2 * buttonImage.getHeight(), buttonImage, buttonImageClicked, text, 12);
    }

    /**
     * Starts the game level
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
                                while (!gameView.hasGameHandler());
                                heroSelection.kys();
                                gameView.getGameHandler().setHero(hero);
                                gameInitialiser.saveHeroProperties(hero);
                                gameView.setState(meletos.rpg_game.State.MAP);
                                //gameView.sound.play(meletos.rpg_game.State.MAP);
                                state = MainMenuStates.MAIN;
                                gameView.getGameHandler().startGame();
                            }
                        }.start();

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
                    stories.add(new Story(icon, f.getName(), "rpg_game_data/CustomMaps/" + f.getName(), true));
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
        if (shift != 0) storyButtons[4].draw(canvas);
        if (shift < stories.size() - 3) storyButtons[5].draw(canvas);
    }

    private void loadHeroes(Story story){
        HeroProperties[] heroes = HeroProperties.load(context, story.getPath(), story.isUserSave());
        for (HeroProperties hero : heroes) {
            hero.loadImage(context, story.getPath());
        }
        heroSelection = new HeroSelection(heroes, this, title, screenWidth - field.getWidth() - screenHeight/8, screenHeight - field.getHeight() - screenHeight/10);
        heroSelection.start();
    }

    /*public void setState(MainMenuStates state) {
        this.state = state;
    }*/
}
