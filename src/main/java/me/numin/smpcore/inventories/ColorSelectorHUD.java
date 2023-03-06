package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class ColorSelectorHUD extends CoreInventory {

    public ColorSelectorHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(1, Material.RED_WOOL, "Red", null);
        setItem(2, Material.ORANGE_WOOL, "Orange", null);
        setItem(3, Material.YELLOW_WOOL, "Yellow", null);
        setItem(4, Material.GREEN_WOOL, "Green", null);
        setItem(5, Material.BLUE_WOOL, "Blue", null);
        setItem(6, Material.PURPLE_WOOL, "Purple", null);
        setItem(7, Material.PINK_WOOL, "Pink", null);
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
        return "Color Selector";
    }
}
