package me.numin.smpcore.utils;

import org.bukkit.Color;
import org.bukkit.Particle;

public class RainbowCycle {
    private boolean riseRed;
    private boolean riseGreen;
    private boolean riseBlue;
    private int r, g, b;
    private final int interval;
    private final int max;

    /**
     * Class used to increase/decrease RGB values in a specific way to simulate a gradient
     * of colors through the classic rainbow cycle (ROYGBIV).
     *
     * Suggested inputs for the classic Rainbow:
     * r = 255, g = 0, b = 0, firstRise = green
     *
     * @param interval The speed at which the colors shift (0 minimum, 255 max).
     * @param r The starting red value.
     * @param g The starting green value.
     * @param b The starting blue value.
     * @param firstRise The color that will start rising first.
     */
    public RainbowCycle(int interval, int r, int g, int b, Color firstRise) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.interval = interval;
        max = 255 - interval;

        if (firstRise == Color.RED)
            riseRed = true;
        else if (firstRise == Color.GREEN)
            riseGreen = true;
        else if (firstRise == Color.BLUE)
            riseBlue = true;
    }

    public Particle.DustOptions cycle() {
        if (riseRed) {
            if (r < max) r += interval;
            else riseBlue = false;
        } else {
            if (r > interval) r -= interval;
            else riseBlue = true;
        }

        if (riseGreen) {
            if (g < max) g += interval;
            else riseRed = false;
        } else {
            if (g > interval) g -= interval;
            else riseRed = true;
        }

        if (riseBlue) {
            if (b < max) b += interval;
            else riseGreen = false;
        } else {
            if (b > interval) b -= interval;
            else riseGreen = true;
        }
        return new Particle.DustOptions(Color.fromRGB(r, g, b), 1);
    }
}
