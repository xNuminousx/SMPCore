package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.inventories.CoreHUD;
import org.bukkit.entity.Player;

public class CoreCommand {

    private String[] args;
    private Player player;

    public CoreCommand(Player player, String[] args) {
        this.args = args;
        this.player = player;

        if (args.length < 1) {
            new CoreHUD(player);
        } else {
            if (args[0].equalsIgnoreCase("get")) {
                for (Effect effect : SMPCore.effects) {
                    player.sendMessage(effect.getPlayer().getName() +  ": " + effect.getName());
                }
            }
        }
    }
}
