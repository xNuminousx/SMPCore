package me.numin.smpcore.effects;

import me.numin.smpcore.effects.api.Effect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RedstoneEffect extends Effect {

    private Player player;

    public RedstoneEffect(Player player) {
        super(player);

        this.player = player;
    }

    @Override
    public void run() {
        Location location = player.getLocation().add(0, 0.6, 0);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, 0.3, 0.5, 0.3, 0, new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1));
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Redstone";
    }
}
