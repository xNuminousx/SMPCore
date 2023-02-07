package me.numin.smpcore.inventories;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerLocationHUD extends CoreInventory {

    public PlayerLocationHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        List<ItemStack> skulls = new ArrayList<>();

        for (Player onlinePlayer : SMPCore.plugin.getServer().getOnlinePlayers()) {
            ItemStack skull = getSkull(onlinePlayer.getUniqueId());
            ItemMeta skullMeta = skull.getItemMeta();

            String name = onlinePlayer.getName();
            String nickname = onlinePlayer.getDisplayName();
            String currWorld = onlinePlayer.getWorld().getName();
            double x = Math.round(onlinePlayer.getLocation().getX());
            double y = Math.round(onlinePlayer.getLocation().getY());
            double z = Math.round(onlinePlayer.getLocation().getZ());
            String sX = Double.toString(x);
            String sY = Double.toString(y);
            String sZ = Double.toString(z);

            List<String> loreConstruct = new ArrayList<>();
            loreConstruct.add(ChatColor.RESET + "Nickname: " + nickname);
            loreConstruct.add(ChatColor.RESET + "World: " + currWorld);
            loreConstruct.add(ChatColor.RESET + "X: " + sX);
            loreConstruct.add(ChatColor.RESET + "Y: " + sY);
            loreConstruct.add(ChatColor.RESET + "Z: " + sZ);

            skullMeta.setDisplayName(name);
            skullMeta.setLore(loreConstruct);
            skull.setItemMeta(skullMeta);

            skulls.add(skull);
        }

        for (int i = 0; i < skulls.size(); i++) {
            if (i == bottomCenter)
                continue;

            setItem(i, skulls.get(i));
        }

        setItem(bottomCenter, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public String getName() {
        return "Player Locations";
    }
}
