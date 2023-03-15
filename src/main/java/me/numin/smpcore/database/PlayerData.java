package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    // STRUCTURE - uuid: active_effect, mobs_killed, waves_lasted

    private HashMap<UUID, ServerPlayer> serverPlayers = new HashMap<>();

    private Database database;
    private SMPCore plugin;

    public PlayerData(SMPCore plugin, Database database) {
        this.plugin = plugin;
        this.database = database;

        try {
            database.setupTable("CREATE TABLE IF NOT EXISTS " + getTableName() + "(`uuid` varchar(36) primary key, `active_effect` varchar(16), `mobs_killed` int, `waves_lasted` int)");
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to setup table for " + getTableName());
            e.printStackTrace();
        }

        loadMap();
    }

    public ServerPlayer getServerPlayer(UUID uuid) {
        ServerPlayer sPlayer = serverPlayers.get(uuid);
        if (sPlayer == null) {
            sPlayer = selectServerPlayer(uuid);

            if (sPlayer != null) {
                serverPlayers.put(uuid, sPlayer);
            }
        }
        return sPlayer;
    }

    public ServerPlayer selectServerPlayer(UUID uuid) {
        try {
            if (serverPlayers.containsKey(uuid)) {
                return serverPlayers.get(uuid);
            }

            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + getTableName() + " WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();

            ServerPlayer sPlayer = null;
            if (results.next()) {
                String effect = results.getString("active_effect");
                int mobsKilled = results.getInt("mobs_killed");
                int wavesLasted = results.getInt("waves_lasted");
                sPlayer = new ServerPlayer(uuid, effect, mobsKilled, wavesLasted);
            }
            statement.close();
            return sPlayer;
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to grab player information for " + uuid);
            e.printStackTrace();
        }
        return null;
    }

    public void insertServerPlayer(ServerPlayer sPlayer) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO " + getTableName() + "(uuid, active_effect, mobs_killed, waves_lasted) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "active_effect = VALUES(active_effect), " +
                    "mobs_killed = VALUES(mobs_killed), " +
                    "waves_lasted = VALUES(waves_lasted)");
            statement.setString(1, sPlayer.getUUID().toString());
            statement.setString(2, sPlayer.getEffect());
            statement.setInt(3, sPlayer.getMobsKilled());
            statement.setInt(4, sPlayer.getWavesLasted());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to insert player into the database for " + sPlayer.getUUID());
            e.printStackTrace();
        }
    }

    public void loadMap() {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + getTableName());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                UUID uuid = UUID.fromString(results.getString("uuid"));
                String effect = results.getString("active_effect");
                int mobsKilled = results.getInt("mobs_killed");
                int wavesLasted = results.getInt("waves_lasted");

                ServerPlayer sPlayer = new ServerPlayer(uuid, effect, mobsKilled, wavesLasted);
                serverPlayers.put(uuid, sPlayer);
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to load the server players map.");
            e.printStackTrace();
        }
    }

    public HashMap<UUID, ServerPlayer> getServerPlayers() {
        return serverPlayers;
    }

    public Database getDatabase() {
        return database;
    }

    public String getTableName() {
        return "player_data";
    }
}
