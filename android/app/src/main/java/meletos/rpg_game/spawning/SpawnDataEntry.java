package meletos.rpg_game.spawning;

import java.util.List;

import meletos.rpg_game.characters.FatherCharacter;

/**
 * Simple wrapper for spawn data -- describes what should spawn at what time
 * NOT IN USE ATM
 */
class SpawnDataEntry {
    final List<FatherCharacter> fatherCharacters;
    final double time;

    public SpawnDataEntry(List<FatherCharacter> fatherCharacters, double time) {
        this.fatherCharacters = fatherCharacters;
        this.time = time;
    }
}
