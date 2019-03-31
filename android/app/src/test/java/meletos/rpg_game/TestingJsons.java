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
        LevelRepresentation lr = new LevelRepresentation();
        lr.setLvlName("exampleLvl");
        lr.setMapSource("testing_map");
        lr.createCharacterHashmap("Hero", 100, 500, "characters/warrior_m");
        lr.createCharacterHashmap("RandomWalker",100, 800, "characters/ninja_m");
        lr.createCharacterHashmap("RandomWalker", 500, 1000, "characters/townfolk1_f");

        // allows for integer !!!
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

            public JsonElement serialize(Double src, Type typeOfSrc,
                                         JsonSerializationContext context) {
                Integer value = (int)Math.round(src);
                return new JsonPrimitive(value);
            }
        });
        Gson gs = gsonBuilder.create();
        String res = gs.toJson(lr);
        System.out.println(res);
        LevelRepresentation lvlRetrieved = new GsonBuilder().create().fromJson(res, LevelRepresentation.class);
        String other = gs.toJson(lvlRetrieved);

        assertEquals(res, other);
    }

    @Test
    public void checkJsons3() {
        LevelRepresentation lr = new LevelRepresentation();
        lr.setLvlName("exampleLvl");
        lr.setMapSource("example_map");
        lr.createCharacterHashmap("RandomWalker", 100, 1000, "folder");
        lr.createCharacterHashmap("Follower", 1000, 200, "folder2");

    }

}