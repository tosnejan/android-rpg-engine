package meletos.rpg_game.dialog;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

public class DialogSwitcher {
    private boolean played;
    private int[] itemsToCheck;
    private int[] itemsToDelete;
    private int[] itemsToGive;

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
