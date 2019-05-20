package meletos.rpg_game.inventory;

import java.util.HashMap;

import meletos.rpg_game.GameHandler;
import meletos.rpg_game.inventory.itinerary.Item;
import meletos.rpg_game.inventory.itinerary.ItemType;
import meletos.rpg_game.inventory.itinerary.Itinerary;

public class Inventory {
    private int[][] inventory;
    private HashMap<ItemType,Integer> equipped;
    private transient Itinerary itinerary;

    public Inventory(int[][] inventory, HashMap<ItemType,Integer> equipped) {
        this.inventory = inventory;
        this.equipped = equipped;
    }

    public int[][] getInventory() {
        return inventory;
    }

    public HashMap<ItemType, Integer> getEquipped() {
        return equipped;
    }

    public int getInventoryItem(int row, int column){
        return inventory[row][column];
    }

    public void setInventoryItem(int row, int column, int ID){
        inventory[row][column] = ID;
    }

    public int getEquipedItem(ItemType type){
        return equipped.get(type) != null ? equipped.get(type) : -1;
    }

    public void setEquipedItem(ItemType type, int ID){
        equipped.put(type, ID);
    }

    /**
     * Unequip item and put it to the first empty field. Does nothing when inventory is full.
     * @param type type of item to unequip
     */
    public void unequipItem(ItemType type) {
        for (int row = 0; row < inventory.length; row++) {
            for (int column = 0; column < inventory[row].length; column++) {
                if (inventory[row][column] == -1){
                    inventory[row][column] = equipped.get(type);
                    equipped.put(type, -1);
                    return;
                }
            }
        }
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    /**
     * @param gameHandler actual gameHandler.
     * @return <code>HashMap</code> with sums of equipped items and hero base stats;
     *          <code>false</code> otherwise
     */
    public HashMap<String, Integer> getStats(GameHandler gameHandler){
        HashMap<String, Integer> playerStats = new HashMap<>();
        HashMap<String, Integer> heroStats = gameHandler.getHeroStats();
        for (String key : heroStats.keySet()) {
            playerStats.put(key, heroStats.get(key));
        }
        for (Integer ID:equipped.values()) {
            if (ID != -1) {
                HashMap<String, Integer> stats = itinerary.getItem(ID).getStats();
                for (String key : stats.keySet()) {
                    int value = stats.get(key) + (playerStats.containsKey(key) ? playerStats.get(key) : 0);
                    playerStats.put(key, value);
                }
            }
        }
        return playerStats;
    }

    /**
     * Searching for item with <b>id</b> in inventory.
     * @param id ID of item you are asking to check
     * @return <code>true</code> if item with <b>id</b> is in inventory;
     *          <code>false</code> otherwise
     */
    public boolean hasItem(int id) {
        for (int row = 0; row < inventory.length; row++) {
            for (int column = 0; column < inventory[row].length; column++) {
                if (inventory[row][column] == id){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Adds item with <b>id</b> to the first empty field.
     * @param id ID of item
     */
    public void putItem(int id) {
        for (int row = 0; row < inventory.length; row++) {
            for (int column = 0; column < inventory[row].length; column++) {
                if (inventory[row][column] == -1){
                    inventory[row][column] = id;
                    return;
                }
            }
        }
    }

    /**
     * @param id ID of item
     * @return <code>true</code> if item was in inventory; <code>false</code> otherwise
     */
    public boolean deleteItem(int id) {
        for (int row = 0; row < inventory.length; row++) {
            for (int column = 0; column < inventory[row].length; column++) {
                if (inventory[row][column] == id){
                    inventory[row][column] = -1;
                    return true;
                }
            }
        }
        return false;
    }

    public HashMap<String, Integer> getItemsStats(){
        HashMap<String, Integer> itemsStats = new HashMap<>();
        for (Integer ID:equipped.values()) {
            if (ID != -1) {
                HashMap<String, Integer> stats = itinerary.getItem(ID).getStats();
                for (String key : stats.keySet()) {
                    int value = stats.get(key) + (itemsStats.containsKey(key) ? itemsStats.get(key) : 0);
                    itemsStats.put(key, value);
                }
            }
        }
        return itemsStats;
    }
}
