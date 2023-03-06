package me.numin.smpcore.effects;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.effects.api.Effect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class RedstoneEffect extends Effect {

    private final Color color;
    private final Player player;

    private final int amount;
    private final double x;
    private final double y;
    private final double z;

    public RedstoneEffect(Player player, Color color) {
        super(player);
        this.player = player;
        this.color = color;

        this.amount = SMPCore.plugin.getConfig().getInt("Effects.Redstone.Amount");
        this.x = SMPCore.plugin.getConfig().getDouble("Effects.Redstone.Offset.X");
        this.y = SMPCore.plugin.getConfig().getDouble("Effects.Redstone.Offset.Y");
        this.z = SMPCore.plugin.getConfig().getDouble("Effects.Redstone.Offset.Z");
    }

    @Override
    public void run() {
        Location location = player.getLocation().add(0, 1, 0);
        location.getWorld().spawnParticle(Particle.REDSTONE, location, amount, x, y, z, 0, new Particle.DustOptions(color, 1));
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getName() {
        return "Colored Dust";
    }
}
