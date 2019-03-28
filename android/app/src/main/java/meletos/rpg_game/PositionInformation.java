package meletos.rpg_game;

import java.util.Objects;

public class PositionInformation {
    public int imgHeigth, imgWidth;
    public Coordinates mainCoord; // upper left corner
    public Coordinates upperRightCorner;
    public Coordinates lowerLeftCorner;
    public Coordinates lowerRightCorner;

    public PositionInformation(int x, int y, int imgHeigth, int imgWidth) {
        this.imgHeigth = imgHeigth;
        this.imgWidth = imgWidth;
        updatePositionInformation(x, y);
    }
    
    public void updatePositionInformation (int x, int y) {
        mainCoord = new Coordinates(x, y);
        upperRightCorner = new Coordinates(x + imgWidth, y);
        lowerLeftCorner = new Coordinates(x, y + imgHeigth);
        lowerRightCorner = new Coordinates(upperRightCorner.x, lowerLeftCorner.y);
    }

    public boolean isCoordinateInside (Coordinates coordinate) {
        return (coordinate.x >= mainCoord.x && coordinate.x <= lowerRightCorner.x)
            && (coordinate.y >= mainCoord.y && coordinate.y <= lowerRightCorner.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionInformation that = (PositionInformation) o;
        return mainCoord.equals(that.mainCoord) &&
               upperRightCorner.equals(that.upperRightCorner) &&
                lowerLeftCorner.equals(that.lowerLeftCorner) &&
                lowerRightCorner.equals(that.lowerRightCorner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainCoord, upperRightCorner, lowerLeftCorner, lowerRightCorner);
    }

    public boolean collisionCheck (PositionInformation other) {
        return this.isCoordinateInside(other.mainCoord) || this.isCoordinateInside(other.lowerLeftCorner)
                || this.isCoordinateInside(other.lowerRightCorner) || this.isCoordinateInside(other.upperRightCorner);
    }

    /**
     * Checks for collision with other character and suggests what direction to go in
     * @param other
     * @return
     */
    public Directions collidesWith (PositionInformation other) {
        if (this.isCoordinateInside(other.mainCoord) || this.isCoordinateInside(other.upperRightCorner)) {
            return Directions.UP;
        }
        if (this.isCoordinateInside(other.lowerLeftCorner) || this.isCoordinateInside(other.lowerRightCorner)) {
            return Directions.DOWN;
        }
        return Directions.NONE;
    }

    public void addSpeed (int xSpeed, int ySpeed) {
        updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y + ySpeed);
    }
}
