package me.numin.smpcore.reporting;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Report {

    public ArrayList<Report> queuedReports = new ArrayList<>();

    private final ReportType type;
    private final String author;
    private final Player player;
    private final String reportName;
    private final String reason;
    private final String time;

    private boolean queued;

    public Report(String author, Player player, String reason) {
        this.author = author;
        this.player = player;
        this.reportName = player.getName();
        this.reason = reason;
        this.type = ReportType.PLAYER;

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa zzz");
        this.time = format.format(now);

        for (Report report : SMPCore.reports) {
            if (report.getAuthor().equalsIgnoreCase(author) && report.getReportName().equalsIgnoreCase(player.getName())) {
                report.getQueuedReports().add(this);
                queued = true;
            }
        }
        if (!queued)
            SMPCore.reports.add(this);
    }

    public Report(String author, String reportName, String reason, ReportType type) {
        this.author = author;
        this.player = null;
        this.reportName = reportName;
        this.reason = reason;
        this.type = type;

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        this.time = format.format(now);

        for (Report report : SMPCore.reports) {
            if (report.getAuthor().equalsIgnoreCase(author) && report.getReportName().equalsIgnoreCase(reportName)) {
                report.getQueuedReports().add(this);
                queued = true;
            }
        }
        if (!queued)
            SMPCore.reports.add(this);
    }

    public String getAuthor() {
        return author;
    }

    public Player getPlayer() {
        return player;
    }

    public String getReportName() {
        return reportName;
    }

    public String getReason() {
        return reason;
    }

    public String getTime() {
        return time;
    }

    public ReportType getType() {
        return type;
    }

    public ArrayList<Report> getQueuedReports() {
        return queuedReports;
    }

    public boolean hasQueuedReports() {
        return !getQueuedReports().isEmpty();
    }

    public static void notifyStaff() {
        for (String name : SMPCore.staff) {
            Player player = Bukkit.getPlayer(name);

            if (player != null && player.isOnline()) {
                player.sendMessage(CoreMessage.newReport());
            }
        }
    }

    public static void open(Player player, Report report) {
        int index = 0;
        for (Report r : SMPCore.reports) {
            if (r.equals(report)) {
                break;
            }
            index++;
        }

        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "R E P O R T" + ChatColor.DARK_GRAY + " " + ChatColor.BOLD + "[" + index + "]");
        player.sendMessage(ChatColor.GOLD + "Title: " + ChatColor.RESET + report.getReportName());
        player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.RESET + report.getAuthor());
        player.sendMessage(ChatColor.GOLD + "Time: " + ChatColor.RESET + report.getTime());

        if (report.hasQueuedReports()) {
            player.sendMessage(ChatColor.GOLD + "Reasons:");
            player.sendMessage(ChatColor.GOLD + "- " + ChatColor.RESET + report.getReason());
            for (Report queuedReport : report.getQueuedReports()) {
                player.sendMessage(ChatColor.GOLD + "- " + ChatColor.RESET + queuedReport.getReason());
            }
        } else {
            player.sendMessage(ChatColor.GOLD + "Reason: " + ChatColor.RESET + report.getReason());
        }
    }

    public void resolve() {
        SMPCore.reports.remove(this);
        SMPCore.plugin.getReportData().unloadReport(this);
    }
}
