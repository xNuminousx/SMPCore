package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class EffectHUD extends CoreInventory {

    public EffectHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, Material.ENDER_PEARL, "Ender", null);
        setItem(topCenter, Material.DRAGON_BREATH, "Rainbow", null);
        setItem(topCenterRight, Material.REDSTONE, "Colored Dust", null);
        setItem(bottomCenterLeft, Material.BUCKET, "Remove Effect", Arrays.asList("Command Alternative", "/effect remove"));
        setItem(bottomCenterRight, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Effects";
    }
}
