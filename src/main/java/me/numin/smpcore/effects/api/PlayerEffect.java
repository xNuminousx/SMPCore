package me.numin.smpcore.effects.api;

import org.bukkit.entity.Player;

public interface PlayerEffect {
    void run();
    Player getPlayer();
    String getName();
}
