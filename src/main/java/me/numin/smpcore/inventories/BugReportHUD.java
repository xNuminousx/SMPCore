package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Collections;

public class BugReportHUD extends CoreInventory {

    public BugReportHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, Material.FLOWER_BANNER_PATTERN, "Plugin Bug", Collections.singletonList("Issues involving a plugin."));
        setItem(topCenter, Material.GLOBE_BANNER_PATTERN, "Game Bug", Collections.singletonList("Issues involving the game or its mechanics."));
        setItem(topCenterRight, Material.PIGLIN_BANNER_PATTERN, "Forum Bug", Collections.singletonList("Issues involving the website or its forums."));
        setItem(bottomCenterLeft, Material.WRITABLE_BOOK, "Report Something Not Listed", null);
        setItem(bottomCenterRight, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Report a Bug";
    }
}
