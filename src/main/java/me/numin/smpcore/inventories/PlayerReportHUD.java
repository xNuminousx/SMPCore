package me.numin.smpcore.inventories;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

public class PlayerReportHUD extends CoreInventory {

    public PlayerReportHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        Collection<? extends Player> onlinePlayers = SMPCore.plugin.getServer().getOnlinePlayers();

        for (int i = 0; i < onlinePlayers.size(); i++) {
            if (i == bottomCenterLeft || i == bottomCenterRight)
                continue;

            Player player = (Player) onlinePlayers.toArray()[i];
            ItemStack skull = getSkull(player);
            ItemMeta skullMeta = skull.getItemMeta();
            skullMeta.setDisplayName(player.getName());
            skull.setItemMeta(skullMeta);

            setItem(i, skull);
        }
        setItem(bottomCenterLeft, Material.WRITABLE_BOOK, "Report Someone Not Listed", null);
        setItem(bottomCenterRight, Material.BARRIER, "Exit", null);
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public Identifier getIdentifier() {
        return Identifier.PLAYER_REPORT;
    }

    @Override
    public String getName() {
        return "Report a Player";
    }
}
