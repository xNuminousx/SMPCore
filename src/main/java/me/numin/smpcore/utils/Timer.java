package me.numin.smpcore.utils;

public class Timer {

    private boolean reached;
    private final long interval;
    private long startTime;

    public Timer(long interval) {
        this.reached = false;
        this.interval = interval;
        this.startTime = System.currentTimeMillis();
    }

    public void tick() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - startTime;
        long remaining = interval - elapsed;

        if (remaining <= 0) {
            reached = true;
            startTime = currentTime;
        } else reached = false;
    }

    public boolean isReached() {
        return reached;
    }
}
