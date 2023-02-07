package me.numin.smpcore.utils;

import org.bukkit.ChatColor;

public class CoreMessage {

    // PREFIXES

    private static final String alert = ChatColor.GOLD + "" + ChatColor.BOLD + "ALERT: " + ChatColor.RESET;

    // MESSAGES

    public enum CoreMessages {

    }

    public static String exitedReport() {
        return "You have exited your report entry.";
    }
    public static String invalidIdentifier() {
        return "That is an invalid report identifier.";
    }
    public static String miscBugReportTitlePrompt() {
        return "Please enter 1 to 2 words to title your report or say 'exit' to exit the report.";
    }
    public static String miscPlayerReportTitlePrompt() {
        return "Please enter the name of who you're reporting and hit enter or say 'exit' to exit the report.";
    }
    public static String missingIdentifier() {
        return "You must include an identifier. Ex: Index number or report title.";
    }
    public static String newReport() {
        return alert + "There is a new report! Use " + ChatColor.YELLOW + "/report newest" + ChatColor.RESET + " to view it.";
    }
    public static String outstandingReports(int x) {
        return alert + "There are " + ChatColor.YELLOW + x + ChatColor.RESET + " report(s) to be reviewed.";
    }
    public static String noActiveReports() {
        return "There are no unresolved reports.";
    }
    public static String reportDescPrompt() {
        return "Please type a brief description of the report or say 'exit' to exit the report.";
    }
    public static String resolvedReport() {
        return "That report has been successfully resolved.";
    }
    public static String successfulReport() {
        return "Thank you for your report!";
    }
}
