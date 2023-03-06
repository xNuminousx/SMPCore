package me.numin.smpcore.effects;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.utils.RainbowCycle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RainbowEffect extends Effect {

    private Player player;
    private Location location;
    private RainbowCycle rainbowCycle;

    private boolean isMoving;
    private int point;
    private int amount;

    public RainbowEffect(Player player) {
        super(player);
        this.player = player;
        this.rainbowCycle = new RainbowCycle(30, 255, 0, 0, Color.GREEN);

        this.amount = SMPCore.plugin.getConfig().getInt("Effects.Rainbow.Amount");
    }

    @Override
    public void run() {
        location = player.getLocation();
        Particle.DustOptions color = rainbowCycle.cycle();

        if (isMoving) {
            location.getWorld().spawnParticle(Particle.REDSTONE, location.add(0, 1, 0), amount, 0.4, 0.3, 0.4, color);
        } else {
            point += 360/90;
            if (point == 360) {
                point = 0;
            }

            double multiplier = 6;
            double angle = point * Math.PI / 360;
            double sX = Math.cos(angle * multiplier);
            double sY = Math.sin(angle * 3) / 2;
            double sZ = Math.sin(angle * multiplier);
            location.add(sX, sY + 1, sZ);
            location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, 0.1, 0.1, 0.1, color);
            location.subtract(sX, sY + 1, sZ);
        }
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
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
