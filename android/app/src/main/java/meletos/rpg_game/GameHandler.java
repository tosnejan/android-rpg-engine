package meletos.rpg_game;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.file_io.LevelRepresentation;
import meletos.rpg_game.inventory.Inventory;
import meletos.rpg_game.menu.HeroProperties;
import meletos.rpg_game.navigation.JoyStick;
import meletos.rpg_game.spawning.SpawnDataEntry;
import meletos.rpg_game.spawning.SpawnHandler;

/**
 * Main logic class. Oversees the characters on the map.
 */
public class GameHandler {
    //characters
    private Hero hero;
    private List<FatherCharacter> characters;
    private SpawnHandler spawnHandler;
    private HeroProperties heroProperties;
    //map info
    private String mapSource;
    private Bitmap map;
    private Bitmap map2;
    private int[][] mapMatrix; // matrix of the map availability
    private int mapWidth;
    private int mapHeight;
    private final int notAvailable = 1; // constant defining whether a pixel is available
    private int xShift = 0;
    private int yShift = 0;
    private int mapScale = 5;

    // screen info
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    private boolean isGamePaused = false;
    public Context context;
    private GameView gameView;

    private String lvlName;
    private Inventory inventory;

    public GameHandler (List<FatherCharacter> characters, final Context context, String lvlName, List<SpawnDataEntry> spawnInstructions) {
        spawnHandler = new SpawnHandler(spawnInstructions, this);
        this.characters = characters;
        this.context = context;
        this.lvlName = lvlName;
        for (FatherCharacter character: characters) {
            if (character.setGameHandler(this)) { // let those characters know I'm the boss!
                hero = (Hero)character; // if true, the character is hero
                characters.remove(character);
            }
        }
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Loads level map from a filename -- usually gets called from LevelGenerator
     * @param fileName
     */
    public void loadMap (String fileName) {
        mapSource = fileName;
        AssetManager am = context.getAssets();
        String path = String.format("maps/%s.png",fileName);
        mapWidth = 0;
        mapHeight = 0;
        try {
            map = BitmapFactory.decodeStream(am.open(path));
            mapWidth = map.getWidth() * mapScale;
            mapHeight = map.getHeight() * mapScale;
            map = Bitmap.createScaledBitmap(map, mapWidth, mapHeight, false);
            path = String.format("maps/%s2.png", fileName);
            map2 = BitmapFactory.decodeStream(am.open(path));
            map2 = Bitmap.createScaledBitmap(map2, mapWidth, mapHeight, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = String.format("maps/%s.txt",fileName);
        BufferedReader reader = null;
        int i = 0;
        int j = 0;
        mapMatrix = new int[mapHeight][mapWidth];
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(path), StandardCharsets.UTF_8));
            String line;
            String[] split;
            int ID = 0;
            while ((line = reader.readLine()) != null) {
                split = line.split("[ ]+");
                for (String num : split) {
                    mapMatrix[j][i] = Integer.parseInt(num);
                    ++i;
                }
                i = 0;
                ++j;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Draws characters and map. Called from gameview's draw
     * @param canvas
     */
    public void drawGame (Canvas canvas) {
        // drawing map
        canvas.drawBitmap(map, xShift, yShift, null);

        // drawing characters
        for (FatherCharacter character: characters) {
            character.draw(canvas);
        }
        hero.draw(canvas);

        // drawing second layer
        canvas.drawBitmap(map2, xShift, yShift, null);

    }

    /**
     * Main logic function. Makes every character update
     */
    public void updateGame () {
        if (isGamePaused) {
            return;
        }
        for (FatherCharacter character: characters) {
            character.update();
        }
        hero.update();
    }

    /**
     * Checks whether the position is available
     * @param x
     * @param y
     * @param imgWidth
     * @param imgHeight
     * @return
     */
    public boolean isPositionAvailable (int x, int y, int imgWidth, int imgHeight) {
        x = (x)/mapScale;
        y = (y)/mapScale;
        imgWidth = imgWidth/mapScale;
        imgHeight = imgHeight/mapScale;
        if (
                x + imgWidth < mapWidth/mapScale &&
                        y + imgHeight < mapHeight/mapScale &&
                        x >= 0 && y >= 0
        ) {
            for (int i = y; i < y + imgHeight; i++) {
                for (int j = x; j < x + imgWidth; j++) {
                    if(mapMatrix[i][j] == notAvailable){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
        /*
        if (x + imgWidth > mapWidth/mapScale) x = mapWidth/mapScale - imgWidth;
        if (y + imgHeight > mapHeight/mapScale) y = mapHeight/mapScale - imgHeight;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        for (int i = y; i < y + imgHeight; i++) {
            for (int j = x; j < x + imgWidth; j++) {
                if(mapMatrix[i][j] != available){
                    return false;
                }
            }
        }
        return true;*/
    }

    public int getxShift() {
        return xShift;
    }

    public int getyShift() {
        return yShift;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public boolean moveMapByX(int x, int xSpeed, int imgWidth) {
        int newX = xShift - xSpeed;
        boolean ret = false;
        if (x + newX < (screenWidth - imgWidth)/2 && xShift == 0) {
            ret = true;
        } else if (x + newX > (screenWidth - imgWidth)/2 && xShift == screenWidth - mapWidth) {
            ret = true;
        } else if ( newX <= 0 && newX >= screenWidth - mapWidth){
            xShift = newX;
            ret = true;
        } else if (newX > 0){
            xShift = 0;
            ret = true;
        } else if (newX < screenWidth - mapWidth){
            xShift = screenWidth - mapWidth;
            ret = true;
        }
        return ret;
    }

    public boolean moveMapByY(int y, int ySpeed, int imgHeight) {
        int newY = yShift - ySpeed;
        boolean ret = false;
        if (y + newY < (screenHeight - imgHeight)/2 && yShift == 0) {
            ret = true;
        }else if (y + newY > (screenHeight - imgHeight)/2 && yShift == screenHeight - mapHeight) {
            ret = true;
        }else if ( newY <= 0 && newY >= screenHeight - mapHeight){
            yShift = newY;
            ret = true;
        }else if (newY > 0){
            yShift = 0;
            ret = true;
        } else if (newY < screenHeight - mapHeight){
            yShift = screenHeight - mapHeight;
            ret = true;
        }
            return ret;
    }
    /**
     * Maybe well make it deprecated :DD
     * @param p
     */
    public Directions suggestDirections(PositionInformation p) {
        Directions yDirection = Directions.NONE;
        Directions xDirection = Directions.NONE;
        Directions finalDirection = Directions.NONE;

        if (p.upperRightCorner.x >= screenWidth) {
            xDirection = Directions.LEFT;
            finalDirection = xDirection;
        } else if (p.mainCoord.x <= 0) {
            xDirection = Directions.RIGHT;
            finalDirection = xDirection;
        }
        if (p.lowerRightCorner.y >= screenHeight) {
            yDirection = Directions.UP;
            finalDirection = yDirection;
        } else if (p.mainCoord.y <= 0) {
            yDirection = Directions.DOWN;
            finalDirection = yDirection;
        }


        if (xDirection == Directions.RIGHT && yDirection == Directions.UP) {
            finalDirection = Directions.UPRIGHT;
        } else if (xDirection == Directions.RIGHT && yDirection == Directions.DOWN) {
            finalDirection = Directions.DOWNRIGHT;
        } else if (xDirection == Directions.LEFT && yDirection == Directions.UP) {
            finalDirection = Directions.UPLEFT;
        } else if (xDirection == Directions.LEFT && yDirection == Directions.DOWN) {
            finalDirection = Directions.DOWNLEFT;
        }
        return finalDirection;
    }

    /**
     * Collision detector -- works by edges of rectangles
     * @param currPosition
     * @param newPosition
     * @return
     */
    public Directions collisionDetector (PositionInformation currPosition, PositionInformation newPosition) {

        for (FatherCharacter character : characters) {
            if (currPosition.equals(character.getPositionInformation())) {
                continue; // probably is the same character
            }
            PositionInformation otherPositionInfo = character.getPositionInformation();

            Directions result = newPosition.collidesWith(otherPositionInfo);
            if (result != Directions.NONE) {
                return result;
            }
        }
        return Directions.NONE;
    }

    /**
     * Pauses game -- characters stop moving and interacting
     */
    public void pauseGame() {
        isGamePaused = true;
    }

    /**
     * Resumes game -- characters start moving
     */
    public void resumeGame() {
        isGamePaused = false;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public void setJoystickToHero (JoyStick js) {
        hero.setJoystick(js);
    }

    /**
     * Saves the current game state -- runs in a new thread
     */
    public void saveGameState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LevelRepresentation lr = new LevelRepresentation();
                for (FatherCharacter character : characters) {
                    lr.createCharacterHashmap(character.getClass().getSimpleName(), character.getX(), character.getY(), character.getAssetsFolder());
                }
                lr.setMapSource(mapSource);
                lr.setLvlName(lvlName);
                lr.setInventory(inventory.getInventory());
                lr.setEquipped(inventory.getEquipped());

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

                    public JsonElement serialize(Double src, Type typeOfSrc,
                                                 JsonSerializationContext context) {
                        Integer value = (int) Math.round(src);
                        return new JsonPrimitive(value);
                    }
                });
                Gson gs = gsonBuilder.create();
                String json = gs.toJson(lr);
                String path = Environment.getExternalStorageDirectory().toString() + "/rpg_game_data/saves/" + lr.getLvlName() + ".json";
                FileWriter out = null;
                try {
                    File saveFile = new File(path);
                    if (!saveFile.exists()) {
                        saveFile.getParentFile().mkdirs();
                    }
                    out = new FileWriter(saveFile);
                    out.write(json);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Inserts new character into GameHandler -- used for spawning
     * @param character
     */
    public void insertCharacter (FatherCharacter character) {
        characters.add(character);
        character.setGameHandler(this);
    }

    public Bitmap getHeroImage(){
        return hero.getImage();
    }

    public void setHero(HeroProperties heroProperties) {
        this.heroProperties = heroProperties;
        hero.getImages(heroProperties.getImagesFolder(), !heroProperties.isCustom());
    }

}

