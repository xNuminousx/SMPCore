package me.numin.smpcore.commands;

import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.CoreHUD;
import org.bukkit.entity.Player;

public class CoreCommand {

    public CoreCommand(Player player, String[] args) {

        if (args.length < 1) {
            new CoreHUD(player);
        } else {
            if (args[0].equalsIgnoreCase("get")) {
                for (Effect effect : Effect.effects) {
                    player.sendMessage(effect.getPlayer().getName() +  ": " + effect.getName());
                }
            }
        }
    }
}
