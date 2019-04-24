package meletos.rpg_game.spawning;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.characters.FatherCharacter;

/**
 * This class is responsible for executing the spawn of new characters at a certain time.
 * It accomplishes this by adding characters into gamehandler.
 */
class TimedSpawn extends TimerTask {
    private GameHandler gh;
    private List<FatherCharacter> characters;

    public TimedSpawn(GameHandler gh, List<FatherCharacter> characters) {
        this.gh = gh;
        this.characters = characters;
    }

    @Override
    public void run() {
        for (FatherCharacter character: characters) {
            gh.insertCharacter(character);
        }
    }
}


/**
 * This class is used for spawning characters during the game -- after some time
 * Will be used from GameHandler
 */
public class SpawnHandler {
    private Timer timer;
    private GameHandler gh;
    private  List<SpawnDataEntry> spawnInstructions;

    public SpawnHandler(List<SpawnDataEntry> spawnInstructions, GameHandler gh) {
        this.gh = gh;
        this.spawnInstructions = spawnInstructions;
    }

    /**
     * Begins the timer
     */
    public void begin() {
        if (spawnInstructions == null) {
            return;
        }
        timer = new Timer(); // start countdown
        for (SpawnDataEntry spawn: spawnInstructions) {
            timer.schedule(new TimedSpawn(gh, spawn.fatherCharacters), (int)spawn.time);
        }
    }
}
