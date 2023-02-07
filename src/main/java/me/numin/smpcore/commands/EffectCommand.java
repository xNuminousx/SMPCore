package me.numin.smpcore.commands;

import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.EffectHUD;
import org.bukkit.entity.Player;

public class EffectCommand {

    private String[] args;
    private Player player;

    public EffectCommand(Player player, String[] args) {
        this.args = args;
        this.player = player;

        // "/effect"
        if (args.length < 1) {
            new EffectHUD(player);
        } else if (args[0].equalsIgnoreCase("remove")) {
            Effect.remove(player);
        }
    }
}
