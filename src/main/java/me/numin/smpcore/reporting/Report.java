package me.numin.smpcore.reporting;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    //TODO: Allow for multiple reports of the same name to be stored. A duplicate report breaks the database.

    private OfflinePlayer author;
    private String title;
    private ReportType reportType;
    private String message;
    private String date;

    public Report(OfflinePlayer author, String title, ReportType reportType, String date, String message) {
        this.author = author;
        this.title = title;
        this.reportType = reportType;
        this.date = date;
        this.message = message;

        SMPCore.plugin.getReports().add(this);
    }
    public Report(OfflinePlayer author, String title, ReportType reportType, String message) {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa zzz");

        new Report(author, title, reportType, format.format(now), message);
    }

    // IF THE REPORT IS ABOUT A PLAYER
    public Report(OfflinePlayer author, Player title, String message) {
        new Report(author, title.getName(), ReportType.PLAYER, message);
    }

    public void delete() {
        SMPCore.plugin.getReports().remove(this);
        SMPCore.plugin.getDatabase().deleteReport(this);
    }

    public void open(Player player) {
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "R E P O R T");
        player.sendMessage(ChatColor.GOLD + "Title: " + ChatColor.RESET + getTitle());
        player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.RESET + getAuthor().getName());
        player.sendMessage(ChatColor.GOLD + "Time: " + ChatColor.RESET + getDate());
        player.sendMessage(ChatColor.GOLD + "Message: " + ChatColor.RESET + getMessage());
    }

    public void notifyStaff() {
        for (String name : SMPCore.staff) {
            Player player = Bukkit.getPlayer(name);

            if (player != null && player.isOnline()) {
                player.sendMessage(CoreMessage.newReport());
            }
        }
    }

    public String getDate() {
        return  date;
    }

    public OfflinePlayer getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public String getMessage() {
        return message;
    }
}
