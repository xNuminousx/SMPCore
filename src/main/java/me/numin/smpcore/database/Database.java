package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.reporting.Report;

import java.sql.*;

public class Database {

    private Connection connection;
    private PlayerData playerData;
    private ReportData reportData;
    private SMPCore plugin;
    private String url;
    private String username;
    private String password;

    public Database(SMPCore plugin) {
        this.plugin = plugin;
        this.url = plugin.getConfig().getString("Storage.url");
        this.username = plugin.getConfig().getString("Storage.username");
        this.password = plugin.getConfig().getString("Storage.password");

        this.playerData = new PlayerData(plugin, this);
        this.reportData = new ReportData(plugin, this);
    }

    public void uploadAllData() {
        for (ServerPlayer sPlayer : playerData.getServerPlayers().values()) {
            playerData.insertServerPlayer(sPlayer);
        }

        for (Report report : reportData.getReports()) {
            reportData.saveReport(report);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection != null)
            return connection;

        this.connection = DriverManager.getConnection(url, username, password);
        plugin.getLogger().info("Connected to the database.");

        return connection;
    }

    public void setupTable(String table) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(table);
        statement.close();
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public ReportData getReportData() {
        return reportData;
    }
}
