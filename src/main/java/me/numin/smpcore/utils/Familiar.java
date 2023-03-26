package me.numin.smpcore.utils;

import me.numin.smpcore.SMPCore;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

//TODO: Add new familiars. Different designs, effects, etc.

public class Familiar {

    public static ArrayList<Familiar> familiars = new ArrayList<>();

    private Location destination;
    private Location location;
    private final Player host;
    private final Vector vector;
    private boolean rest;
    private long startedResting, lastUpdate;

    private double range;
    private double soundFrequency;

    public Familiar(Player player) {
        host = player;
        location = host.getLocation().add(trueRandom(), trueRandom() + 1, trueRandom());
        destination = generateRandomPoint(location);
        vector = new Vector(0, 0, 0);
        host.getWorld().playSound(location, Sound.ENTITY_GHAST_AMBIENT, 0.1F, 2);

        this.range = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfFamiliar.Range");
        this.soundFrequency = SMPCore.plugin.getConfig().getDouble("Spells.SpellOfFamiliar.SoundFrequency");

        Familiar.familiars.add(this);
    }

    public static void killAll(){
        for (Familiar familiar : familiars) {
            familiar.kill();
        }
    }

    public void kill() {
        host.getWorld().playSound(location, Sound.ENTITY_GHAST_DEATH, 0.1F, 1.5F);
        Familiar.familiars.remove(this);
    }

    public void move() {
        if (host.getLocation().getWorld() != location.getWorld()) {
            location = generateRandomPoint(host.getEyeLocation());
            refreshDestination();
            return;
        }

        Block currBlock = location.getBlock();
        double hostDistance = location.distance(host.getLocation());
        double radius = 4;
        double speed = hostDistance > radius ? 0.4 : 0.1;

        if (hostDistance > range) {
            location = generateRandomPoint(host.getEyeLocation());
        }

        if (!rest) {
            // then move
            double destinationDistance = location.distance(destination);
            vector.add(destination.toVector().subtract(location.toVector()).multiply(speed)).normalize();
            location.add(vector.clone().multiply(speed));

            if (destinationDistance < 0.4) {
                startedResting = System.currentTimeMillis();
                rest = true;
            }

            if (System.currentTimeMillis() > lastUpdate + 2000) {
                startedResting = System.currentTimeMillis();
                rest = true;
            }

            if (host.getLocation().distance(location) > radius || host.getLocation().distance(destination) > radius)
                refreshDestination();

            if (currBlock.getType().isSolid())
                refreshDestination();
        } else {
            if (System.currentTimeMillis() > startedResting + 3000) {
                refreshDestination();
                rest = false;
            }

            if (hostDistance > radius) {
                refreshDestination();
                rest = false;
            }
        }

        if (new Random().nextInt(1000) < soundFrequency)
            host.getWorld().playSound(location, Sound.ENTITY_GHAST_AMBIENT, 0.1F, 2);

        host.getWorld().spawnParticle(Particle.TOWN_AURA, location, 10, 0.06, 0.06, 0.06, 0);
        host.getWorld().spawnParticle(Particle.SCRAPE, location, 2, 0.05, 0.05, 0.05, 0);
    }

    public void refreshDestination() {
        destination = generateRandomPoint(host.getLocation());
        lastUpdate = System.currentTimeMillis();
    }

    public double trueRandom() {
        int x = new Random().nextInt(2);
        if (x == 1) return Math.random();
        return -Math.random();
    }

    public Location randomPoint(Location base) {
        return base.clone().add(trueRandom(), trueRandom(), trueRandom());
    }

    public Location generateRandomPoint(Location base) {
        Location newPoint = randomPoint(base.add(0, 1, 0));

        if (newPoint.getBlock().getType().isSolid()) {
            for (int i = 0; i <= 10; i++) {
                Location nextAttempt = randomPoint(base.add(0, 1, 0));
                if (!nextAttempt.getBlock().getType().isSolid())
                    return nextAttempt;
            }
            rest = true;
        }
        return newPoint;
    }

    public Player getHost() {
        return host;
    }

    public Location getLocation() {
        return location;
    }
}