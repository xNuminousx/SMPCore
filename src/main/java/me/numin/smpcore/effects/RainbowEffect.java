package me.numin.smpcore.effects;

import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.utils.RainbowCycle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RainbowEffect extends Effect {

    private Player player;
    private RainbowCycle rainbowCycle;

    public RainbowEffect(Player player) {
        super(player);

        this.player = player;
        this.rainbowCycle = new RainbowCycle(30, 255, 0, 0, Color.GREEN);
    }

    @Override
    public void run() {
        Location location = player.getLocation().add(0, 1, 0);
        Particle.DustOptions color = rainbowCycle.cycle();
        player.getWorld().spawnParticle(Particle.REDSTONE, location, 2, 0.4, 0.4, 0.4, color);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Rainbow";
    }
}
