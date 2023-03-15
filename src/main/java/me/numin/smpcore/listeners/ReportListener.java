package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.*;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.reporting.ReportType;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ReportListener implements Listener {

    private InventoryClickEvent event;
    private ReportType type;
    private Player reportTarget;
    private String reportTitle;
    private String reason;
    private boolean typingTitle;
    private boolean typingReason;

    @EventHandler
    public void hudClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        this.event = event;
        reportTitle = null;
        reason = null;

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory.getName().equalsIgnoreCase("Make a Report")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                } else if (itemName.equalsIgnoreCase("Report A Player")) {
                    new PlayerReportHUD(player);
                } else if (itemName.equalsIgnoreCase("Report A Bug")) {
                    new BugReportHUD(player);
                } else if (itemName.equalsIgnoreCase("Manage Reports")) {
                    for (String name : SMPCore.staff) {
                        if (player.getName().equalsIgnoreCase(name)) {
                            new StaffReportManagerHUD(player);
                            break;
                        }
                    }
                }
                event.setCancelled(true);
            } else if (coreInventory.getName().equalsIgnoreCase("Report a Bug")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Exit")) {
                    new MainReportHUD(player);
                    event.setCancelled(true);
                    return;
                }

                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Report Something Not Listed")) {
                    player.sendMessage(CoreMessage.miscBugReportTitlePrompt());
                    type = ReportType.MISC;
                    typingTitle = true;
                    event.setCancelled(true);
                    coreInventory.close();
                    return;
                }
                event.setCancelled(true);
                reportTitle = event.getCurrentItem().getItemMeta().getDisplayName();
                type = ReportType.BUG;
                coreInventory.close();
                player.sendMessage(CoreMessage.reportDescPrompt());
                typingReason = true;
            } else if (coreInventory.getName().equalsIgnoreCase("Report a Player")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Report Someone Not Listed")) {
                    coreInventory.close();
                    player.sendMessage(CoreMessage.miscPlayerReportTitlePrompt());
                    typingTitle = true;
                    type = ReportType.MISC;
                    return;
                } else if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Exit")) {
                    new MainReportHUD(player);
                    return;
                }

                reportTitle = event.getCurrentItem().getItemMeta().getDisplayName();
                reportTarget = SMPCore.plugin.getServer().getPlayer(reportTitle);
                coreInventory.close();
                player.sendMessage(CoreMessage.reportDescPrompt());
                type = ReportType.PLAYER;
                typingReason = true;
            } else if (coreInventory.getName().equalsIgnoreCase("Reports")) {
                if (invalidClick()) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Exit")) {
                    new CoreHUD(player);
                } else {
                    for (Report report : SMPCore.plugin.getDatabase().getReportData().getReports()) {
                        if (itemName.contains(report.getTitle() + "[" + report.getIdentifier() + "]")) {
                            if (event.isRightClick()) {
                                report.delete();
                                new StaffReportManagerHUD(player);
                                break;
                            } else {
                                report.open(player);
                                coreInventory.close();
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void chatListener(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (typingTitle && reportTitle == null) {
            if (event.getMessage().equalsIgnoreCase("exit")) {
                player.sendMessage(CoreMessage.exitedReport());
            } else {
                player.sendMessage(CoreMessage.reportDescPrompt());
                reportTitle = event.getMessage();
                typingReason = true;
            }
            typingTitle = false;
            event.setCancelled(true);
            return;
        }

        if (typingReason && reason == null) {
            if (event.getMessage().equalsIgnoreCase("exit")) {
                player.sendMessage(CoreMessage.exitedReport());
            } else {
                Report report;
                reason = event.getMessage();
                if (type == ReportType.PLAYER) {
                    report = new Report(player, reportTarget, reason);
                } else {
                    report = new Report(player, reportTitle, type, reason);
                }
                report.notifyStaff();
                player.sendMessage(CoreMessage.successfulReport());
            }
            typingReason = false;
            event.setCancelled(true);
        }
    }

    public boolean invalidClick() {
        return (event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty();
    }
}
