package me.numin.smpcore.game.api;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.game.MobBattle;
import me.numin.smpcore.utils.Timer;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Wave {

    //TODO: Track waves a player has contributed to. Will be used for future reward system.

    private HashMap<Entity, BattleEntity> mobs = new HashMap<>();
    private MobBattle game;
    private Timer mobTimer;
    private Timer gracePeriod;

    private boolean inGracePeriod;
    private int mobsToSpawn; // number of mobs to spawn per interval during a wave
    private int spawns; // number of times to spawn mobs during a wave
    private int stage;
    private long delay;
    private long maxDuration;
    private long startTime;

    public Wave(MobBattle game, long delay) {
        this.game = game;
        this.delay = delay;

        this.inGracePeriod = true;
        this.spawns = 0; // controlling the spawn interval during a wave
        this.stage = 0; // automatically increased since a grace period is started on initialization
        this.maxDuration = 600000; // 10 minutes;
        this.mobsToSpawn = 3;

        this.gracePeriod = new Timer(delay);
        this.startTime = System.currentTimeMillis();
    }

    public void progress() {
        if (!inGracePeriod) {
            mobTimer.tick();
            spawnMobs();

            if (getMobsRemaining() <= 0 && spawns > 1) {
                inGracePeriod = true;
                gracePeriod = new Timer(delay);
                broadcast("Wave #" + stage + " finished. Grace period will last " + delay / 1000 + "s.");
            }
        } else {
            gracePeriod.tick();
            if (gracePeriod.isReached()) {
                inGracePeriod = false;
                stage++;
                spawns = 0;
                mobTimer = new Timer(5000);

                if (stage % 5 == 0) { // Every 5th wave, increase the number of mobs that spawn.
                    mobsToSpawn++;
                }

                broadcast("Grace period is over. Wave #" + stage + " starting now.");
            }
        }

        if (startTime + maxDuration < System.currentTimeMillis()) {
            cleanUp();
            inGracePeriod = true;
            gracePeriod = new Timer(delay);
        }
    }

    public void broadcast(String message) {
        for (Player player : game.getPlayers())
            player.sendMessage(message);
    }

    public void cleanUp() {
        if (mobs != null ) {
            for (BattleEntity mob : mobs.values())
                mob.getEntity().remove();
            mobs.clear();
        }
    }

    public void spawnMobs() {
        if (game.getMobSpawnPoints() == null || game.getMobSpawnPoints().isEmpty()) {
            SMPCore.plugin.getLogger().warning("No spawn points found for a Mob Battle! Stopping game...");
            game.stop();
            return;
        }

        if (mobTimer.isReached() && spawns < 4) {
            spawns++;

            for (int i = 0; i < mobsToSpawn + stage; i++) {
                Location point = game.getRandomMobSpawn();
                BattleEntity bEntity = new BattleEntity(game, point, !(stage == 1));
                mobs.put(bEntity.getEntity(), bEntity);
            }
        }
    }

    public HashMap<Entity, BattleEntity> getMobs() {
        return mobs;
    }

    public boolean isInGracePeriod() {
        return inGracePeriod;
    }

    public int getMobsRemaining() {
        int remaining = 0;
        for (BattleEntity mob : mobs.values()) {
            if (!mob.getEntity().isDead())
                remaining++;
        }
        return remaining;
    }

    public int getStage() {
        return stage;
    }

    public long getDelay() {
        return delay;
    }
}
