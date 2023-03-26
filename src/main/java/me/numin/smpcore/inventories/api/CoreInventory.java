package me.numin.smpcore.inventories.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CoreInventory implements InteractiveInventory {

    public static ArrayList<CoreInventory> inventories = new ArrayList<>();

    // Inventory item locations;
    public int topCenterLeft = 2;
    public int topCenter = 4;
    public int topCenterRight = 6;
    public int midCenterRight = 11;
    public int midCenter = 13;
    public int midCenterLeft = 15;
    public int bottomCenterLeft = 20;
    public int bottomCenter = 22;
    public int bottomCenterRight = 24;

    private Inventory inventory;
    private final Player player;

    public CoreInventory(Player player) {
        this.player = player;
        open();
    }

    public void open() {
        if (getType() == InventoryType.CHEST) inventory = Bukkit.createInventory(player, getSlotCount(), getName());
        else inventory = Bukkit.createInventory(player, getType(), getName());
        setItems();

        if (hasInventory(player)) {
            inventories.remove(CoreInventory.getCoreInventory(player));
        }

        player.openInventory(getInventory());
        inventories.add(this);
    }

    public void close() {
        inventories.remove(this);
        player.closeInventory();
    }

    public int getSlotCount() {
        return 27;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(player);
        skull.setItemMeta(meta);
        return skull;
    }

    public ItemStack getSkull(UUID uuid) {
        return getSkull(SMPCore.plugin.getServer().getPlayer(uuid));
    }

    public void setItem(int slot, ItemStack icon) {
        getInventory().setItem(slot, icon);
    }

    public void setItem(int slot, Material icon, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        itemStack.setItemMeta(meta);
        getInventory().setItem(slot, itemStack);
    }

    public void setItem(int slot, ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (name != null) meta.setDisplayName(name);
        if (name != null) meta.setLore(lore);
        item.setItemMeta(meta);
        getInventory().setItem(slot, item);
    }

    public void setSkullItem(int slot, Player target, String name, List<String> lore) {
        ItemStack skull = getSkull(target);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        skull.setItemMeta(meta);
        getInventory().setItem(slot, skull);
    }

    public void setSkullItem(int slot, UUID uuid, String name, List<String> lore) {
        setSkullItem(slot, SMPCore.plugin.getServer().getPlayer(uuid), name, lore);
    }

    public static CoreInventory getCoreInventory(Player player) {
        for (CoreInventory coreInventory : inventories) {
            if (coreInventory.getPlayer().getUniqueId() == player.getUniqueId()) {
                return coreInventory;
            }
        }
        return null;
    }

    public static boolean hasInventory(Player player) {
        for (CoreInventory coreInventory : inventories)
            if (coreInventory.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return true;
            }
        return false;
    }

    public boolean invalidClick(InventoryClickEvent event) {
        return (event.getCurrentItem() == null) ||
                (event.getCurrentItem().equals(new ItemStack(Material.AIR))) ||
                event.getCurrentItem().getItemMeta() == null ||
                event.getCurrentItem().getItemMeta().getDisplayName().isEmpty();
    }
}
