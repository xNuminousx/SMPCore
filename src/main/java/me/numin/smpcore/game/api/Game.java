package me.numin.smpcore.game.api;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.files.GameData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public abstract class Game implements IGame {

    public static ArrayList<Game> games = new ArrayList<>();

    private ArrayList<Player> players = new ArrayList<>();
    private Player host;
    private String name;

    boolean doRespawn;
    long duration;
    long startTime;

    public Game(Player host, String name, long duration, boolean doRespawn) {
        this.host = host;
        this.name = name;
        this.doRespawn = doRespawn;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();

        games.add(this);
        setup();
    }

    public void spawn(Player player) {
        int i = new Random().nextInt(getSpawnPoints().size());
        player.teleport(getSpawnPoints().get(i));
    }

    @Override
    public Player getHost() {
        return host;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean doRespawn() {
        return doRespawn;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getTimeRemaining() {
        return ((getStartTime() + getDuration()) - System.currentTimeMillis()) / 1000;
    }

    public String getTimeRemainingAsString() {
        float timeRemaining = (float) getTimeRemaining() / 60;
        int minute = Math.round(timeRemaining) - 1;
        int seconds = (int) (getTimeRemaining() % 60);

        if (minute >= 1) {
            return minute + "m " +
                    seconds + "s";
        } else {
            return getTimeRemaining() + "s";
        }
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if (getPlayers().contains(player))
            return;

        getPlayers().add(player);
        spawn(player);
        player.sendMessage("You've joined the game!");
    }

    public void removePlayer(Player player) {
        getPlayers().remove(player);
        player.sendMessage("You've left the game!");
    }

    @Override
    public Location getCenter() {
        GameData gameData = SMPCore.plugin.getGameData();
        for (String name : gameData.getPoints(getName()).keySet()) {
            if (name.equalsIgnoreCase("center")) {
                return gameData.loadPoint(getName(), name);
            }
        }
        return null;
    }

    @Override
    public Location getSpectate() {
        GameData gameData = SMPCore.plugin.getGameData();
        for (String name : gameData.getPoints(getName()).keySet()) {
            if (name.equalsIgnoreCase("spectate")) {
                return gameData.loadPoint(getName(), name);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Location> getSpawnPoints() {
        ArrayList<Location> points = new ArrayList<>();
        GameData gameData = SMPCore.plugin.getGameData();

        for (String name : gameData.getPoints(getName()).keySet()) {
            if (name.contains("playerspawn"))
                points.add(gameData.loadPoint(getName(), name));
        }
        return points;
    }
}
