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
        mainCoord = new Coordinates(x, y);
        upperRightCorner = new Coordinates(x + imgWidth, y);
        lowerLeftCorner = new Coordinates(x, y + imgHeigth);
        lowerRightCorner = new Coordinates(upperRightCorner.x, lowerLeftCorner.y);
    }
    
    public void updatePositionInformation (int x, int y) {
        mainCoord.updateCoordinates(x, y);
        upperRightCorner.updateCoordinates(x + imgWidth, y);
        lowerLeftCorner.updateCoordinates(x, y + imgHeigth);
        lowerRightCorner.updateCoordinates(upperRightCorner.x, lowerLeftCorner.y);
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

    public void addSpeed (int xSpeed, int ySpeed, GameHandler gameHandler) {
        if (gameHandler.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y + ySpeed);
        } else if (gameHandler.isPositionAvailable(mainCoord.x,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x, mainCoord.y + ySpeed);
        } else if (gameHandler.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y,imgWidth,imgHeigth)){
            updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
        }
    }
    // used by follower
    public void addSpeed (int xSpeed, int ySpeed) {
        updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y + ySpeed);
    }
    public void heroAddSpeed (int xSpeed, int ySpeed, GameHandler gh) {
        if (gh.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            if (gh.moveMapByX(mainCoord.x,xSpeed,imgWidth));
                updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
            if (gh.moveMapByY(mainCoord.y,ySpeed,imgHeigth));
                updatePositionInformation(mainCoord.x , mainCoord.y + ySpeed);
        } else if (gh.isPositionAvailable(mainCoord.x,mainCoord.y + ySpeed,imgWidth,imgHeigth)){
            if (gh.moveMapByY(mainCoord.y,ySpeed,imgHeigth))updatePositionInformation(mainCoord.x , mainCoord.y + ySpeed);
        } else if (gh.isPositionAvailable(mainCoord.x + xSpeed,mainCoord.y,imgWidth,imgHeigth)){
            if (gh.moveMapByX(mainCoord.x,xSpeed,imgWidth))
                updatePositionInformation(mainCoord.x + xSpeed, mainCoord.y);
        }
    }
}
