package meletos.rpg_game.file_io;

import com.google.gson.Gson;

/**
 * Will be used to load the objects from the level file
 * returns an array of objects to be used by the game
 */
public class LevelInterpreter {
    private String name = "MainActivity.java";
    private Object[] gameObjects;

    public LevelInterpreter() {
    }

    public void readFromFile () {
        Gson gson = new Gson();
        //gameObjects = gson.fromJson();

    }

}
