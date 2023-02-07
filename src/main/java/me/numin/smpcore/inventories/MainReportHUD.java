package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Collections;

public class MainReportHUD extends CoreInventory {

    public MainReportHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, Material.BOOK, "Report A Player", Collections.singletonList("Click to report a player."));
        setItem(topCenter, Material.WRITTEN_BOOK, "Manage Reports", Collections.singletonList("Staff Only"));
        setItem(topCenterRight, Material.BOOK, "Report A Bug", Collections.singletonList("Click to report a bug."));
        setItem(bottomCenter, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Make a Report";
    }
}