package me.numin.smpcore.database;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ReportData {

    // STRUCTURE -- identifier: title, author, report_type, date, message

    private ArrayList<Report> reports = new ArrayList<>();

    private Database database;
    private SMPCore plugin;

    public ReportData(SMPCore plugin, Database database) {
        this.plugin = plugin;
        this.database = database;

        try {
            database.setupTable("CREATE TABLE IF NOT EXISTS " + getTableName() + "(`identifier` varchar(5) primary key, `title` varchar(16), `author` varchar(36), `report_type` varchar(16), `date` varchar(30), `message` varchar(200))");
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to setup table for " + getTableName());
            e.printStackTrace();
        }
        //loadReports();
    }

    public Report getReport(String id) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + getTableName() + " WHERE identifier = ?");
            statement.setString(1, id);
            ResultSet results = statement.executeQuery();

            if (results.next()) {
                int identifier = results.getInt("identifier");
                String title = results.getString("title");
                String uuid = results.getString("author");
                OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                ReportType type = ReportType.getReportTypeFromString(results.getString("report_type"));
                String date = results.getString("date");
                String message = results.getString("message");
                statement.close();
                return new Report(identifier, author, title, type, date, message);
            }
            statement.close();
            return null;
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to grab a report. ID# " + id);
            e.printStackTrace();
        }
        return null;
    }

    public void deleteReport(Report report) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("DELETE FROM " + getTableName() + " WHERE identifier = ?");
            statement.setInt(1, report.getIdentifier());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to delete a report. ID# " + report.getIdentifier());
            e.printStackTrace();
        }
    }

    public void loadReports() {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM " + getTableName());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int identifier = results.getInt("identifier");
                String title = results.getString("title");
                String uuid = results.getString("author");
                OfflinePlayer author = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                ReportType type = ReportType.getReportTypeFromString(results.getString("report_type"));
                String date = results.getString("date");
                String message = results.getString("message");
                new Report(identifier, author, title, type, date, message);
            }
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to load the reports.");
            e.printStackTrace();
        }
    }

    public void saveReport(Report report) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement("INSERT IGNORE INTO " + getTableName() + "(identifier, title, author, report_type, date, message) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setInt(1, report.getIdentifier());
            statement.setString(2, report.getTitle());
            statement.setString(3, report.getAuthor().getUniqueId().toString());
            statement.setString(4, report.getReportType().toString());
            statement.setString(5, report.getDate());
            statement.setString(6, report.getMessage());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            plugin.getLogger().warning("Failed to save a report. ID# " + report.getIdentifier());
            e.printStackTrace();
        }
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public Database getDatabase() {
        return database;
    }

    public String getTableName() {
        return "report_data";
    }
}
