package me.numin.smpcore.listeners;

import me.numin.smpcore.game.MobBattle;
import me.numin.smpcore.game.PvPGame;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class GameListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory.invalidClick(event)) {
                event.setCancelled(true);
                return;
            }

            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

            if (itemName.equalsIgnoreCase("PvP Battle")) {
                player.sendMessage("Setting up PvP game...");
                coreInventory.close();
                new PvPGame(player, "PvP", 600000, true); // 10 minutes

//                if (!SMPCore.games.isEmpty()) {
//                    player.sendMessage("There is already an active game. Try /game join");
//                    return;
//                }
//
//                player.sendMessage("Setting up game...");
//                coreInventory.close();
//                new LegacyGame(player);
            } else if (itemName.equalsIgnoreCase("Join Current Game")) {
                for (Game game : Game.games) {
                    game.addPlayer(player);
                    return;
                }
                player.sendMessage("There are no active games to join. Try /game start");
            } else if (itemName.equalsIgnoreCase("Mob Battle")) {
                player.sendMessage("Setting up Mob Battle...");
                coreInventory.close();
                new MobBattle(player, "MobBattle", 600000, true);

//                if (SMPCore.games.isEmpty()) {
//                    player.sendMessage("There are no games to stop.");
//                }
//                List<LegacyGame> toStop = new ArrayList<>();
//                for (LegacyGame game : SMPCore.games) {
//                    if (game.getHost().getUniqueId().equals(player.getUniqueId()) || SMPCore.staff.contains(player.getName())) {
//                        player.sendMessage("Stopping the game...");
//                        toStop.add(game);
//                    } else {
//                        player.sendMessage("You are not the host of the game, so you cannot force stop it.");
//                    }
//                }
//                for (LegacyGame game : toStop)
//                    game.stop();
//
//                toStop.clear();
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        for (Game game : Game.games) {
            if (game instanceof MobBattle) {
                MobBattle mobBattle = (MobBattle) game;
                Entity mob = event.getEntity();
                if (mobBattle.getMobs().contains(mob)) {
                    event.getDrops().clear();
                    mobBattle.getMobs().remove(mob);
                }

            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();

        for (Game game : Game.games) {
            if (game.getPlayers().contains(player)) {
                if (command.toLowerCase().contains("/game")) {
                    event.setCancelled(false);
                    return;
                }
                if (command.toLowerCase().contains("/core")) {
                    event.setCancelled(false);
                    return;
                }
                player.sendMessage("You must leave the game to do that. Use /game leave.");
                event.setCancelled(true);
                return;
            }
        }
    }
}
