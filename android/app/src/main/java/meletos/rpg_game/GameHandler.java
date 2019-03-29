package meletos.rpg_game;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * Class used to check whether the characters are updating properly
 */
public class GameHandler implements Serializable {
    private FatherCharacter[] characters;
    private int[][] mapMatrix; // matrix of the map availability
    private int mapWidth;
    private int mapHeight;
    private final int available = 0; // constant defining whether a pixel is available
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isGamePaused = false;
    private Context context;
    private Bitmap map;
    private int xShift = 0;
    private int yShift = 0;
    private int mapScale = 5;

    public GameHandler (FatherCharacter[] characters, Context context, int[][] mapMatrix) {
        this.characters = characters;
        this.mapMatrix = mapMatrix;
        this.context = context;
        loadMap("testing_map");
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public GameHandler (FatherCharacter[] characters, Context context) {
        this.characters = characters;
        this.context = context;
        loadMap("testing_map");
        for (FatherCharacter character: characters) {
            character.setGameHandler(this); // let those characters know I'm the boss!
        }
    }

    public void loadMap (String fileName) {
        AssetManager am = context.getAssets();
        String path = String.format("maps/%s.png",fileName);
        mapWidth = 0;
        mapHeight = 0;
        try {
            map = BitmapFactory.decodeStream(am.open(path));
            mapWidth = map.getWidth() * mapScale;
            mapHeight = map.getHeight() * mapScale;
            map = Bitmap.createScaledBitmap(map, mapWidth, mapHeight, true);
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

    public void drawGame (Canvas canvas) {
        //String threadName = Thread.currentThread().getName();
        //System.out.println("This is view logic here on thread: " + threadName);
        canvas.drawBitmap(map, xShift, yShift, null);
        for (FatherCharacter character: characters) {
            character.draw(canvas);
        }
    }

    public void updateGame () {
        if (isGamePaused) {
            return;
        }
        for (FatherCharacter character: characters) {
            character.update();
        }
    }

    /**
     * Checks whether the position is available -- a sketch
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
                    if(mapMatrix[i][j] != available){
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
     * Sets character on a new course after it hits some wall
     * -- is quite ugly at the moment :D
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
     * Collision detector typu easy
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

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        isGamePaused = false;
    }

}
