package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
import me.numin.smpcore.utils.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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

        // PLAYER TABLE
        String playerTable = "CREATE TABLE IF NOT EXISTS player_stats(`uuid` varchar(36) primary key, `effect` varchar(16), `wins` int, `loses` int, `blocks_broken` long)";
        statement.execute(playerTable);

        // REPORT TABLE
        String reportTable = "CREATE TABLE IF NOT EXISTS reports(`title` varchar(16) primary key, `author` varchar(36), `report_type` varchar(16), `date` varchar(30), `message` varchar(200))";
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

    public Report getReportByTitle(String title) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM reports WHERE title = ?");
        statement.setString(1, title);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String uuid = results.getString("author");
            OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            ReportType reportType = ReportType.getReportTypeFromString(results.getString("report_type"));
            String date = results.getString("date");
            String message = results.getString("message");
            statement.close();
            return new Report(author, title, reportType, date, message);
        }
        statement.close();
        return null;
    }

    public void deleteReport(Report report) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM reports WHERE title = ?");
            statement.setString(1, report.getTitle());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to delete " + report.getTitle() + " from reports.");
            e.printStackTrace();
        }
    }

    public void loadReports() {
        SMPCore.plugin.getLogger().info("Loading reports...");
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM reports");
            ResultSet results = statement.executeQuery();

            if (results == null) {
                SMPCore.plugin.getLogger().info("No reports found.");
                return;
            }

            while (results.next()) {
                String title = results.getString("title");
                String uuid = results.getString("author");
                OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                ReportType reportType = ReportType.getReportTypeFromString(results.getString("report_type"));
                String date = results.getString("date");
                String message = results.getString("message");

                new Report (author, title, reportType, date, message);
            }
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to load saved reports.");
            e.printStackTrace();
        }
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

    public void saveReport(Report report) {
        try {
            if (getReportByTitle(report.getTitle()) != null)
                return;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SMPCore.plugin.getLogger().info("Saving a report...");
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO reports(title, author, report_type, date, message) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, report.getTitle());
            statement.setString(2, report.getAuthor().getUniqueId().toString());
            statement.setString(3, report.getReportType().toString());
            statement.setString(4, report.getDate());
            statement.setString(5, report.getMessage());
            statement.executeUpdate();
            statement.close();
            SMPCore.plugin.getLogger().info("Report saved.");
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Failed to save a report to the database.");
            e.printStackTrace();
        }
    }


    public void createPlayerStats(PlayerStats playerStats) throws SQLException {
        try {
            SMPCore.plugin.getLogger().info("Creating new player tracker for " + Bukkit.getPlayer(playerStats.getUuid()));
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO player_stats(uuid, effect, wins, loses, blocks_broken) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, playerStats.getUuid().toString());
            statement.setString(2, playerStats.getEffect());
            statement.setInt(3, playerStats.getWins());
            statement.setInt(4, playerStats.getLoses());
            statement.setLong(5, playerStats.getBlocksBroken());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Unable to save stats for " + Bukkit.getPlayer(playerStats.getUuid()));
            e.printStackTrace();
        }
    }

    public void updatePlayerStats(PlayerStats playerStats) throws SQLException {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE player_stats SET effect = ?, wins = ?, loses = ?, blocks_broken = ? WHERE uuid = ?");
            statement.setString(1, playerStats.getEffect());
            statement.setInt(2, playerStats.getWins());
            statement.setInt(3, playerStats.getLoses());
            statement.setLong(4, playerStats.getBlocksBroken());
            statement.setString(5, playerStats.getUuid().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            SMPCore.plugin.getLogger().info("Unable to update player stats for " + Bukkit.getPlayer(playerStats.getUuid()));
            e.printStackTrace();
        }
    }
}
