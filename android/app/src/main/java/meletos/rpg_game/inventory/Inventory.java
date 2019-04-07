package meletos.rpg_game.inventory;

import java.util.HashMap;

import meletos.rpg_game.inventory.itinerary.ItemType;

public class Inventory {
    private int[][] inventory;
    private HashMap<ItemType,Integer> equipped;

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
}
