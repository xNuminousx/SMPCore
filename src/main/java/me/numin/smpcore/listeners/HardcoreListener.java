package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;
import java.util.UUID;

//TODO: Finish hardcore law implementation

public class HardcoreListener implements Listener {

//    @EventHandler
//    public void onWorldChange(PlayerChangedWorldEvent event) {
//        Player player = event.getPlayer();
//        World to = player.getWorld();
//
//        if (!to.getName().equalsIgnoreCase("hardcore")) {
//            return;
//        }
//
//        List<UUID> bannedPlayers = SMPCore.plugin.getHardcoreData().getBannedPlayers();
//
//        if (bannedPlayers.contains(player.getUniqueId())) {
//            player.teleport(SMPCore.plugin.getSpawn());
//            player.sendMessage("You have lost the game. You cannot return.");
//        }
//    }
//
//    @EventHandler
//    public void onDeath(PlayerDeathEvent event) {
//        Player player = event.getEntity();
//
//        if (player.getWorld().getName().equalsIgnoreCase("hardcore")) {
//            SMPCore.plugin.getHardcoreData().banPlayer(player);
//        }
//    }
//
//    @EventHandler
//    public void onMove(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//
//        if (player.getWorld().getName().equalsIgnoreCase("hardcore") && SMPCore.plugin.getHardcoreData().getBannedPlayers().contains(player.getUniqueId())) {
//            player.teleport(SMPCore.plugin.getSpawn());
//        }
//    }
}
