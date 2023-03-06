package me.numin.smpcore.game.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface IGame {
    void setup();
    void stop();
    void run();
    Location getCenter();
    Location getSpectate();
    Player getHost();
    String getName();
    boolean doRespawn();
    long getDuration();
    long getStartTime();
    long getTimeRemaining();
    ArrayList<Location> getPlayerSpawnPoints();
    ArrayList<Player> getPlayers();
}
