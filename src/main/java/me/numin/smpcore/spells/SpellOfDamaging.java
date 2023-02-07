package me.numin.smpcore.spells;

import me.numin.smpcore.spells.api.Spell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellOfDamaging extends Spell {

    private Location origin;
    private Location blast;
    private Vector direction;

    public SpellOfDamaging(Player player) {
        super(player);

        this.origin = player.getEyeLocation();
        this.blast = origin.clone();
        this.direction = player.getLocation().getDirection();

        player.playSound(origin, Sound.BLOCK_BEACON_POWER_SELECT, 0.1F, 1.5F);
    }

    @Override
    public void cast() {
        blast.add(direction.multiply(1));
        blast.getWorld().spawnParticle(Particle.SMOKE_LARGE, blast, 2, 0, 0, 0, 0);
        blast.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, blast, 1, 0, 0, 0, 0.07);
        blast.getWorld().spawnParticle(Particle.CRIT_MAGIC, blast, 3, 0.1, 0.1, 0.1, 0);

        if (blast.distance(origin) > 10 || blast.getBlock().getType().isSolid()) {
            remove();
        }

        for (Entity entity : blast.getWorld().getNearbyEntities(blast, 1, 1, 1)) {
            if (entity instanceof LivingEntity && !entity.getUniqueId().equals(getPlayer().getUniqueId())) {
                ((LivingEntity) entity).damage(3, getPlayer());
                remove();
            }
        }
    }
}
