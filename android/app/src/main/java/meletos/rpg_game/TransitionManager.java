package meletos.rpg_game;

import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.inventory.Inventory;

/**
 * SKETCH - TODO
 * Each level will have one or more -- it will handle transitions from one level to another
 * Implements lock.
 */
public class TransitionManager {
    private int height;
    private int width; //  setting the senstitivity
    private PositionInformation transitionPosition; // position where hero gets checked
    private int keyID; // the key that hero has to have
    private String nextLevelName; // saves the name of the next level
    private boolean heroVisited = false;

    public TransitionManager(int keyID, int x, int y, String nextLevelName, int height, int width) {
        this.keyID = keyID;
        this.nextLevelName = nextLevelName;
        this.height = height;
        this.width = width;
        transitionPosition = new PositionInformation(x, y, this.height, this.width);
    }

    public void checkForHero (Hero hero, Inventory inventory, GameView gameView) {
        if (transitionPosition.collisionCheck(hero.positionInformation) && !heroVisited) {
            if (inventory.hasItem(keyID) || keyID == -1) {
                System.out.println("HERO IS HERE");
                heroVisited = true;
                // start new map
                gameView.setHasGameHandler(false);
                gameView.getFileManager().loadLevels(nextLevelName);

                while (!gameView.hasGameHandler()) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //heroSelection.kys();
                //gameView.getGameHandler().setHero(hero);
                //gameInitialiser.saveHeroProperties(hero);
                gameView.getGameHandler().setHero(gameView.getFileManager().loadHeroProperties());

                gameView.getGameHandler().startGame();
            }
        }
    }

}
