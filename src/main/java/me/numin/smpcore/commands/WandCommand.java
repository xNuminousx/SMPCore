package me.numin.smpcore.commands;

import me.numin.smpcore.inventories.DamagingRecipeHUD;
import me.numin.smpcore.inventories.FamiliarRecipeHUD;
import me.numin.smpcore.inventories.HealingRecipeHUD;
import me.numin.smpcore.inventories.WandRecipeHUD;
import org.bukkit.entity.Player;

public class WandCommand {

    public WandCommand(Player player, String[] args) {
        if (args.length < 1) {
            new WandRecipeHUD(player);
        } else {
            if (args[0].equalsIgnoreCase("damaging")) {
                new DamagingRecipeHUD(player);
            } else if (args[0].equalsIgnoreCase("healing")) {
                new HealingRecipeHUD(player);
            } else if (args[0].equalsIgnoreCase("familiar")) {
                new FamiliarRecipeHUD(player);
            }
        }
    }
}
