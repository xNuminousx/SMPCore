package me.numin.smpcore.listeners;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.database.PlayerData;
import me.numin.smpcore.database.ServerPlayer;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.*;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.utils.CoreMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.HashMap;
import java.util.UUID;

public class CoreListener implements Listener {

    private final SMPCore plugin;

    public CoreListener(SMPCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);

            if (coreInventory == null) return;

            if (coreInventory.getIdentifier().equals(CoreInventory.Identifier.CORE)) {
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
                    String message = SMPCore.plugin.getConfig().getString("Language.NuminMessage");
                    if (message == null) message = "Hey, that tickled!";
                    player.sendMessage(message);
                    coreInventory.close();
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (CoreInventory.hasInventory(player)) {
            CoreInventory coreInventory = CoreInventory.getCoreInventory(player);
            CoreInventory.inventories.remove(coreInventory);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = plugin.getDatabase().getPlayerData();
        ServerPlayer sPlayer = playerData.getServerPlayer(player.getUniqueId());

        if (sPlayer != null) {
            playerData.insertServerPlayer(sPlayer);
        }

        Effect effect = null;
        for (Effect eff : Effect.effects) {
            if (eff.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                effect = eff;
            }
        }

        if (effect != null) {
            Effect.effects.remove(effect);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        HashMap<UUID, ServerPlayer> serverPlayers = SMPCore.plugin.getDatabase().getPlayerData().getServerPlayers();
        if (serverPlayers.containsKey(uuid)) {
            ServerPlayer sPlayer = serverPlayers.get(uuid);
            Effect.initializeEffect(player, sPlayer.getEffect());
        } else {
            ServerPlayer sPlayer = plugin.getDatabase().getPlayerData().selectServerPlayer(uuid);

            if (sPlayer == null)
                new ServerPlayer(uuid, "", 0, 0);
        }

        if (!player.hasPlayedBefore()) {
            // Give server info book.
            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null)
                    player.getInventory().addItem(getServerInfoBook());
                break;
            }
        }

        if (SMPCore.staff.contains(player.getName()) && !plugin.getDatabase().getReportData().getReports().isEmpty()) {
            int x = plugin.getDatabase().getReportData().getReports().size();
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
