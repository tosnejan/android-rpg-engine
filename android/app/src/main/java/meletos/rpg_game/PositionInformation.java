package meletos.rpg_game;

import java.util.Objects;

/**
 * Class used for capturing objects position at a given time.
 * Is used in collision detection.
 */
public class PositionInformation {
    private final int imgHeigth;
    private final int imgWidth;
    public final Coordinates mainCoord; // upper left corner
    final Coordinates upperRightCorner;
    private final Coordinates lowerLeftCorner;
    final Coordinates lowerRightCorner;

    public PositionInformation(int x, int y, int imgHeigth, int imgWidth) {
        this.imgHeigth = imgHeigth;
        this.imgWidth = imgWidth;
        mainCoord = new Coordinates(x, y);
        upperRightCorner = new Coordinates(x + imgWidth, y);
        lowerLeftCorner = new Coordinates(x, y + imgHeigth);
        lowerRightCorner = new Coordinates(upperRightCorner.x, lowerLeftCorner.y);
    }

    /**
     * Updates position information
     * @param x coord
     * @param y coord
     */
    private void updatePositionInformation(int x, int y) {
        mainCoord.updateCoordinates(x, y);
        upperRightCorner.updateCoordinates(x + imgWidth, y);
        lowerLeftCorner.updateCoordinates(x, y + imgHeigth);
        lowerRightCorner.updateCoordinates(upperRightCorner.x, lowerLeftCorner.y);
    }

    /**
     * Finds out whether a given coordinate is located within
     * the rectangle of position information
     * @param coordinate to check
     * @return true if inside
     * false otherwise
     */
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

    /**
     * Simpler function than collidesWith, this only informs
     * that there has been a collision
     * @param other to check collision against
     * @return <code>true<code/> if there is a collision
     * <code>false<code/> otherwise
     */
    public boolean collisionCheck (PositionInformation other) {
        return this.isCoordinateInside(other.mainCoord) || this.isCoordinateInside(other.lowerLeftCorner)
                || this.isCoordinateInside(other.lowerRightCorner) || this.isCoordinateInside(other.upperRightCorner);
    }

    /**
     * Checks for collision with other character and suggests what direction to go in
     * @param other to check collision against
     * @return Directions suggested direction to avoid collision
     */
    public Directions collidesWith (PositionInformation other) {
        if (this.isCoordinateInside(other.mainCoord) && this.isCoordinateInside(other.upperRightCorner)) {
            return Directions.UP;
        }
        if (this.isCoordinateInside(other.mainCoord)) {
            return Directions.UPLEFT;
        }
        if (this.isCoordinateInside(other.upperRightCorner)) {
            return Directions.UPRIGHT;
        }
        if (this.isCoordinateInside(other.lowerLeftCorner) && this.isCoordinateInside(other.lowerRightCorner)) {
            return Directions.DOWN;
        }
        if (this.isCoordinateInside(other.lowerLeftCorner)) {
            return Directions.DOWNLEFT;
        }
        if (this.isCoordinateInside(other.lowerRightCorner)) {
            return Directions.DOWNRIGHT;
        }
        return Directions.NONE;
    }

    /**
     * Moves the characters. Adds speed and computes all the corners
     * @param xSpeed to add
     * @param ySpeed to add
     * @param gameHandler to check if position is available
     */
    public void addSpeed (int xSpeed, int ySpeed, GameHandler gameHandler) {
        if (gameHandler.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y + ySpeed);
        } else if (gameHandler.isPositionAvailable(mainCoord.x,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x, mainCoord.y + ySpeed);
        } else if (gameHandler.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
        }
    }

    /**
     * Used by follower
     * @param xSpeed to add
     * @param ySpeed to add
     */
    public void addSpeed (int xSpeed, int ySpeed) {
        updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y + ySpeed);
    }

    /**
     * Enables sliding.
     * @param xSpeed to add
     * @param ySpeed to add
     * @param gh gameHandler
     */
    public void heroAddSpeed (int xSpeed, int ySpeed, GameHandler gh) {
        if (gh.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            if (gh.moveMapByX(mainCoord.x,xSpeed,imgWidth))
                updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
            if (gh.moveMapByY(mainCoord.y,ySpeed,imgHeigth))
                updatePositionInformation(mainCoord.x , mainCoord.y + ySpeed);
        } else if (gh.isPositionAvailable(mainCoord.x,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            if (gh.moveMapByY(mainCoord.y,ySpeed,imgHeigth))updatePositionInformation(mainCoord.x , mainCoord.y + ySpeed);
        } else if (gh.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y,imgWidth,imgHeigth)){
            if (gh.moveMapByX(mainCoord.x,xSpeed,imgWidth))
                updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
        }
    }
}
