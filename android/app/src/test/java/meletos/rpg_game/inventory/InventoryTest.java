package meletos.rpg_game.inventory;

import org.junit.Test;

import java.util.HashMap;

import meletos.rpg_game.inventory.itinerary.ItemType;

import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory i = new Inventory(new int[10][10], new HashMap<ItemType, Integer>());

    @Test
    public void getEquipped() {
        int item = 1;
        i.putItem(item);
        i.setEquipedItem(ItemType.ARMOR, 1);
        HashMap<ItemType, Integer> equipped = i.getEquipped();
        assertNotNull(equipped);
    }

    @Test
    public void getInventoryItem() {
        int item = i.getInventoryItem(1,1);
        assertEquals(item, 0);
    }

    @Test
    public void setEquipedItem() {
        int item = 20;
        i.putItem(item);
        i.setEquipedItem(ItemType.BELT, 1);
        HashMap<ItemType, Integer> equipped = i.getEquipped();
        assertNotNull(equipped);
    }

    @Test
    public void unequipItem() {
        i.unequipItem(ItemType.ARMOR);
        i.unequipItem(ItemType.BELT);
        HashMap<ItemType, Integer> equipped = i.getEquipped();
        HashMap<ItemType, Integer> expected = new HashMap<ItemType, Integer>();
        assertEquals(equipped, expected);
    }

}