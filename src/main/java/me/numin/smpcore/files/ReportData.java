package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class ReportData {

    private final SMPCore plugin;
    private final StorageFile file;

    public ReportData(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("reports");
    }

    public StorageFile getFile() {
        return file;
    }

    public void loadReports() {
        try {
            plugin.getLogger().info("Searching for saved reports...");
            Set<String> configKeys = getFile().getConfig().getKeys(true);

            for (String key : configKeys) {
                String[] parts = key.split(Pattern.quote("."));

                if (parts.length == 3) {
                    String author = parts[1];
                    String name = parts[2];
                    String path = constructConfigPath(author, name);
                    Object value = getFile().getConfig().get(path);

                    if (value instanceof String) {
                        String reason = (String) value;
                        new Report(author, name, reason, ReportType.MISC);
                    } else if (value instanceof List) {
                        List<String> reasons = (List<String>)value;
                        for (String reason : reasons) {
                            new Report(author, name, reason, ReportType.MISC);
                        }
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load all of the unresolved reports.");
            e.printStackTrace();
        }
    }

    public void saveReport(Report report) {
        try {
            if (report.hasQueuedReports()) {
                List<String> reasons = new ArrayList<>();
                reasons.add(report.getReason());
                for (Report queuedReport : report.getQueuedReports()) {
                    reasons.add(queuedReport.getReason());
                }
                getFile().getConfig().set(constructConfigPath(report.getAuthor(), report.getReportName()), reasons);
                return;
            }
            getFile().getConfig().set(constructConfigPath(report.getAuthor(), report.getReportName()), report.getReason());
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save a logged report.");
            plugin.getLogger().info("Report Name: " + report.getReportName());
            plugin.getLogger().info("Author: " + report.getAuthor());
            plugin.getLogger().info("Time: " + report.getTime());
        }
    }

    public void saveReports() {
        try {
            plugin.getLogger().info("Saving unresolved reports...");
            for (Report report : SMPCore.reports) {
                saveReport(report);
            }
            file.saveConfig();
            plugin.getLogger().info("Unresolved reports have been saved.");
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save a logged report.");
            e.printStackTrace();
        }
    }

    public void unloadReport(Report report) {
        try {
            file.getConfig().getConfigurationSection("Reports." + report.getAuthor() + "." + report.getReportName()).set("Reasons", null);
            file.saveConfig();
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to unload a report on file.");
            e.printStackTrace();
        }
    }

    private String constructConfigPath(String author, String name) {
        return "Reports." + author + "." + name + ".Reasons";
    }
}
