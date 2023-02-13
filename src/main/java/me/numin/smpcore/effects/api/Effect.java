package me.numin.smpcore.effects.api;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.database.Database;
import me.numin.smpcore.effects.EnderEffect;
import me.numin.smpcore.effects.RainbowEffect;
import me.numin.smpcore.effects.RedstoneEffect;
import me.numin.smpcore.utils.PlayerStats;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

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
                database.createPlayerStats(new PlayerStats(player.getUniqueId(), this.getName(), 0, 0, 0));
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
        ArrayList<Effect> deletionQueue = new ArrayList<>();
        for (Effect effect : SMPCore.effects) {
            if (effect.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                deletionQueue.add(effect);
            }
        }

        if (!deletionQueue.isEmpty()) {
            for (Effect effect : deletionQueue) {
                SMPCore.effects.remove(effect);
                effect.getPlayer().sendMessage("Your " + effect.getName() + " effect has been removed.");

                //REMOVE FROM THE DATABASE
                try {
                    Database database = SMPCore.plugin.getDatabase();
                    PlayerStats playerStats = database.getPlayerStatsByUUID(player.getUniqueId());

                    if (playerStats != null) {
                        playerStats.setEffect("");
                        database.updatePlayerStats(playerStats);
                    }
                } catch (SQLException e) {
                    SMPCore.plugin.getLogger().info("Failed to remove an effect from player: " + player.getName());
                    e.printStackTrace();
                }
            }
            deletionQueue.clear();
        } else {
            player.sendMessage("You do not have an effect.");
        }
    }

    public static void initializeEffect(Player player, String effect) {
        if (effect.equalsIgnoreCase("ender"))
            new EnderEffect(player);
        else if (effect.equalsIgnoreCase("rainbow"))
            new RainbowEffect(player);
        else if (effect.equalsIgnoreCase("redstone"))
            new RedstoneEffect(player);
    }
}
