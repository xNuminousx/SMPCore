package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.game.Game;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

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

            if (itemName.equalsIgnoreCase("Start a Game")) {
                if (!SMPCore.games.isEmpty()) {
                    player.sendMessage("There is already an active game. Try /game join");
                    return;
                }

                player.sendMessage("Setting up game...");
                coreInventory.close();
                new Game(player);
            } else if (itemName.equalsIgnoreCase("Join Current Game")) {
                for (Game game : SMPCore.games) {
                    game.addPlayer(player);
                    return;
                }
                player.sendMessage("There are no active games to join. Try /game start");
            } else if (itemName.equalsIgnoreCase("Stop a Game")) {
                if (SMPCore.games.isEmpty()) {
                    player.sendMessage("There are no games to stop.");
                }
                List<Game> toStop = new ArrayList<>();
                for (Game game : SMPCore.games) {
                    if (game.getHost().getUniqueId().equals(player.getUniqueId()) || SMPCore.staff.contains(player.getName())) {
                        player.sendMessage("Stopping the game...");
                        toStop.add(game);
                    } else {
                        player.sendMessage("You are not the host of the game, so you cannot force stop it.");
                    }
                }
                for (Game game : toStop)
                    game.stop();

                toStop.clear();
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();

        for (Game game : SMPCore.games) {
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
