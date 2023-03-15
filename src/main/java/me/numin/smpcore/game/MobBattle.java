package me.numin.smpcore.game;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.database.ServerPlayer;
import me.numin.smpcore.files.GameData;
import me.numin.smpcore.game.api.BattleEntity;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.game.api.Wave;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MobBattle extends Game {

    //TODO: Make *pretty*
    //TODO: Track the number of waves a player has played for rewards

    public enum Difficulty {EASY, MEDIUM, HARD}

    private Difficulty difficulty;
    private final Wave wave;

    public MobBattle(Player host, long duration, boolean doRespawn) {
        super(host, duration, doRespawn);
        this.wave = new Wave(this, 5000); // 5-second grace periods
        this.difficulty = Difficulty.EASY;
    }

    @Override
    public void setup() {
        addPlayer(getHost());
    }

    @Override
    public void stop() {
        for (Player player : getPlayers()) {
            ServerPlayer sPlayer = SMPCore.plugin.getDatabase().getPlayerData().getServerPlayer(player.getUniqueId());

            if (sPlayer == null)
                continue;

            SMPCore.plugin.getDatabase().getPlayerData().insertServerPlayer(sPlayer);
        }
        wave.cleanUp();
        super.stop();
    }

    @Override
    public String getName() {
        return "MobBattle";
    }

    @Override
    public void run() {
        wave.progress();

        // stage 1-5 = easy, 6-10 = medium, 11+ = hard
        int stage = wave.getStage();
        if (stage > 10) difficulty = Difficulty.HARD;
        else if (stage > 5) difficulty = Difficulty.MEDIUM;
        else difficulty = Difficulty.EASY;
    }

    public Wave getWave() {
        return wave;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public HashMap<Entity, BattleEntity> getMobs() {
        if (wave.getMobs() == null || wave.getMobs().isEmpty())
            return null;

        return wave.getMobs();
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

    public Location getRandomMobSpawn() {
        ArrayList<Location> spawnPoints = getMobSpawnPoints();

        if (spawnPoints == null || spawnPoints.isEmpty())
            return null;

        int i = new Random().nextInt(getMobSpawnPoints().size());
        return getMobSpawnPoints().get(i);
    }
}
