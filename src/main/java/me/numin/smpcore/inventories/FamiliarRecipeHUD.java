package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.spells.api.Wands.WandItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class FamiliarRecipeHUD extends CoreInventory {

    public FamiliarRecipeHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(2, Material.NETHER_WART, "Nether Wart", null);
        setItem(4, Material.BONE, "Bone", null);
        setItem(5, Material.STICK, "Stick or Wand", Arrays.asList("You may use a regular stick", "or you can use a wand you've", "already built!"));
        setItem(6, Material.EGG, "Egg", null);
        setItem(8, Material.NETHER_WART, "Nether Wart", null);
        getInventory().setItem(0, WandItem.SPELL_OF_FAMILIAR.getWand());
    }

    @Override
    public InventoryType getType() {
        return InventoryType.WORKBENCH;
    }

    @Override
    public String getName() {
        return "Spell of Familiar";
    }
}
