package meletos.rpg_game.file_io;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import meletos.rpg_game.Coordinates;
import meletos.rpg_game.inventory.itinerary.ItemType;

/**
 * Wrapper class holding all the information about a level
 * Gets serialised into a json file
 */
public class LevelRepresentation {
    private String lvlName;
    private String mapSource;
    private int[][] inventory;
    private HashMap<ItemType,Integer> equipped;
    private ArrayList<HashMap> characters;
    private Coordinates heroPosition;
    private String lvlText;

    // TODO -- save 'game script' - what should happen when

    public LevelRepresentation () {
        characters = new ArrayList<>();
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }
    public void setMapSource (String mapSource) {
        this.mapSource = mapSource;
    }

    public void setInventory(int[][] inventory) {
        this.inventory = inventory;
    }

    public void setEquipped(HashMap<ItemType, Integer> equipped) {
        this.equipped = equipped;
    }

    private void addCharacter (HashMap character) {
        characters.add(character);
    }

    public void createCharacterHashmap (
            String charType,
            int x, int y,
            String assetsFolder
    ) {
        HashMap character = new HashMap();
        character.put("charType", charType);
        character.put("xCoord", x);
        character.put("yCoord", y);
        character.put("assetsFolder", assetsFolder);
        addCharacter(character);
    }

    public void createCharacterHashmap (
            String charType,
            int x, int y,
            String assetsFolder,
            Coordinates[] followCoord
    ) {
        HashMap character = new HashMap();
        character.put("charType", charType);
        character.put("xCoord", x);
        character.put("yCoord", y);
        character.put("assetsFolder", assetsFolder);
        character.put("followCoord", followCoord);
        addCharacter(character);
    }

    public void removeEntry (String key) {
        characters.remove(key);
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

    public ArrayList<HashMap> getCharacters() {
        return characters;
    }

    public String getLvlName() {
        return lvlName;
    }

    public String getMapSource() {
        return mapSource;
    }

    public int[][] getInventory() {
        return inventory;
    }

    public HashMap<ItemType, Integer> getEquipped() {
        return equipped;
    }
}
