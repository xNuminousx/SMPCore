package me.numin.smpcore.game;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.files.GameData;
import me.numin.smpcore.game.api.Game;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.Iterator;

//TODO: Make *pretty*

public class MobBattle extends Game {

    ArrayList<Entity> mobs = new ArrayList<>();
    ArrayList<Location> mobSpawnPoints;

    boolean forceStartNewWave, spawnMobs, startGracePeriod, waveStarted;
    int wave;
    long gracePeriodDuration = 20000; // 20 seconds
    long gracePeriodStartTime;
    long waveDuration = 60000; // 1 minute
    long waveStartTime;

    public MobBattle(Player host, String name, long duration, boolean doRespawn) {
        super(host, name, duration, doRespawn);

        mobSpawnPoints = getMobSpawnPoints();
        this.forceStartNewWave = false;
        this.spawnMobs = false;
        this.startGracePeriod = true;
        this.waveStarted = false;
        this.gracePeriodStartTime = System.currentTimeMillis();
        this.wave = 1;
    }

    @Override
    public void setup() {
        addPlayer(getHost());
    }

    @Override
    public void stop() {
        for (Entity mob : mobs)
            mob.remove();

        for (Player player : getPlayers())
            player.teleport(getSpectate());

        getPlayers().clear();
        games.remove(this);
    }

    public void run() {
        // END WAVE, START GRACE PERIOD
        if (forceStartNewWave || (waveStarted && (waveStartTime + waveDuration) <= System.currentTimeMillis())) {
            // CLEAN UP MOBS
            Iterator<Entity> iterator = mobs.iterator();
            while (iterator.hasNext()) {
                Entity mob = iterator.next();
                mob.remove();
                iterator.remove();
            }

            gracePeriodStartTime = System.currentTimeMillis();
            wave++;
            forceStartNewWave = false;
            startGracePeriod = true;
            waveStarted = false;
            for (Player player : getPlayers())
                player.sendMessage("New wave starting! You have " + gracePeriodDuration / 1000 + "s to prepare!");
        }

        // END GRACE PERIOD
        if (startGracePeriod) {
            if ((gracePeriodStartTime + gracePeriodDuration) <= System.currentTimeMillis()) {
                startGracePeriod = false;
                spawnMobs = true;
                waveStarted = true;
                waveStartTime = System.currentTimeMillis();
                for (Player player : getPlayers())
                    player.sendMessage("Grace period has ended. Now starting Wave #" + wave + ".");
            }

        // PROGRESS WAVE
        } else {
            if (spawnMobs) {
                for (Location point : mobSpawnPoints) {
                    for (int i = 0; i < wave; i++) {
                        Entity mob = point.getWorld().spawn(point, Zombie.class);
                        mobs.add(mob);
                    }
                }
                spawnMobs = false;
            }

            if (mobs.isEmpty()) forceStartNewWave = true;
        }
    }

    public int getWave() {
        return wave;
    }

    public long getGracePeriodDuration() {
        return gracePeriodDuration;
    }

    public long getWaveDuration() {
        return waveDuration;
    }

    public long getWaveStartTime() {
        return waveStartTime;
    }

    public ArrayList<Entity> getMobs() {
        return mobs;
    }

    public ArrayList<Location> getMobSpawnPoints() {
        ArrayList<Location> points = new ArrayList<>();
        GameData gameData = SMPCore.plugin.getGameData();

        for (String name : gameData.getPoints(getName()).keySet()) {
            if (name.contains("mobspawn"))
                points.add(gameData.loadPoint(getName(), name));
        }
        return points;
    }
}
