package meletos.rpg_game.inventory;

import java.util.HashMap;

import meletos.rpg_game.inventory.itinerary.Item;
import meletos.rpg_game.inventory.itinerary.ItemType;
import meletos.rpg_game.inventory.itinerary.Itinerary;

public class Inventory {
    private int[][] inventory;
    private HashMap<ItemType,Integer> equipped;
    private Itinerary itinerary;

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

    public HashMap<String, Integer> getStats(){
        HashMap<String, Integer> playerStats = new HashMap<>();
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
}
