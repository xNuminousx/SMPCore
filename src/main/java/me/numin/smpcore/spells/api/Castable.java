package me.numin.smpcore.spells.api;

import org.bukkit.entity.Player;

public interface Castable {
    void cast();
    void remove();
    Player getPlayer();
}
