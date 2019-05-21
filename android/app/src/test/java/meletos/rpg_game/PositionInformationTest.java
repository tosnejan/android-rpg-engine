package meletos.rpg_game;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionInformationTest {
    private final PositionInformation pos = new PositionInformation(0, 0, 100, 100);

    @Test
    public void isCoordinateInside1() {
        Coordinates coordinate = new Coordinates(1, 1);
        assertTrue(pos.isCoordinateInside(coordinate));
    }

    @Test
    public void isCoordinateInside2() {
        Coordinates coordinate = new Coordinates(-1, -10);
        assertFalse(pos.isCoordinateInside(coordinate));
    }

    @Test
    public void collisionCheck() {
        PositionInformation otherPositionInfo = new PositionInformation(10, 10, 1, 1);
        assertTrue(pos.collisionCheck(otherPositionInfo));
    }

    @Test
    public void addSpeed1() {
        int beforeX = pos.mainCoord.x;
        int beforeY = pos.mainCoord.y;
        pos.addSpeed(1, 1);
        assertTrue(beforeX < pos.mainCoord.x && beforeY < pos.mainCoord.y);
    }

    @Test
    public void addSpeed2() {
        int beforeX = pos.upperRightCorner.x;
        int beforeY = pos.upperRightCorner.y;
        pos.addSpeed(1, 1);
        assertTrue(beforeX < pos.upperRightCorner.x && beforeY < pos.upperRightCorner.y);

    }

    @Test
    public void addSpeed3() {
        int beforeX = pos.lowerRightCorner.x;
        int beforeY = pos.lowerRightCorner.y;
        pos.addSpeed(1, 1);
        assertTrue(beforeX < pos.lowerRightCorner.x && beforeY < pos.lowerRightCorner.y);
    }

    @Test
    public void addSpeed4() {
        int beforeX = pos.mainCoord.x;
        int beforeY = pos.mainCoord.y;
        pos.addSpeed(-1, -1);
        assertFalse(beforeX < pos.mainCoord.x && beforeY < pos.mainCoord.y);
    }

    @Test
    public void addSpeed5() {
        int beforeX = pos.upperRightCorner.x;
        int beforeY = pos.upperRightCorner.y;
        pos.addSpeed(-1, -1);
        assertFalse(beforeX < pos.upperRightCorner.x && beforeY < pos.upperRightCorner.y);

    }

    @Test
    public void addSpeed6() {
        int beforeX = pos.lowerRightCorner.x;
        int beforeY = pos.lowerRightCorner.y;
        pos.addSpeed(-1, -1);
        assertFalse(beforeX < pos.lowerRightCorner.x && beforeY < pos.lowerRightCorner.y);
    }

}