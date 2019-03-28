package meletos.rpg_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import meletos.rpg_game.characters.BouncingCharacter;
import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;

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
    public void checkJsons() {
        PositionInformation[] characters =
                {
                        new PositionInformation(100, 200, 1, 2),
                        new PositionInformation(500, 1000, 1, 2),

                };
        Gson gs = new Gson();
        String res = gs.toJson(characters);
        PositionInformation[] targetArray = new GsonBuilder().create().fromJson(res, PositionInformation[].class);
        System.out.println(res);
        assertArrayEquals(characters, targetArray);
    }

    /*@Test
    public void checkJsons2() {
        GameHandler gameHandler = new GameHandler(null, null );
        Gson gs = new Gson();
        String res = gs.toJson(gameHandler);
        GameHandler gH = new GsonBuilder().create().fromJson(res, GameHandler.class);
        System.out.println(res);
        assertEquals(gameHandler, gH);
    }*/

}