package me.numin.smpcore.spells.api;

import me.numin.smpcore.SMPCore;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class Spell implements Castable {

    public enum SpellType {DAMAGING, FAMILIAR, HEALING}

    private final Player player;

    public Spell(Player player) {
        this.player = player;
        player.playSound(player.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2, 1);
        SMPCore.spells.put(player, this);
    }

    @Override
    public void remove() {
        SMPCore.spells.remove(player);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
