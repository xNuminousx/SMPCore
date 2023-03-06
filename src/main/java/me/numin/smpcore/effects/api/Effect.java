package me.numin.smpcore.effects.api;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.database.Database;
import me.numin.smpcore.effects.EnderEffect;
import me.numin.smpcore.effects.RainbowEffect;
import me.numin.smpcore.effects.RedstoneEffect;
import me.numin.smpcore.database.PlayerStats;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Effect implements PlayerEffect {

    public Effect(Player player) {
        SMPCore.effects.removeIf(effect -> effect.getPlayer().getUniqueId() == player.getUniqueId());
        SMPCore.effects.add(this);
        //SMPCore.plugin.data.addEffect(player, this);

        //SAVE TO THE DATABASE
        try {
            Database database = SMPCore.plugin.getDatabase();
            PlayerStats playerStats = database.getPlayerStatsByUUID(player.getUniqueId());

            if (playerStats == null)
                new PlayerStats(player.getUniqueId(), this.getName(), 0, 0, 0, SMPCore.plugin.getPlayerStatsCache());
            else {
                playerStats.setEffect(this.getName());
                database.updatePlayerStats(playerStats);
            }
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to save an effect for player: " + player.getName());
            e.printStackTrace();
        }

        player.sendMessage("You now have the " + this.getName() + " effect!");
    }

    public static void remove(Player player) {
        Set<Effect> effectsToRemove = SMPCore.effects.stream()
                .filter(effect -> effect.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .collect(Collectors.toSet());
        SMPCore.effects.removeAll(effectsToRemove);

        if (!effectsToRemove.isEmpty()) {
             try {
                 Database database = SMPCore.plugin.getDatabase();
                 PlayerStats playerStats = database.getPlayerStatsByUUID(player.getUniqueId());

                 if (playerStats != null) {
                     effectsToRemove.forEach(effect -> playerStats.setEffect(""));
                     database.updatePlayerStats(playerStats);
                 }
             } catch (SQLException e) {
                 SMPCore.plugin.getLogger().info("Failed to remove an effect from player: " + player.getName());
                 e.printStackTrace();
             }

            effectsToRemove.forEach(effect ->
                    effect.getPlayer().sendMessage("Your " + effect.getName() + " effect has been removed."));
        } else player.sendMessage("You do not have an effect.");
    }

    public static void initializeEffect(Player player, String effect) {
        if (effect.equalsIgnoreCase("ender"))
            new EnderEffect(player);
        else if (effect.equalsIgnoreCase("rainbow"))
            new RainbowEffect(player);
        else if (effect.equalsIgnoreCase("colored dust"))
            new RedstoneEffect(player, Color.RED);
    }
}
