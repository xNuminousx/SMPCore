package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.*;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.utils.CoreMessage;
import me.numin.smpcore.utils.PlayerStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.sql.SQLException;

public class CoreListener implements Listener {

    private final SMPCore plugin;

    public CoreListener(SMPCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        PlayerStats playerStats = this.plugin.getDatabase().getPlayerStatsByUUID(player.getUniqueId());

        if (playerStats == null) {
            playerStats = new PlayerStats(player.getUniqueId(), "", 0, 0, 1);
            this.plugin.getDatabase().createPlayerStats(playerStats);
        } else {
            playerStats.setBlocksBroken(playerStats.getBlocksBroken() + 1);
            this.plugin.getDatabase().updatePlayerStats(playerStats);
        }
    }

    @EventHandler
    public void hudClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory.getName().equalsIgnoreCase("Core Features")) {
                if (coreInventory.invalidClick(event)) {
                    event.setCancelled(true);
                    return;
                }

                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if (itemName.equalsIgnoreCase("Reporting")) {
                    new MainReportHUD(player);
                } else if (itemName.equalsIgnoreCase("Effects")) {
                    new EffectHUD(player);
                } else if (itemName.equalsIgnoreCase("Wand Recipes")) {
                    new WandRecipeHUD(player);
                } else if (itemName.equalsIgnoreCase("Games")) {
                    new GameHUD(player);
                } else if (itemName.equalsIgnoreCase("Staff Actions")) {
                    for (String name : SMPCore.staff) {
                        if (player.getName().equalsIgnoreCase(name)) {
                            new StaffHUD(player);
                            break;
                        }
                    }
                } else if (itemName.equalsIgnoreCase("Numin")) {
                    player.sendMessage("Hey, that tickled!");
                    coreInventory.close();
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);
            SMPCore.inventories.remove(coreInventory);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Effect effect = null;

        for (Effect eff : SMPCore.effects) {
            if (eff.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                effect = eff;
            }
        }

        if (effect != null) {
            SMPCore.effects.remove(effect);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {

            //Setup player database chart
            PlayerStats playerStats = new PlayerStats(player.getUniqueId(), "", 0, 0, 0);
            this.plugin.getDatabase().createPlayerStats(playerStats);

            // Give server info book.
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) == null) {
                    player.getInventory().setItem(i, getServerInfoBook());
                    return;
                }
            }
        }

        // ENABLE LAST EFFECT
        PlayerStats playerStats = SMPCore.plugin.getDatabase().getPlayerStatsByUUID(player.getUniqueId());
        String effect = playerStats.getEffect();
        Effect.initializeEffect(player, effect);

        if (SMPCore.staff.contains(player.getName()) && !SMPCore.reports.isEmpty()) {
            int x = SMPCore.reports.size();
            player.sendMessage(CoreMessage.outstandingReports(x));
        }
    }

    public ItemStack getServerInfoBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta)book.getItemMeta();
        bookMeta.setTitle("Server Info");
        bookMeta.addPage("Welcome to Numin's SMP Server! Here is some information to help you get started..." +
                "\n" +
                "\n- You can use the " + ChatColor.BOLD +  "/rules" + ChatColor.RESET + " command to learn about our rules and guidelines for the server.");
        bookMeta.addPage("- When you encounter an issue with a player or the server, you can report it easily using the " + ChatColor.BOLD + "/report" + ChatColor.RESET +  " command." +
                "\n" +
                "- The area around spawn is for community use! Do not build your base here or forage for materials here. You start your survival journey elsewhere ");
        bookMeta.addPage("and use spawn to build shops and do business." +
                "\n" +
                "\n" +
                "Welcome to the community! If you have any questions, please ask!");
        bookMeta.setAuthor("Numin");
        book.setItemMeta(bookMeta);
        return book;
    }
}
