package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.MainReportHUD;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;

public class ReportCommand {

    public ReportCommand(Player player, String[] args) {
        ArrayList<Report> reports = SMPCore.plugin.getDatabase().getReportData().getReports();

        // "/report"
        if (args.length < 1) {
            new MainReportHUD(player);
        } else {
            // "/report delete"
            if (args[0].equalsIgnoreCase("delete")) {
                if (reports.isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                // "/report delete x"
                if (args.length < 2) {
                    player.sendMessage(CoreMessage.missingIdentifier());
                } else {
                    String reportOf = args[1];
                    for (Report report : reports) {
                        if (report.getTitle().equalsIgnoreCase(reportOf)) {
                            report.delete();
                            player.sendMessage(CoreMessage.resolvedReport());
                            return;
                        }
                    }
                    try {
                        int index = Integer.parseInt(reportOf);
                        if (index >= reports.size()) {
                            player.sendMessage(CoreMessage.invalidIdentifier());
                            return;
                        }

                        Report report = reports.get(index);
                        if (report == null) {
                            player.sendMessage(CoreMessage.invalidIdentifier());
                            return;
                        }

                        report.delete();
                        player.sendMessage(CoreMessage.resolvedReport());
                    } catch (Exception e) {
                        player.sendMessage(CoreMessage.invalidIdentifier());
                    }
                }
            } else if (args[0].equalsIgnoreCase("newest")) {
                if (reports.isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                for (Iterator<Report> iterator = reports.iterator(); iterator.hasNext(); ) {
                    Report report = iterator.next();

                    if (!iterator.hasNext()) {
                        report.open(player);
                    }
                }
            } else {
                player.sendMessage(CoreMessage.invalidIdentifier());
            }
        }
    }
}
