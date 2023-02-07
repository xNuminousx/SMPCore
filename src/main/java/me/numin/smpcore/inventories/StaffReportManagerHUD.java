package me.numin.smpcore.inventories;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
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
        for (Report report : SMPCore.reports) {
            List<String> lore = Arrays.asList("From: " + report.getAuthor(), "Left-Click for details.", "Right-Click to resolve.");
            if (index == bottomCenter)
                index++;

            String header = "[" + index + "] ";
            if (report.getType() == ReportType.PLAYER)
                setSkullItem(index, report.getPlayer(), index + ": " + report.getPlayer().getName(), lore);
            else {
                setItem(index, Material.PAPER, header + report.getReportName(), lore);
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
    public String getName() {
        return "Reports";
    }
}
