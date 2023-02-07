package me.numin.smpcore.listeners;

import me.numin.smpcore.spells.SpellOfDamaging;
import me.numin.smpcore.spells.SpellOfFamiliar;
import me.numin.smpcore.spells.SpellOfHealing;
import me.numin.smpcore.spells.api.Spell;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class SpellListener implements Listener {

    @EventHandler
    public void onSwing(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();

        if (!heldItem.getType().equals(Material.STICK)) {
            return;
        }

        if (!Objects.requireNonNull(heldItem.getItemMeta()).getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)) {
            return;
        }

        if (heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Damaging")) {
            useWand(Spell.SpellType.DAMAGING, player, heldItem);
        } else if (heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Familiar")) {
            useWand(Spell.SpellType.FAMILIAR, player, heldItem);
        } else if (heldItem.getItemMeta().getDisplayName().equalsIgnoreCase("Spell of Healing")) {
            useWand(Spell.SpellType.HEALING, player, heldItem);
        }
    }

    public void useWand(Spell.SpellType type, Player player, ItemStack wand) {
        if (type == Spell.SpellType.FAMILIAR) {
            new SpellOfFamiliar(player);
            return;
        }

        ItemMeta meta = wand.getItemMeta();
        List<String> lore = meta.getLore();
        int uses = Integer.parseInt(lore.get(1));

        if (uses == 0) {
            player.sendMessage("Your wand is out of spells! You must recharge it to get more.");
        } else {
            if (uses == 1)
                player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 0.5F, 1.5F);
            lore.set(1, Integer.toString(uses - 1));
            meta.setLore(lore);
            wand.setItemMeta(meta);

            if (type == Spell.SpellType.DAMAGING) {
                new SpellOfDamaging(player);
            } else if (type == Spell.SpellType.HEALING) {
                new SpellOfHealing(player);
            }
        }
    }
}
