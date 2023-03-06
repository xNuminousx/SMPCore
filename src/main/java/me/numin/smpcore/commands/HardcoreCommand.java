package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HardcoreCommand {

    public HardcoreCommand(Player player, String[] args) {
//        if (!SMPCore.staff.contains(player.getName())) {
//            player.sendMessage("You do not have access to this command!");
//            return;
//        }
//
//        if (args.length < 1) {
//            player.sendMessage("/hardcore ban [player]");
//            player.sendMessage("/hardcore unban [player]");
//            player.sendMessage("/hardcore save");
//        } else if (args.length == 1) {
//            if (args[0].equalsIgnoreCase("save")) {
//                SMPCore.plugin.getHardcoreData().saveBannedPlayers();
//            }
//        } else {
//            String input = args[0];
//            String targetName = args[1];
//            Player targetPlayer = Bukkit.getPlayer(targetName);
//
//            if (targetPlayer == null) {
//                player.sendMessage("That player does not exist.");
//                return;
//            }
//
//            List<UUID> bannedPlayers = SMPCore.plugin.getHardcoreData().bannedPlayers;
//            UUID targetUUID = targetPlayer.getUniqueId();
//
//            if (input.equalsIgnoreCase("ban")) {
//                if (bannedPlayers.contains(targetUUID)) {
//                    player.sendMessage("That player is already banned.");
//                    return;
//                }
//
//                SMPCore.plugin.getHardcoreData().banPlayer(targetPlayer);
//
//                if (targetPlayer.getWorld().getName().equalsIgnoreCase("hardcore")) {
//                    targetPlayer.teleport(SMPCore.plugin.getSpawn());
//                    targetPlayer.sendMessage("You have been banned from the hardcore world.");
//                }
//
//                player.sendMessage("You have banned " + targetName + " from the hardcore world.");
//            } else if (input.equalsIgnoreCase("unban")) {
//                if (!bannedPlayers.contains(targetUUID)) {
//                    player.sendMessage("That player is not banned.");
//                    return;
//                }
//
//                SMPCore.plugin.getHardcoreData().unbanPlayer(targetPlayer);
//                player.sendMessage("You have unbanned " + targetName + " from the hardcore world.");
//            }
//        }
    }
}
