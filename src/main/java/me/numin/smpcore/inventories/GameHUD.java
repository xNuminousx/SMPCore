package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class GameHUD extends CoreInventory {

    public GameHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, Material.EMERALD, "Start a Game", null);
        setItem(topCenter, Material.DIAMOND_SWORD, "Join Current Game", null);
        setItem(topCenterRight, Material.REDSTONE, "Stop a Game", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public String getName() {
        return "Games";
    }
}
