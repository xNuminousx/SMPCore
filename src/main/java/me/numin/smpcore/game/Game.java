package me.numin.smpcore.game;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.files.GameData;
import me.numin.smpcore.utils.CustomFirework;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private List<Player> players = new ArrayList<>();
    private Player host;
    private String name;

    private boolean doRespawn;
    private long duration = 600000; // If no time is defined, each game is 10 minutes long.
    private long startTime;

    public Game(Player host) {
        this.host = host;
        this.name = "PvP";
        this.doRespawn = true;
        SMPCore.games.add(this);
        start();
    }

    public Game(Player host, String name) {
        this.host = host;
        this.name = name;
        this.doRespawn = true;
        SMPCore.games.add(this);
        start();
    }

    public Game(Player host, String name, long duration, boolean doRespawn) {
        this.host = host;
        this.name = name;
        this.duration = duration * 1000;
        this.doRespawn = doRespawn;
        SMPCore.games.add(this);
        start();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        addPlayer(host);
        new CustomFirework(getCenter(), 100, Color.GREEN, FireworkEffect.Type.BURST, true, true);
    }

    public void stop() {
        List<Player> toLeave = new ArrayList<>(getPlayers());
        for (Player player : toLeave) {
            leaveGame(player);
        }
        clearPlayers();
        new CustomFirework(getCenter(), 100, Color.RED, FireworkEffect.Type.BURST, true, true);

        SMPCore.games.remove(this);
    }

    public boolean doRespawn() {
        return doRespawn;
    }

    public void respawn(Player player) {
        player.teleport(getRandomPoint());
    }

    public long getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeRemaining() {
        return ((startTime + duration) - System.currentTimeMillis()) / 1000;
    }

    public String getTimeRemainingAsString() {
        String constructor;

        float timeRemaining = (float) getTimeRemaining() / 60;
        int minute = Math.round(timeRemaining) - 1;
        int seconds = (int) (getTimeRemaining() % 60);

        if (minute >= 1)
            constructor = minute + "m " + seconds + "s";
        else
            constructor = getTimeRemaining() + "s";

        return constructor;
    }

    public Player getHost() {
        return host;
    }

    public void addPlayer(Player player) {
        if (getPlayers().contains(player))
            return;

        player.sendMessage("You've joined the game!");
        getPlayers().add(player);
        if (getSpawnPoints().isEmpty()) {
            player.sendMessage("There are no spawn points setup. Could not teleport you into the game.");
        } else {
            player.teleport(getRandomPoint());
            new CustomFirework(player, 100, Color.YELLOW, FireworkEffect.Type.BALL, true, true);
        }
    }

    public void clearPlayers() {
        List<Player> deletionQueue = new ArrayList<>(getPlayers());
        for (Player player : deletionQueue)
            removePlayer(player);
        if (!getPlayers().isEmpty())
            getPlayers().clear();
    }

    public void removePlayer(Player player) {
        getPlayers().remove(player);
    }

    public void leaveGame(Player player) {
        Location spectate = SMPCore.plugin.getGameData().loadPoint("spectate");

        new CustomFirework(player, 100, Color.YELLOW, FireworkEffect.Type.BALL, true, true);

        if (spectate == null) {
            player.sendMessage("You have left the game. Please use /spawn to exit the arena.");
        } else {
            player.sendMessage("You have left the game.");
            player.teleport(spectate);
        }
        removePlayer(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Location> getSpawnPoints() {
        List<Location> points = new ArrayList<>();
        GameData gameData = SMPCore.plugin.getGameData();
        for (String name : gameData.getPoints()) {
            if (name.equalsIgnoreCase("spectate") || name.equalsIgnoreCase("center"))
                continue;
            Location point = gameData.loadPoint(name);
            points.add(point);
        }
        return points;
    }

    public Location getCenter() {
        GameData gameData = SMPCore.plugin.getGameData();
        for (String name : gameData.getPoints()) {
            if (name.equalsIgnoreCase("center")) {
                return gameData.loadPoint(name);
            }
        }
        return null;
    }

    public Location getRandomPoint() {
        int spawnPointQty = getSpawnPoints().size();
        int index = new Random().nextInt(spawnPointQty);
        return getSpawnPoints().get(index);
    }
}
