package me.numin.smpcore.inventories;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.List;

public class StaffReportManagerHUD extends CoreInventory {

    public StaffReportManagerHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        int index = 0;

        for (Report report : SMPCore.plugin.getDatabase().getReportData().getReports()) {
            List<String> lore = Arrays.asList("From: " + report.getAuthor().getName(), "Left-Click for details.", "Right-Click to resolve.");

            if (report.getReportType() == ReportType.PLAYER) {
                setSkullItem(index, Bukkit.getPlayer(report.getTitle()), (index + 1) + ": " + report.getTitle() + "[" + report.getIdentifier() + "]", lore);
            } else {
                setItem(index, Material.BOOK, (index + 1) + ": " + report.getTitle() + "[" + report.getIdentifier() + "]", lore);
            }
            index++;
        }
        setItem(bottomCenter, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.STAFF_REPORT;
    }

    @Override
    public String getName() {
        return "Reports (" + SMPCore.plugin.getDatabase().getReportData().getReports().size() + ")";
    }
}
