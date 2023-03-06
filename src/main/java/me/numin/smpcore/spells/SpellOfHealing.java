package me.numin.smpcore.spells;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.spells.api.Spell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpellOfHealing extends Spell {

    private final Location origin;
    private Location spiral;

    private int amplifier;
    private int duration;
    private int point;
    private long time;

    public SpellOfHealing(Player player) {
        super(player);

        this.origin = player.getLocation();
        this.time = System.currentTimeMillis();

        this.amplifier = SMPCore.plugin.getConfig().getInt("Spells.SpellOfHealing.Amplifier");
        this.duration = SMPCore.plugin.getConfig().getInt("Spells.SpellOfHealing.Duration");

        player.playSound(origin, Sound.BLOCK_BELL_RESONATE, 0.5F, 2);

        origin.getWorld().spawnParticle(Particle.HEART, origin.clone().add(0, 2, 0), 1, 0, 0, 0, 0);
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, amplifier));
    }

    @Override
    public void cast() {
        spiral = getPlayer().getLocation();
        for (int i = 0; i < 2; i++) {
            point += 360/90;
            if (point == 360) {
                point = 0;
            }

            double angle = point * Math.PI / 180;
            double x = Math.cos(angle * 5);
            double y = Math.cos(angle) / 1.5;
            double z = Math.sin(angle * 5);
            spiral.add(x, y + 1, z);
            spiral.getWorld().spawnParticle(Particle.GLOW, spiral, 2, 0, 0, 0, 0);
            spiral.subtract(x, y + 1, z);
        }
        if (System.currentTimeMillis() > time + 1000) {
            remove();
        }
    }
}
