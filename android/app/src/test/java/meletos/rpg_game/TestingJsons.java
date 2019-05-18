package meletos.rpg_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.logging.Level;

import meletos.rpg_game.characters.BouncingCharacter;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;
import meletos.rpg_game.file_io.LevelRepresentation;
import meletos.rpg_game.inventory.Inventory;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestingJsons {

    @Test
    public void checkJsons2() {
        String json = new Gson().toJson(new TransitionManager(-1, 130, 375, "house.json", 5, 100));
        String house = new Gson().toJson(new TransitionManager(-1, 885, 845, "second_lvl.json", 5, 150));
        // sirka 150
        System.out.println(house);
        System.out.println(json);
    }
}