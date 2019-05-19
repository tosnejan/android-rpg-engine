package meletos.rpg_game.file_io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.GameHandler;
import meletos.rpg_game.TransitionManager;
import meletos.rpg_game.characters.Chest;
import meletos.rpg_game.characters.Hero;
import meletos.rpg_game.inventory.itinerary.ItemType;

/**
 * Wrapper class holding all the information about a level
 * Gets serialised into a json file and retrieved from it.
 */
public class LevelRepresentation {
    private String lvlName;
    private String mapSource;
    private ArrayList<CharacterRepresentation> characters;
    private CharacterRepresentation hero;
    private Coordinates heroPosition;
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

    public LevelRepresentation (GameHandler gh) {

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

    public TransitionManager getTransitionManager() {
        return transitionManager;
    }


    public void setChests(List<Chest> chests) {
        this.chests = chests;
    }

    public List<Chest> getChests() {
        return chests;
    }

    public void setTransitionManager(TransitionManager transitionManager) {
        this.transitionManager = transitionManager;
    }

    private void addSpawnInstructions (double time, List<HashMap> charactersToSpawn) {
        spawnStructure.put(Double.toString(time), charactersToSpawn);
    }

    /*
    public void createCharacterHashmap (
            String charType,
            int x, int y,
            String imagesFolder,
            boolean isHero,
            boolean enemy
    ) {
        HashMap character = new HashMap();
        character.put("charType", charType);
        character.put("imagesFolder", imagesFolder);
        character.put("isEnemy", enemy);
        character.put("xCoord", x);
        character.put("yCoord", y);
        if (!isHero) {
            addCharacter(character);
        } else {
            hero = character;
        }
    }

    // for follower
    public void createCharacterHashmap (
            String charType,
            int x, int y,
            String assetsFolder,
            Coordinates[] followCoord,
            boolean enemy
    ) {
        HashMap character = new HashMap();
        character.put("charType", charType);
        character.put("isEnemy", enemy);
        character.put("xCoord", x);
        character.put("yCoord", y);
        character.put("assetsFolder", assetsFolder);
        character.put("followCoord", followCoord);
        addCharacter(character);
    }

    public void removeEntry (String key) {
        characters.remove(key);
    }*/

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

    public String getLvlName() {
        return lvlName;
    }

    public String getMapSource() {
        return mapSource;
    }


    public HashMap<String, List<HashMap>> getSpawnStructure() {
        return spawnStructure;
    }

    public CharacterRepresentation getHero () {
        return hero;
    }

    @Override
    public String toString() {
        return "LevelRepresentation{" +
                "lvlName='" + lvlName + '\'' +
                ", mapSource='" + mapSource + '\'' +
                ", characters=" + characters +
                ", hero=" + hero +
                ", heroPosition=" + heroPosition +
                ", lvlText='" + lvlText + '\'' +
                ", spawnStructure=" + spawnStructure +
                '}';
    }
}
