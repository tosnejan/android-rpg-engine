package meletos.rpg_game.file_io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import meletos.rpg_game.TransitionManager;
import meletos.rpg_game.characters.Chest;

/**
 * Wrapper class holding all the information about a level
 * Gets serialised into a json file and retrieved from it.
 */
public class LevelRepresentation {
    private String lvlName;
    private String mapSource;
    private ArrayList<CharacterRepresentation> characters;
    private CharacterRepresentation hero;
    private String lvlText;
    private TransitionManager transitionManager;
    private List<Chest> chests;

    /**
    *hashmap with double keys representing time, list of character
    *hashmaps representing characters to be spawned at any given time
     */
    private HashMap<String, List<HashMap>> spawnStructure = null;


    public LevelRepresentation () {
        characters = new ArrayList<>();
    }


    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }
    public void setMapSource (String mapSource) {
        this.mapSource = mapSource;
    }
    public void addCharacter (CharacterRepresentation character) {
        characters.add(character);
    }

    public void setHero (CharacterRepresentation hero) {
        this.hero = hero;
    }

    TransitionManager getTransitionManager() {
        return transitionManager;
    }

    public void setChests(List<Chest> chests) {
        this.chests = chests;
    }

    List<Chest> getChests() {
        return chests;
    }

    public void setTransitionManager(TransitionManager transitionManager) {
        this.transitionManager = transitionManager;
    }

    private void addSpawnInstructions (double time, List<HashMap> charactersToSpawn) {
        spawnStructure.put(Double.toString(time), charactersToSpawn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LevelRepresentation that = (LevelRepresentation) o;
        return Objects.equals(lvlName, that.lvlName) &&
                Objects.equals(mapSource, that.mapSource) &&
                Objects.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lvlName, mapSource, characters);
    }

    public ArrayList<CharacterRepresentation> getCharacters() {
        return characters;
    }

    String getLvlName() {
        return lvlName;
    }

    String getMapSource() {
        return mapSource;
    }


    HashMap<String, List<HashMap>> getSpawnStructure() {
        return spawnStructure;
    }

    public CharacterRepresentation getHero () {
        return hero;
    }
}
