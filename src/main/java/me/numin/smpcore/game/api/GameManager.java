package me.numin.smpcore.game.api;

import me.numin.smpcore.game.MobBattle;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class GameManager implements Runnable {

    boolean notifyTime = true;

    @Override
    public void run() {
        for (Game game : Game.games) {
            if (game.getPlayers().isEmpty() || game.getTimeRemaining() > game.getDuration()) {
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

                if (player.isDead()) {
                    if (game.doRespawn())
                        game.spawn(player);
                    else {
                        players.remove();
                        continue;
                    }
                }

                if (game.getTimeRemaining() == 10000 && notifyTime) {
                    player.sendMessage("There are 10 seconds remaining in the game!");
                    notifyTime = false;
                }

                if (player.getWorld() != center.getWorld() || player.getLocation().distance(center) > 30) {
                    player.sendMessage("You have left the game.");
                    players.remove();
                }

                if (game instanceof MobBattle) {
                    MobBattle mobBattle = (MobBattle) game;
                    mobBattle.run();
                }
            }
        }
    }
}