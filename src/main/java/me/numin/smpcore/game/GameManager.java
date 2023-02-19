package me.numin.smpcore.game;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.utils.CustomFirework;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements Runnable {

    private boolean timeNotif = false;

    @Override
    public void run() {
        for (Game game : SMPCore.games) {
            if (game.getPlayers().isEmpty()) {
                SMPCore.plugin.getLogger().info("A game has ended due to lack of players.");
                game.stop();
                return;
            }

            if (System.currentTimeMillis() > game.getStartTime() + game.getDuration()) {
                game.stop();
                return;
            }

            if (game.getTimeRemaining() == 5 && !timeNotif) {
                for (Player player : game.getPlayers()) {
                    player.sendMessage("There are 5 seconds remaining in the game.");
                }
                timeNotif = true;
            }

            for (CustomFirework customFirework : CustomFirework.fireworks) {
                if (customFirework.doTravel() && customFirework.getFirework().getLocation().distance(customFirework.getOrigin()) > 4) {
                    customFirework.getFirework().detonate();
                    customFirework.setDetonated(true);
                }
            }

            Location center = SMPCore.plugin.getGameData().loadPoint("center");
            List<Player> toLeave = new ArrayList<>();
            for (Player player : game.getPlayers()) {
                if (!player.isOnline()) {
                    toLeave.add(player);
                    return;
                }

                if (player.isDead()) {
                    if (game.doRespawn()) {
                        game.respawn(player);
                    } else {
                        toLeave.add(player);
                        return;
                    }
                }

                if (player.getLocation().distance(center) > 30) {
                    player.sendMessage("You have left the game.");
                    toLeave.add(player);
                }
            }
            for (Player player : toLeave)
                game.removePlayer(player);
            toLeave.clear();
        }
    }
}
