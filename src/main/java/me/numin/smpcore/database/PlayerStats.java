package me.numin.smpcore.database;

import java.util.UUID;

public class PlayerStats {

    private final PlayerStatsCache cache;

    private final UUID uuid;
    private String effect;
    private int wins;
    private int loses;
    private long blocksBroken;

    //FIXME: Fix the chosen color of RedstoneEffect not saving in the database

    public PlayerStats(UUID uuid, String effect, int wins, int loses, long blocksBroken, PlayerStatsCache cache) {
        this.uuid = uuid;
        this.effect = effect;
        this.wins = wins;
        this.loses = loses;
        this.blocksBroken = blocksBroken;
        this.cache = cache;

        getCache().getPlayerStatsMap().put(uuid, this);
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public long getBlocksBroken() {
        return blocksBroken;
    }

    public void setBlocksBroken(long blocksBroken) {
        this.blocksBroken = blocksBroken;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerStatsCache getCache() {
        return cache;
    }
}
