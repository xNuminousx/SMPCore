package me.numin.smpcore.utils;

import me.numin.smpcore.SMPCore;
import org.bukkit.ChatColor;

public class CoreMessage {

    // PREFIXES

    private static final String alert = ChatColor.GOLD + "" + ChatColor.BOLD + "ALERT: " + ChatColor.RESET;

    // MESSAGES

    public static String exitedReport() {
        return SMPCore.plugin.getConfig().getString("Language.ReportExit");
    }
    public static String invalidIdentifier() {
        return SMPCore.plugin.getConfig().getString("Language.InvalidIdentifier");
    }
    public static String miscBugReportTitlePrompt() {
        return SMPCore.plugin.getConfig().getString("Language.MiscBugTitlePrompt");
    }
    public static String miscPlayerReportTitlePrompt() {
        return SMPCore.plugin.getConfig().getString("Language.MiscPlayerTitlePrompt");
    }
    public static String missingIdentifier() {
        return SMPCore.plugin.getConfig().getString("Language.MissingIdentifier");
    }
    public static String newReport() {
        return alert + "There is a new report! Use " + ChatColor.YELLOW + "/report newest" + ChatColor.RESET + " to view it.";
    }
    public static String outstandingReports(int x) {
        return alert + "There are " + ChatColor.YELLOW + x + ChatColor.RESET + " report(s) to be reviewed.";
    }
    public static String noActiveReports() {
        return SMPCore.plugin.getConfig().getString("Language.NoReports");
    }
    public static String reportDescPrompt() {
        return SMPCore.plugin.getConfig().getString("Language.DescriptionPrompt");
    }
    public static String resolvedReport() {
        return SMPCore.plugin.getConfig().getString("Language.ResolvedReport");
    }
    public static String successfulReport() {
        return SMPCore.plugin.getConfig().getString("Language.SuccessfulReport");
    }
}
