package meletos.rpg_game;

import android.graphics.Path;

import java.util.Objects;

public class PositionInformation {
    public int imgHeigth, imgWidth;
    public Coordinates upperLeftCorner;
    public Coordinates upperRightCorner;
    public Coordinates lowerLeftCorner;
    public Coordinates lowerRightCorner;

    public PositionInformation(int x, int y, int imgHeigth, int imgWidth) {
        this.imgHeigth = imgHeigth;
        this.imgWidth = imgWidth;
        updatePositionInformation(x, y);
    }
    
    public updatePositionInformation (int x, int y) {
        upperLeftCorner = new Coordinates(x, y);
        upperRightCorner = new Coordinates(x + imgWidth, y);
        lowerLeftCorner = new Coordinates(x, y + imgHeigth);
        lowerRightCorner = new Coordinates(upperRightCorner.x, lowerLeftCorner.y);
    }

    public boolean isCoordinateInside (Coordinates coordinate) {
        return (coordinate.x >= upperLeftCorner.x && coordinate.x <= lowerRightCorner.x)
            && (coordinate.y >= upperLeftCorner.y && coordinate.y <= lowerRightCorner.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionInformation that = (PositionInformation) o;
        return upperLeftCorner.equals(that.upperLeftCorner) &&
               upperRightCorner.equals(that.upperRightCorner) &&
                lowerLeftCorner.equals(that.lowerLeftCorner) &&
                lowerRightCorner.equals(that.lowerRightCorner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upperLeftCorner, upperRightCorner, lowerLeftCorner, lowerRightCorner);
    }

    public boolean collisionCheck (PositionInformation other) {
        return this.isCoordinateInside(other.upperLeftCorner) || this.isCoordinateInside(other.lowerLeftCorner)
                || this.isCoordinateInside(other.lowerRightCorner) || this.isCoordinateInside(other.upperRightCorner);
    }

    /**
     * Checks for collision with other character and suggests what direction to go in
     * @param other
     * @return
     */
    public Directions collidesWith (PositionInformation other) {
        if (this.isCoordinateInside(other.upperLeftCorner) || this.isCoordinateInside(other.upperRightCorner)) {
            return Directions.UP;
        }
        if (this.isCoordinateInside(other.lowerLeftCorner) || this.isCoordinateInside(other.lowerRightCorner)) {
            return Directions.DOWN;
        }
        return Directions.NONE;
    }
}
