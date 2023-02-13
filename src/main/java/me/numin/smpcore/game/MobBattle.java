package me.numin.smpcore.game;

import org.bukkit.entity.Player;

//TODO: Streamline "Game" and "Mob Battle" to work together.
public class MobBattle extends Game{

    private String name = "Mob Battle";
    private long duration = 600000;
    private boolean doRespawn = true;

    public MobBattle(Player host) {
        super(host);
    }

    public MobBattle(Player host, String name) {
        super(host, name);
        this.name = name;
    }

    public MobBattle(Player host, String name, long duration, boolean doRepsawn) {
        super(host, name, duration, doRepsawn);
        this.name = name;
        this.duration = duration;
        this.doRespawn = doRepsawn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public boolean doRespawn() {
        return doRespawn;
    }
}
