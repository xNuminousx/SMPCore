package me.numin.smpcore.spells.api;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class Spell implements Castable {

    public enum SpellType {DAMAGING, FAMILIAR, HEALING}
    public static Map<Player, Spell> spells = new HashMap<>();

    private final Player player;

    public Spell(Player player) {
        this.player = player;
        player.playSound(player.getEyeLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2, 1);
        spells.put(player, this);
    }

    @Override
    public void remove() {
        spells.remove(player);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
