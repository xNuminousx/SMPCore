package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.spells.api.Wands.WandItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class WandRecipeHUD extends CoreInventory {

    public WandRecipeHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(topCenterLeft, WandItem.SPELL_OF_DAMAGING.getWand(), null, null);
        setItem(topCenter, WandItem.SPELL_OF_HEALING.getWand(), null, null);
        setItem(topCenterRight, WandItem.SPELL_OF_FAMILIAR.getWand(), null, null);
        setItem(bottomCenter, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Wand Recipes";
    }
}
