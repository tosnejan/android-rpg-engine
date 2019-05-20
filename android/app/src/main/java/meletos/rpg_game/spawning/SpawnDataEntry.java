package meletos.rpg_game.spawning;

import java.util.List;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * Simple wrapper for spawn data -- describes what should spawn at what time
 * NOT IN USE ATM
 */
public class SpawnDataEntry {
    List<FatherCharacter> fatherCharacters;
    double time;

    public SpawnDataEntry(List<FatherCharacter> fatherCharacters, double time) {
        this.fatherCharacters = fatherCharacters;
        this.time = time;
    }
}
