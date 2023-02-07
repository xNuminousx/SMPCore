package me.numin.smpcore.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class CustomFirework {

    public CustomFirework(Player player, int power, Color color, FireworkEffect.Type type, boolean flicker, boolean trail) {
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().with(type).withColor(color).flicker(flicker).trail(trail).build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }

    public CustomFirework(Location location, int power, Color color, FireworkEffect.Type type, boolean flicker, boolean trail) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().with(type).withColor(color).flicker(flicker).trail(trail).build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();
    }
}
