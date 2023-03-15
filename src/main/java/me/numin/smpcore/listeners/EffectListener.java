package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.RainbowEffect;
import me.numin.smpcore.effects.RedstoneEffect;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.ColorSelectorHUD;
import me.numin.smpcore.inventories.CoreHUD;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class EffectListener implements Listener {

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
                } else if (itemName.equalsIgnoreCase("Colored Dust")) {
                    new ColorSelectorHUD(player);
                    return;
                }
                Effect.initializeEffect(player, itemName);
                event.setCancelled(true);
            }

            if (coreInventory.getName().equalsIgnoreCase("Color Selector")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                Color color;

                if (itemName.equalsIgnoreCase("Red")) color = Color.RED;
                else if (itemName.equalsIgnoreCase("Orange")) color = Color.ORANGE;
                else if (itemName.equalsIgnoreCase("Yellow")) color = Color.YELLOW;
                else if (itemName.equalsIgnoreCase("Green")) color = Color.GREEN;
                else if (itemName.equalsIgnoreCase("Blue")) color = Color.BLUE;
                else if (itemName.equalsIgnoreCase("Purple")) color = Color.PURPLE;
                else if (itemName.equalsIgnoreCase("Pink")) color = Color.FUCHSIA;
                else color = Color.BLACK;

                new RedstoneEffect(player, color);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null)
            return;

        for (Effect effect : SMPCore.effects) {
            if (effect instanceof RainbowEffect) {
                RainbowEffect rEffect = (RainbowEffect) effect;
                rEffect.setIsMoving(from.distance(to) > 0);
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
