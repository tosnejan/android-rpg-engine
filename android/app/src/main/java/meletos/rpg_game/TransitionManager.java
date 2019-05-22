package meletos.rpg_game;

import android.util.Log;

import java.util.Objects;

import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.inventory.Inventory;

/**
 * It will handles transitions from one level to another
 * Implements lock.
 */
public class TransitionManager {
    private final int height;
    private final int width; //  setting the senstitivity
    private final PositionInformation transitionPosition; // position where hero gets checked
    private final int keyID; // the key that hero has to have -- if -1 it means free pass
    private final String nextLevelName; // saves the name of the next level
    private boolean heroVisited = false;

    TransitionManager(int keyID, int x, int y, String nextLevelName, int height, int width) {
        this.keyID = keyID;
        this.nextLevelName = nextLevelName;
        this.height = height;
        this.width = width;
        transitionPosition = new PositionInformation(x, y, this.height, this.width);
    }

    /**
     * Checks for hero. When it is in proximity, it will transport him
     * @param hero to check for
     * @param inventory used to find key
     * @param gameView so that it is able to trigger new level
     */
    void checkForHero(Hero hero, Inventory inventory, GameView gameView) {
        if (transitionPosition.collisionCheck(hero.positionInformation)) {
            if ((inventory.hasItem(keyID) || keyID == -1) && !heroVisited) {
                gameView.getGameHandler().saveGameState();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage());
                }
                heroVisited = true;
                // start new level
                gameView.getGameHandler().recycleBitmaps();
                gameView.setHasGameHandler(false);
                gameView.setState(State.LOADING);
                gameView.getFileManager().loadLevels(nextLevelName, inventory);

                while (!gameView.hasGameHandler()) { // waiting for loading
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                    }
                }
                gameView.getGameHandler().setHero(gameView.getFileManager().loadHeroProperties());
                gameView.setState(State.MAP);
                gameView.getGameHandler().startGame();
            }
        } else {
            heroVisited = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransitionManager that = (TransitionManager) o;
        return height == that.height &&
                width == that.width &&
                keyID == that.keyID &&
                heroVisited == that.heroVisited &&
                Objects.equals(transitionPosition, that.transitionPosition) &&
                nextLevelName.equals(that.nextLevelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, width, transitionPosition, keyID, nextLevelName, heroVisited);
    }
}
