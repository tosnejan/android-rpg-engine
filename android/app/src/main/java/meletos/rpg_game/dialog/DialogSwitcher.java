package meletos.rpg_game.dialog;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

public class DialogSwitcher {
    public boolean played;

    public boolean check(GameHandler gameHandler, FatherCharacter character){
        if (played){
            return character.isPlayed();
        }
        return true;
    }
}
