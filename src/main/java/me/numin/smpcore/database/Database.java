package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.HashMap;
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

        // PLAYER TABLE
        String playerTable = "CREATE TABLE IF NOT EXISTS player_stats(`uuid` varchar(36) primary key, `effect` varchar(16), `wins` int, `loses` int, `blocks_broken` long)";
        statement.execute(playerTable);

        // REPORT TABLE
        String reportTable = "CREATE TABLE IF NOT EXISTS reports(`identifier` varchar(5) primary key, `title` varchar(16), `author` varchar(36), `report_type` varchar(16), `date` varchar(30), `message` varchar(200))";
        statement.execute(reportTable);

        statement.close();
    }

    public Connection getConnection() throws SQLException {
        if (connection != null)
            return connection;

        //String url = "jdbc:mysql://localhost/smp_stats";
        String url = "jdbc:mysql://u5099_ylLQ95oIG8:We6qt6qN0zccre!QAAvTcyZ!@paneldatabase.humbleservers.com:3306/s5099_stats";
        String user = "root";
        String password = "";

        this.connection = DriverManager.getConnection(url, user, password);
        SMPCore.plugin.getLogger().info("Connected to the SMP database.");

        return connection;
    }

    public Report getReport(String identifer) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM reports WHERE identifier = ?");
        statement.setString(1, identifer);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            int identifier = results.getInt("identifier");
            String title = results.getString("title");
            String uuid = results.getString("author");
            OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            ReportType reportType = ReportType.getReportTypeFromString(results.getString("report_type"));
            String date = results.getString("date");
            String message = results.getString("message");
            statement.close();
            return new Report(identifier, author, title, reportType, date, message);
        }
        statement.close();
        return null;
    }

    public void deleteReport(Report report) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM reports WHERE identifier = ?");
            statement.setInt(1, report.getIdentifier());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to delete " + report.getTitle() + " from reports.");
            e.printStackTrace();
        }
    }

    public void loadReports() {
        SMPCore.plugin.getLogger().info("Loading reports.");
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM reports");
            ResultSet results = statement.executeQuery();

            if (results == null) {
                SMPCore.plugin.getLogger().info("No reports found.");
                return;
            }

            while (results.next()) {
                int identifier = results.getInt("identifier");
                String title = results.getString("title");
                String uuid = results.getString("author");
                OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                ReportType reportType = ReportType.getReportTypeFromString(results.getString("report_type"));
                String date = results.getString("date");
                String message = results.getString("message");

                new Report(identifier, author, title, reportType, date, message);
            }
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to load saved reports.");
            e.printStackTrace();
        }
    }

    public void saveReport(Report report) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT IGNORE INTO reports(identifier, title, author, report_type, date, message) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setInt(1, report.getIdentifier());
            statement.setString(2, report.getTitle());
            statement.setString(3, report.getAuthor().getUniqueId().toString());
            statement.setString(4, report.getReportType().toString());
            statement.setString(5, report.getDate());
            statement.setString(6, report.getMessage());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to save a report to the database.");
            e.printStackTrace();
        }
    }

    public PlayerStats getPlayerStatsByUUID(UUID uuid) throws SQLException {
        PlayerStatsCache playerStatsCache = SMPCore.plugin.getPlayerStatsCache();

        if (playerStatsCache.getPlayerStatsMap().containsKey(uuid))
            return playerStatsCache.getPlayerStats(uuid);

        String sUUID = uuid.toString();
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, sUUID);
        ResultSet results = statement.executeQuery();

        PlayerStats playerStats = null;
        if (results.next()) {
            String effect = results.getString("effect");
            int wins = results.getInt("wins");
            int loses = results.getInt("loses");
            long blocksBroken = results.getLong("blocks_broken");
            playerStats =  new PlayerStats(uuid, effect, wins, loses, blocksBroken, SMPCore.plugin.getPlayerStatsCache());
        }
        statement.close();
        return playerStats;
    }
    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        HashMap<UUID, PlayerStats> playerStatsMap = SMPCore.plugin.getPlayerStatsCache().getPlayerStatsMap();
        UUID uuid = playerStats.getUuid();

        if (playerStatsMap.containsKey(uuid)) {
            playerStatsMap.put(uuid, playerStats);
            return;
        }
        injectPlayerStats(playerStats);
    }

    public void injectPlayerStats(PlayerStats playerStats) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO player_stats(uuid, effect, wins, loses, blocks_broken) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "effect = VALUES(effect), " +
                    "wins = VALUES(wins), " +
                    "loses = VALUES(loses), " +
                    "blocks_broken = VALUES(blocks_broken)");
            statement.setString(1, playerStats.getUuid().toString());
            statement.setString(2, playerStats.getEffect());
            statement.setInt(3, playerStats.getWins());
            statement.setInt(4, playerStats.getLoses());
            statement.setLong(5, playerStats.getBlocksBroken());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Unable to update player stats for " + Bukkit.getPlayer(playerStats.getUuid()));
            e.printStackTrace();
        }
    }
}
