package me.numin.smpcore.inventories.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InteractiveInventory {
    void open();
    void close();
    void setItems();
    int getSlotCount();
    Inventory getInventory();
    InventoryType getType();
    Player getPlayer();
    String getName();
}
