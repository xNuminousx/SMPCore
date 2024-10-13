package me.numin.smpcore.spells;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.spells.api.Spell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpellOfDamaging extends Spell {

    private final Location origin;
    private final Location blast;
    private final Vector direction;

    private double damage;
    private double radius;
    private double range;
    private double speed;

    public SpellOfDamaging(Player player) {
        super(player);

        this.origin = player.getEyeLocation();
        this.blast = origin.clone();
        this.direction = player.getLocation().getDirection();

        this.damage = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfDamaging.Damage");
        this.radius = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfDamaging.AoE");
        this.range = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfDamaging.Range");
        this.speed = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfDamaging.Speed");

        player.playSound(origin, Sound.BLOCK_BEACON_POWER_SELECT, 0.1F, 1.5F);
    }

    @Override
    public void cast() {
        blast.add(direction.multiply(speed));
        blast.getWorld().spawnParticle(Particle.LARGE_SMOKE, blast, 2, 0, 0, 0, 0);
        blast.getWorld().spawnParticle(Particle.FIREWORK, blast, 1, 0, 0, 0, 0.07);
        blast.getWorld().spawnParticle(Particle.CRIMSON_SPORE, blast, 3, 0.1, 0.1, 0.1, 0);

        if (blast.distance(origin) > range || blast.getBlock().getType().isSolid()) {
            remove();
        }

        for (Entity entity : blast.getWorld().getNearbyEntities(blast, radius, radius, radius)) {
            if (entity instanceof LivingEntity && !entity.getUniqueId().equals(getPlayer().getUniqueId())) {
                ((LivingEntity) entity).damage(damage, getPlayer());
                remove();
            }
        }
    }
}
