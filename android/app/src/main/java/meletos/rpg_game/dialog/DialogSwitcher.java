package meletos.rpg_game.dialog;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

/**
 * Used for switching between dialogs.
 */
public class DialogSwitcher {
    private boolean played;
    private int[] itemsToCheck;
    private int[] itemsToDelete;
    private int[] itemsToGive;

    /**
     * @param gameHandler An actual <code>GameHandler</code>.
     * @param character The character whose dialog is checking.
     * @return <code>True</code> if character should increase his <code>actualDialog</code>
     *          otherwise return <code>false</code>.
     */
    public boolean check(GameHandler gameHandler, FatherCharacter character){
        if (played){
            if (!character.isPlayed()) return false;
        }

        for (int i = 0; i < itemsToCheck.length; i++) {
            if(!gameHandler.getInventory().hasItem(itemsToCheck[i])) return false;
        }

        for (int i = 0; i < itemsToDelete.length; i++) {
            gameHandler.getInventory().deleteItem(itemsToDelete[i]);
        }

        for (int i = 0; i < itemsToGive.length; i++) {
            gameHandler.getInventory().putItem(itemsToGive[i]);
        }
        return true;
    }
}
