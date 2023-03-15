package me.numin.smpcore.effects.api;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.database.ServerPlayer;
import me.numin.smpcore.effects.EnderEffect;
import me.numin.smpcore.effects.RainbowEffect;
import me.numin.smpcore.effects.RedstoneEffect;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class Effect implements PlayerEffect {

    public Effect(Player player) {
        SMPCore.effects.removeIf(effect -> effect.getPlayer().getUniqueId() == player.getUniqueId());
        SMPCore.effects.add(this);

        ServerPlayer sPlayer = SMPCore.plugin.getDatabase().getPlayerData().getServerPlayer(player.getUniqueId());
        if (sPlayer == null) {
            new ServerPlayer(player.getUniqueId(), this.getName(), 0, 0);
        } else {
            sPlayer.setEffect(this.getName());
        }
        player.sendMessage("You now have the " + this.getName() + " effect!");
    }

    public static void remove(Player player) {
        Set<Effect> effectsToRemove = SMPCore.effects.stream()
                .filter(effect -> effect.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .collect(Collectors.toSet());
        SMPCore.effects.removeAll(effectsToRemove);

        if (!effectsToRemove.isEmpty()) {
            ServerPlayer sPlayer = SMPCore.plugin.getDatabase().getPlayerData().getServerPlayer(player.getUniqueId());

            if (sPlayer != null)
                sPlayer.setEffect("");

            effectsToRemove.forEach(effect ->
                    effect.getPlayer().sendMessage("Your " + effect.getName() + " effect has been removed."));
        } else player.sendMessage("You do not have an effect.");
    }

    public static void initializeEffect(Player player, String effect) {
        //TODO: Add customized color support for dust trail
        if (effect.equalsIgnoreCase("ender"))
            new EnderEffect(player);
        else if (effect.equalsIgnoreCase("rainbow"))
            new RainbowEffect(player);
        else if (effect.equalsIgnoreCase("colored dust"))
            new RedstoneEffect(player, Color.RED);
    }
}
