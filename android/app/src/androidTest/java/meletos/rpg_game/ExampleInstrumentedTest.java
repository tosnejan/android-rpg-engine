package meletos.rpg_game;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import meletos.rpg_game.characters.FatherCharacter;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.characters.RandomWalker;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private List<FatherCharacter> characters = new ArrayList<FatherCharacter>();
    Context appContext = InstrumentationRegistry.getTargetContext();


    @Test
    public void useAppContext() {
        // Context of the app under test.

        assertEquals("meletos.rpg_game", appContext.getPackageName());
    }

    @Test
    public void testException() {
        try {


            RandomWalker r1 = new RandomWalker(1, 1, appContext, true, null);
            characters.add(r1);
            RandomWalker r2 = new RandomWalker(1, 1, appContext, true, null);
            characters.add(r2);
            Hero hero = new Hero(100, 100, null, null, false);
            GameHandler gh = new GameHandler(characters, hero, appContext, "test", null, null);
            gh.removeCharacter(r1);
            gh.removeCharacter(r2);
        } catch (Exception e) {
            assertEquals("NullPointerException", e.getClass().getSimpleName());
        }

    }

}
