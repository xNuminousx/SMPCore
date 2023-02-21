package me.numin.smpcore.game;

import me.numin.smpcore.game.api.Game;
import org.bukkit.entity.Player;

public class PvPGame extends Game {

    public PvPGame(Player host, String name, long duration, boolean doRespawn) {
        super(host, name, duration, doRespawn);
    }

    @Override
    public void setup() {
        addPlayer(getHost());
    }

    @Override
    public void stop() {
        for (Player player : getPlayers())
            player.teleport(getSpectate());
        getPlayers().clear();
        games.remove(this);
    }
}
