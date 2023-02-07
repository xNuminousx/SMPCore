package me.numin.smpcore.listeners;

import me.numin.smpcore.inventories.CoreHUD;
import me.numin.smpcore.inventories.PlayerLocationHUD;
import me.numin.smpcore.inventories.StaffReportManagerHUD;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class StaffListener implements Listener {

    private InventoryClickEvent event;

    @EventHandler
    public void hudClick(InventoryClickEvent event) {
        this.event = event;
        Player player = (Player)event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory.getName().equalsIgnoreCase("Staff Actions")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                } else if (itemName.equalsIgnoreCase("Manage Reports")) {
                    new StaffReportManagerHUD(player);
                } else if (itemName.equalsIgnoreCase("Player Locations")) {
                    new PlayerLocationHUD(player);
                }
                event.setCancelled(true);
            } else if (coreInventory.getName().equalsIgnoreCase("Player Locations")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    //return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                }

                event.setCancelled(true);
            }
        }
    }

    public boolean invalidClick() {
        return (event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty();
    }
}
