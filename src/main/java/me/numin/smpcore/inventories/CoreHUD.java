package me.numin.smpcore.inventories;

import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class CoreHUD  extends CoreInventory {

    public CoreHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        setItem(1, Material.WRITABLE_BOOK, "Reporting", Arrays.asList("Command Alternative:", "/report"));
        setItem(3, Material.FIREWORK_ROCKET, "Effects", Arrays.asList("Command Alternative:", "/effect"));
        setItem(5, constructWand(), "Wand Recipes", Arrays.asList("Command Alternative:", "/wand recipe"));
        setItem(7, Material.IRON_SWORD, "Games", Arrays.asList("Command Alternative:", "/game"));
        setItem(21, Material.NETHER_STAR, "Staff Actions", null);
        setSkullItem(23, UUID.fromString("a3279535-9ea7-484a-8f3e-96019a6e96e7"), "Numin", Collections.singletonList("Lord creator of all things..."));
    }

    public ItemStack constructWand() {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Core Features";
    }
}
