package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;

import java.util.UUID;

public class ServerPlayer {

    private String effect;
    private UUID uuid;

    private int mobsKilled;
    private int wavesLasted;

    public ServerPlayer(UUID uuid, String effect, int mobsKilled, int wavesLasted) {
        this.uuid = uuid;
        this.effect = effect;
        this.mobsKilled = mobsKilled;
        this.wavesLasted = wavesLasted;

        Database database = SMPCore.plugin.getDatabase();
        if (database != null)
            database.getPlayerData().getServerPlayers().put(uuid, this);
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getMobsKilled() {
        return mobsKilled;
    }

    public void setMobsKilled(int mobsKilled) {
        this.mobsKilled = mobsKilled;
    }

    public int getWavesLasted() {
        return wavesLasted;
    }

    public void setWavesLasted(int wavesLasted) {
        this.wavesLasted = wavesLasted;
    }
}
