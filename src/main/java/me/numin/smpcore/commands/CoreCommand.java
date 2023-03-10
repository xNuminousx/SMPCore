package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.CoreHUD;
import me.numin.smpcore.database.PlayerStats;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class CoreCommand {

    public CoreCommand(Player player, String[] args) {

        if (args.length < 1) {
            new CoreHUD(player);
        } else {
            if (args[0].equalsIgnoreCase("get")) {
                for (Effect effect : SMPCore.effects) {
                    player.sendMessage(effect.getPlayer().getName() +  ": " + effect.getName());
                }
            } else if (args[0].equalsIgnoreCase("blocks")) {
                SMPCore plugin = SMPCore.plugin;
                try {
                    PlayerStats playerStats = plugin.getPlayerStatsCache().getPlayerStats(player.getUniqueId());
                    long blocksBroken = playerStats == null ? 0 : playerStats.getBlocksBroken();
                    player.sendMessage("Blocks Broken: " + blocksBroken);
                } catch (SQLException e) {
                    plugin.getLogger().info("Unable to get number of blocks broken for player: " + player.getName());
                    e.printStackTrace();
                }
            }
        }
    }
}
