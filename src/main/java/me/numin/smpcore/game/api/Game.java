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
    private final Player host;

    boolean doRespawn;
    long duration;
    long startTime;

    public Game(Player host, long duration, boolean doRespawn) {
        this.host = host;
        this.doRespawn = doRespawn;
        this.duration = duration;
        this.startTime = System.currentTimeMillis();

        games.add(this);
        setup();
    }

    public void spawnPlayer(Player player) {
        if (getPlayerSpawnPoints() == null || getPlayerSpawnPoints().isEmpty()) {
            SMPCore.plugin.getLogger().warning("No player spawn points found for a game! Stopping the game...");
            stop();
            return;
        }

        int i = new Random().nextInt(getPlayerSpawnPoints().size());
        player.teleport(getPlayerSpawnPoints().get(i));
    }

    @Override
    public void stop() {
        if (getSpectate() == null)
            SMPCore.plugin.getLogger().warning("No defined `spectate` point found. Unable to teleport players.");
        else {
            for (Player player : getPlayers()) {
                player.teleport(getSpectate());
                player.sendMessage("The game has stopped.");
            }
        }
        getPlayers().clear();
        games.remove(this);
    }

    public static void stopAll() {
        for (Game game : games)
            game.stop();
    }

    @Override
    public Player getHost() {
        return host;
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
        return (getStartTime() + getDuration()) - System.currentTimeMillis();
    }

    public long getTimeRemainingInSeconds() {
        return getTimeRemaining() / 1000;
    }

    //FIXME: Not returning the correct numbers.
    public String getTimeRemainingAsString() {
        float timeRemaining = ((float) getTimeRemaining() / 60) / 1000;
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
        if (players == null || players.isEmpty())
            return players = new ArrayList<>();
        return players;
    }

    public void addPlayer(Player player) {
        getPlayers().add(player);
        spawnPlayer(player);
        player.sendMessage("You've joined the game!");
    }

    public void removePlayer(Player player) {
        if (!getPlayers().contains(player))
            return;

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
        SMPCore.plugin.getLogger().warning("No defined `center` point found. Stopping game...");
        stop();
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
    public ArrayList<Location> getPlayerSpawnPoints() {
        ArrayList<Location> points = new ArrayList<>();
        GameData gameData = SMPCore.plugin.getGameData();

        for (String name : gameData.getPoints(getName()).keySet()) {
            if (name.contains("playerspawn"))
                points.add(gameData.loadPoint(getName(), name));
        }
        return points;
    }
}
