package me.numin.smpcore.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;

public class CustomFirework {

    public static ArrayList<CustomFirework> fireworks = new ArrayList<>();
    private final Firework firework;
    private final Location origin;
    private boolean hasDetonated = false, travel;

    public CustomFirework(Player player, int power, Color color, FireworkEffect.Type type, boolean flicker, boolean trail, boolean travel) {
        Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation().add(0, 2, 0), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().with(type).withColor(color).flicker(flicker).trail(trail).build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);

        this.firework = firework;
        this.origin = player.getLocation().clone();
        this.travel = travel;

        if (travel) fireworks.add(this);
        else {
            getFirework().detonate();
            hasDetonated = true;
        }
    }

    public CustomFirework(Location location, int power, Color color, FireworkEffect.Type type, boolean flicker, boolean trail, boolean travel) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().with(type).withColor(color).flicker(flicker).trail(trail).build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);

        this.firework = firework;
        this.origin = location;
        this.travel = travel;

        if (travel) fireworks.add(this);
        else {
            getFirework().detonate();
            hasDetonated = true;
        }
    }

    public Firework getFirework() {
        return firework;
    }

    public Location getOrigin() {
        return origin;
    }

    public boolean doTravel() {
        return travel;
    }

    public boolean hasDetonated() {
        return hasDetonated;
    }

    public void setDetonated(boolean hasDetonated) {
        this.hasDetonated = hasDetonated;
    }
}
