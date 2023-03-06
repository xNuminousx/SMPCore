package me.numin.smpcore.effects;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class EnderEffect extends Effect {

    private final Player player;

    private final int amount;
    private final double x;
    private final double y;
    private final double z;
    private final double speed;

    public EnderEffect(Player player) {
        super(player);
        this.player = player;

        this.amount = SMPCore.plugin.getConfig().getInt("Effects.Ender.Amount");
        this.x = SMPCore.plugin.getConfig().getDouble("Effects.Ender.Offset.X");
        this.y = SMPCore.plugin.getConfig().getDouble("Effects.Ender.Offset.Y");
        this.z = SMPCore.plugin.getConfig().getDouble("Effects.Ender.Offset.Z");
        this.speed = SMPCore.plugin.getConfig().getDouble("Effects.Ender.Speed");
    }

    @Override
    public void run() {
        Location location = player.getLocation().add(0, 0.6, 0);
        location.getWorld().spawnParticle(Particle.PORTAL, location, amount, x, y, z, speed);
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
