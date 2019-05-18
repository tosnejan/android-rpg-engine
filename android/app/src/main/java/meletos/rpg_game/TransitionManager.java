package meletos.rpg_game;

import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.inventory.Inventory;
import meletos.rpg_game.menu.MainMenuStates;

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
    private boolean heroVisited = false;

    public TransitionManager(int keyID, int x, int y, String nextLevelName) {
        this.keyID = keyID;
        this.nextLevelName = nextLevelName;
        transitionPosition = new PositionInformation(x, y, HEIGHT, WIDTH);
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
