package me.numin.smpcore.effects.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.entity.Player;

public class EffectManager implements Runnable {

    public EffectManager() {
    }

    @Override
    public void run() {
        for (Effect effect : SMPCore.effects) {
            Player player = effect.getPlayer();

            if (!player.isOnline() || player.isDead()) {
                return;
            }

            try {
                effect.run();
            } catch (Exception e) {
                SMPCore.plugin.getLogger().warning("Failed to run effect '" + effect.getName() + "' for " + effect.getPlayer().getName() + ".");
                e.printStackTrace();
            }
        }
    }
}
