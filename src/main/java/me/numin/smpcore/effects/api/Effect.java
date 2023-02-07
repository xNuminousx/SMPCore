package me.numin.smpcore.effects.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class Effect implements PlayerEffect {

    public Effect(Player player) {
        SMPCore.effects.removeIf(effect -> effect.getPlayer().getUniqueId() == player.getUniqueId());
        SMPCore.effects.add(this);
        //SMPCore.plugin.data.addEffect(player, this);
    }

    public static void remove(Player player) {
        ArrayList<Effect> deletionQueue = new ArrayList<>();
        for (Effect effect : SMPCore.effects) {
            if (effect.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                deletionQueue.add(effect);
            }
        }

        if (!deletionQueue.isEmpty()) {
            for (Effect effect : deletionQueue) {
                effect.getPlayer().sendMessage("Your " + effect.getName() + " effect has been removed.");
                SMPCore.effects.remove(effect);
            }
            deletionQueue.clear();
        } else {
            player.sendMessage("You do not have an effect.");
        }
    }
}
