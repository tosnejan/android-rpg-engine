package meletos.rpg_game;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CollisionTest {
    @Test
    public void collisionCheck1() {
        PositionInformation firstPos = new PositionInformation(0,0, 4, 4);
        PositionInformation secondPos = new PositionInformation(1,1, 4, 4);
        assertTrue(firstPos.collisionCheck(secondPos));
    }

    @Test
    public void collisionCheck2 () {
        PositionInformation firstPos = new PositionInformation(0,0, 4, 4);
        PositionInformation secondPos = new PositionInformation(4,4, 4, 4);
        assertTrue(firstPos.collisionCheck(secondPos));
    }

    @Test
    public void collisionCheck3 () {
        PositionInformation firstPos = new PositionInformation(4,4, 4, 4);
        PositionInformation secondPos = new PositionInformation(0,0, 4, 4);
        assertTrue(firstPos.collisionCheck(secondPos));
    }

    @Test
    public void collisionCheck4 () {
        PositionInformation firstPos = new PositionInformation(4,4, 4, 4);
        PositionInformation secondPos = new PositionInformation(3,3, 1, 1);
        assertTrue(firstPos.collisionCheck(secondPos));
    }

    @Test
    public void collisionCheck5 () {
        PositionInformation firstPos = new PositionInformation(4,4, 4, 4);
        PositionInformation secondPos = new PositionInformation(3,4, 1, 1);
        assertTrue(firstPos.collisionCheck(secondPos));
    }

    @Test
    public void collisionCheck6 () {
        PositionInformation firstPos = new PositionInformation(4,4, 4, 4);
        PositionInformation secondPos = new PositionInformation(3,4, 1, 1);
        assertEquals(Directions.UPRIGHT, firstPos.collidesWith(secondPos));
    }

    @Test
    public void collisionCheck7 () {
        PositionInformation firstPos = new PositionInformation(4,4, 4, 4);
        PositionInformation secondPos = new PositionInformation(1000,20000, 1, 1);
        assertEquals(Directions.NONE, firstPos.collidesWith(secondPos));
    }
}