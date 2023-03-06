package me.numin.smpcore.inventories;

import me.numin.smpcore.game.MobBattle;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.inventories.api.CoreInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class GameSelectorHUD extends CoreInventory {

    public GameSelectorHUD(Player player) {
        super(player);
    }

    @Override
    public void setItems() {
        if (Game.games.isEmpty())
            setItem(4, Material.BARRIER, "NO ACTIVE GAMES", Arrays.asList("Try /game start"));

        int i = 0;
        Material item;
        for (Game game : Game.games) {
            if (game instanceof MobBattle) item = Material.ZOMBIE_HEAD;
            else item = Material.IRON_SWORD;
            setItem(i, item, game.getName(), Arrays.asList("Players: " + game.getPlayers().size(), "Time Remaining: " + game.getTimeRemainingAsString()));
            i++;
        }
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Override
    public int getSlotCount() {
        return 9;
    }

    @Override
    public String getName() {
        return "Game Selector";
    }
}
