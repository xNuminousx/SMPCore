package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.utils.PlayerStats;

import java.sql.*;
import java.util.UUID;

public class Database {

    Connection connection;

    public Database() {
        try {
            createStatTable();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to connect to the SMP database.");
            e.printStackTrace();
        }
    }

    public void createStatTable() throws SQLException {
        Statement statement = getConnection().createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS player_stats(`uuid` varchar(36) primary key, `effect` varchar(16), `wins` int, `loses` int, `blocks_broken` long)";
        statement.execute(sql);
        statement.close();
        SMPCore.plugin.getLogger().info("Created a stats table in the SMP database.");
    }

    public Connection getConnection() throws SQLException {
        if (connection != null)
            return connection;

        String url = "jdbc:mysql://u5099_ylLQ95oIG8:We6qt6qN0zccre!QAAvTcyZ!@paneldatabase.humbleservers.com:3306/s5099_stats";
        String user = "root";
        String password = "";

        this.connection = DriverManager.getConnection(url, user, password);
        SMPCore.plugin.getLogger().info("Connected to the SMP database.");

        return connection;
    }

    public PlayerStats getPlayerStatsByUUID(UUID uuid) throws SQLException {
        String sUUID = uuid.toString();

        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, sUUID);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String effect = results.getString("effect");
            int wins = results.getInt("wins");
            int loses = results.getInt("loses");
            long blocksBroken = results.getLong("blocks_broken");
            statement.close();
            return new PlayerStats(uuid, effect, wins, loses, blocksBroken);
        }
        statement.close();
        return null;
    }

    public void createPlayerStats(PlayerStats playerStats) throws SQLException {
        SMPCore.plugin.getLogger().info("Creating new player stat tracker...");
        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO player_stats(uuid, effect, wins, loses, blocks_broken) VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, playerStats.getUuid().toString());
        statement.setString(2, playerStats.getEffect());
        statement.setInt(3, playerStats.getWins());
        statement.setInt(4, playerStats.getLoses());
        statement.setLong(5, playerStats.getBlocksBroken());
        statement.executeUpdate();
        statement.close();
    }

    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("UPDATE player_stats SET effect = ?, wins = ?, loses = ?, blocks_broken = ? WHERE uuid = ?");
        statement.setString(1, playerStats.getEffect());
        statement.setInt(2, playerStats.getWins());
        statement.setInt(3, playerStats.getLoses());
        statement.setLong(4, playerStats.getBlocksBroken());
        statement.setString(5, playerStats.getUuid().toString());
        statement.executeUpdate();
        statement.close();
    }
}
