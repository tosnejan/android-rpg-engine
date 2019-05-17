package meletos.rpg_game;

import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.inventory.Inventory;

/**
 * SKETCH - TODO
 * Each level will have one or more -- it will handle transitions from one level to another
 * Implements lock.
 */
public class TransitionManager {
    private final int HEIGHT = 5;
    private final int WIDTH = 5; // constants setting the senstitivity
    private PositionInformation transitionPosition; // position where hero gets checked
    private int keyID; // the key that hero has to have
    private String nextLevelName; // saves the name of the next level


    public TransitionManager(int keyID, int x, int y, String nextLevelName) {
        this.keyID = keyID;
        this.nextLevelName = nextLevelName;
        transitionPosition = new PositionInformation(x, y, HEIGHT, WIDTH);
    }

    public void CheckForHero (Hero hero, Inventory inventory, GameView gameView) {
        if (transitionPosition.collisionCheck(hero.positionInformation)) {
            if (inventory.hasItem(keyID)) {
                // start new map

                gameView.loadLevel(nextLevelName, true);
            }
        }
    }

}
