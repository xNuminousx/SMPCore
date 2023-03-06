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
        setItem(topCenterLeft, Material.IRON_SWORD, "PvP Battle", null);
        setItem(topCenter, Material.END_CRYSTAL, "Join a Game", null);
        setItem(topCenterRight, Material.ZOMBIE_HEAD, "Mob Battle", null);
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
