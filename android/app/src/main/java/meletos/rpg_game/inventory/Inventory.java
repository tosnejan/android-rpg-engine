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
}
