package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsCache {

    private final HashMap<UUID, PlayerStats> playerStatsMap = new HashMap<>();
    private final Database database;

    public PlayerStatsCache(Database database) {
        this.database = database;
    }

    public PlayerStats getPlayerStats(UUID uuid) throws SQLException {
        PlayerStats playerStats = playerStatsMap.get(uuid);
        if (playerStats == null) {
            playerStats = database.getPlayerStatsByUUID(uuid);
            if (playerStats != null) {
                playerStatsMap.put(uuid, playerStats);
            }
        }
        return playerStats;
    }

    public void loadMap() {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM player_stats");
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                UUID uuid = UUID.fromString(results.getString("uuid"));
                String effect = results.getString("effect");
                int wins = results.getInt("wins");
                int loses = results.getInt("loses");
                long blocksBroken = results.getLong("blocks_broken");

                new PlayerStats(uuid, effect, wins, loses, blocksBroken, this);
            }
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to register player stats.");
            e.printStackTrace();
        }
    }

    public HashMap<UUID, PlayerStats> getPlayerStatsMap() {
        return playerStatsMap;
    }
}
