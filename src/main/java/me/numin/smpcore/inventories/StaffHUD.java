package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class StaffHUD extends CoreInventory {

    public StaffHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, Material.WRITTEN_BOOK, "Manage Reports", null);
        setItem(topCenterRight, Material.COMPASS, "Player Locations", null);
        setItem(bottomCenter, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Staff Actions";
    }
}
