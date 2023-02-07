package me.numin.smpcore.listeners;

import me.numin.smpcore.inventories.*;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.spells.api.Wands;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class WandInventoryListener implements Listener {

    private InventoryClickEvent event;

    @EventHandler
    public void hudClick(InventoryClickEvent event) {
        this.event = event;
        Player player = (Player) event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);
            ItemStack clickedItem = event.getCurrentItem();

            if (invalidClick()) {
                event.setCancelled(true);
                return;
            }

            if (coreInventory.getName().equalsIgnoreCase("Wand Recipes")) {
                if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Damaging")) {
                    new DamagingRecipeHUD(player);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Healing")) {
                    new HealingRecipeHUD(player);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Familiar")) {
                    new FamiliarRecipeHUD(player);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                }
            } else if (coreInventory.getName().equalsIgnoreCase("Spell of Damaging") ||
                    coreInventory.getName().equalsIgnoreCase("Spell of Healing") ||
                    coreInventory.getName().equalsIgnoreCase("Spell of Familiar")) {
                assert clickedItem != null;
                if (Wands.isWand(clickedItem)) {
                    new WandRecipeHUD(player);
                }
                event.setCancelled(true);
            }
            event.setCancelled(true);
        }
    }

    public boolean invalidClick() {
        return (event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty();
    }
}
