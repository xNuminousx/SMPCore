package me.numin.smpcore.game.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class GameManager implements Runnable {

    @Override
    public void run() {
        for (Game game : Game.games) {
            if (game.getPlayers() == null)
                continue;

            if (game.getPlayers().isEmpty() || game.getTimeRemaining() == 0) {
                SMPCore.plugin.getLogger().info("Force stopping a game.");
                game.stop();
                return;
            }

            Location center = game.getCenter();
            Iterator<Player> players = game.getPlayers().iterator();
            while (players.hasNext()) {
                Player player = players.next();
                if (!player.isOnline()) {
                    players.remove();
                    continue;
                }

                if (game.getTimeRemaining() == 10000) {
                    player.sendMessage("There are 10 seconds remaining in the game!");
                }

                if (player.getWorld() != center.getWorld() || player.getLocation().distance(center) > 30) {
                    player.sendMessage("You are out of range of the game. Exiting queue.");
                    players.remove();
                }
                game.run();
            }
        }
    }
}