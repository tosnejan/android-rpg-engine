package meletos.rpg_game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import meletos.rpg_game.file_io.CharacterRepresentation;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestingJsons {

    @Test
    public void checkJsonsTransitionManager1() {
        TransitionManager transitionManager1 = new TransitionManager(-1, 130, 375, "house.json", 5, 100);
        String json = new Gson().toJson(transitionManager1);
        String house = new Gson().toJson(new TransitionManager(-1, 885, 845, "second_lvl.json", 5, 150));
        TransitionManager tm2 = new GsonBuilder().create().fromJson(json, TransitionManager.class);
        assertEquals(transitionManager1, tm2);
    }

    @Test
    public void checkJsonsTransitionManager2() {
        TransitionManager transitionManager1 = new TransitionManager(-1, 885, 845, "second_lvl.json", 5, 150);
        String json = new Gson().toJson(transitionManager1);
        TransitionManager tm2 = new GsonBuilder().create().fromJson(json, TransitionManager.class);
        assertEquals(transitionManager1, tm2);
    }


    @Test
    public void checkJsonsCharacterRepresentation() {
        CharacterRepresentation cr1 = new CharacterRepresentation("RandomWalker", "img", "image", true, 10, 200, null, null, 1, true, null);
        String json = new Gson().toJson(cr1);
        CharacterRepresentation cr2 = new GsonBuilder().create().fromJson(json, CharacterRepresentation.class);
        assertEquals(cr1, cr2);
    }

    @Test
    public void checkJsons4() {
        TransitionManager transitionManager1 = new TransitionManager(-1, 885, 845, "second_lvl.json", 5, 150);
        String json = new Gson().toJson(transitionManager1);
        TransitionManager tm2 = new GsonBuilder().create().fromJson(json, TransitionManager.class);
        assertEquals(transitionManager1, tm2);
    }
}