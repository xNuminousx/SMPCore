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
                if (SMPCore.plugin.getReports().isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                // "/report delete x"
                if (args.length < 2) {
                    player.sendMessage(CoreMessage.missingIdentifier());
                } else {
                    String reportOf = args[1];
                    for (Report report : SMPCore.plugin.getReports()) {
                        if (report.getTitle().equalsIgnoreCase(reportOf)) {
                            report.delete();
                            player.sendMessage(CoreMessage.resolvedReport());
                            return;
                        }
                    }
                    try {
                        int index = Integer.parseInt(reportOf);
                        if (index >= SMPCore.plugin.getReports().size()) {
                            player.sendMessage(CoreMessage.invalidIdentifier());
                            return;
                        }

                        Report report = SMPCore.plugin.getReports().get(index);
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
                if (SMPCore.plugin.getReports().isEmpty()) {
                    player.sendMessage(CoreMessage.noActiveReports());
                    return;
                }

                for (Iterator<Report> iterator = SMPCore.plugin.getReports().iterator(); iterator.hasNext(); ) {
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
