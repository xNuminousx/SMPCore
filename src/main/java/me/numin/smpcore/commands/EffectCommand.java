package me.numin.smpcore.commands;

import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.EffectHUD;
import org.bukkit.entity.Player;

public class EffectCommand {

    public EffectCommand(Player player, String[] args) {

        // "/effect"
        if (args.length < 1) {
            new EffectHUD(player);
        } else if (args[0].equalsIgnoreCase("remove")) {
            Effect.remove(player);
        }
    }
}
