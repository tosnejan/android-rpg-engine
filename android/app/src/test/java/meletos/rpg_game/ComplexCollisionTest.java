package meletos.rpg_game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ComplexCollisionTest {
    @Test
    public void collisionCheck1() {
        PositionInformation firstPos = new PositionInformation(0,0, 4, 4);
        PositionInformation secondPos = new PositionInformation(1,1, 4, 4);
        assertEquals(true, firstPos.collisionCheck(secondPos));
    }
}