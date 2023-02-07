package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.MainReportHUD;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class ReportCommand {

    public ReportCommand(Player player, String[] args) {
        // "/report"
        if (args.length < 1) {
            new MainReportHUD(player);
        } else {
            // "/report delete"
            if (args[0].equalsIgnoreCase("delete")) {
                if (SMPCore.reports.isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                // "/report delete x"
                if (args.length < 2) {
                    player.sendMessage(CoreMessage.missingIdentifier());
                } else {
                    String reportOf = args[1];
                    for (Report report : SMPCore.reports) {
                        if (report.getReportName().equalsIgnoreCase(reportOf)) {
                            SMPCore.reports.remove(report);
                            player.sendMessage(CoreMessage.resolvedReport());
                            return;
                        }
                    }
                    try {
                        int index = Integer.parseInt(reportOf);
                        if (index >= SMPCore.reports.size()) {
                            player.sendMessage(CoreMessage.invalidIdentifier());
                            return;
                        }

                        Report report = SMPCore.reports.get(index);
                        if (report == null) {
                            player.sendMessage(CoreMessage.invalidIdentifier());
                            return;
                        }

                        SMPCore.reports.remove(index);
                        player.sendMessage(CoreMessage.resolvedReport());
                    } catch (Exception e) {
                        player.sendMessage(CoreMessage.invalidIdentifier());
                    }
                }
            } else if (args[0].equalsIgnoreCase("newest")) {
                if (SMPCore.reports.isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                for (Iterator<Report> iterator = SMPCore.reports.iterator(); iterator.hasNext(); ) {
                    Report report = iterator.next();

                    if (!iterator.hasNext()) {
                        Report.open(player, report);
                    }
                }
            } else {
                player.sendMessage(CoreMessage.invalidIdentifier());
            }
        }
    }
}
