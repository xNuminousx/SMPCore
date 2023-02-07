package me.numin.smpcore.listeners;

import me.numin.smpcore.effects.RainbowEffect;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.effects.EnderEffect;
import me.numin.smpcore.effects.RedstoneEffect;
import me.numin.smpcore.inventories.CoreHUD;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EffectListener implements Listener {

    private ArrayList<Effect> deletionQueue = new ArrayList<>();
    private InventoryClickEvent event;

    @EventHandler
    public void hudClick(InventoryClickEvent event) {
        this.event = event;
        Player player = (Player)event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory.getName().equalsIgnoreCase("Effects")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                    return;
                } else if (itemName.equalsIgnoreCase("Remove Effect")) {
                    Effect.remove(player);
                } else if (itemName.equalsIgnoreCase("Ender")) {
                    new EnderEffect(player);
                    player.sendMessage("You now have the Ender effect.");
                } else if (itemName.equalsIgnoreCase("Redstone")) {
                    new RedstoneEffect(player);
                    player.sendMessage("You now have the Redstone effect.");
                } else if (itemName.equalsIgnoreCase("Rainbow")) {
                    new RainbowEffect(player);
                    player.sendMessage("You now have the Rainbow effect.");
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
