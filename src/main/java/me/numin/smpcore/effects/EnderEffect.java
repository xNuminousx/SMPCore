package me.numin.smpcore.effects;

import me.numin.smpcore.effects.api.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EnderEffect extends Effect {

    private Player player;

    public EnderEffect(Player player) {
        super(player);

        this.player = player;
    }

    @Override
    public void run() {
        Location location = player.getLocation().add(0, 0.6, 0);
        location.getWorld().spawnParticle(Particle.PORTAL, location, 1, 0.3, 0.5, 0.3, 0.4);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Ender";
    }
}
