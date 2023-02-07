package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GameData {

    private SMPCore plugin;
    private StorageFile file;

    private String basePath = "Game.";
    private String pointPath = basePath + "Points.";

    public GameData(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("game");
    }

    public StorageFile getFile() {
        return file;
    }

    public void definePoint(String name, Location location) {
        double x = location.getX(), y = location.getY(), z = location.getZ();
        float yaw = location.getYaw(), pitch = location.getPitch();
        getFile().getConfig().set(pointPath + name + ".X", x);
        getFile().getConfig().set(pointPath + name + ".Y", y);
        getFile().getConfig().set(pointPath + name + ".Z", z);
        getFile().getConfig().set(pointPath + name + ".Yaw", yaw);
        getFile().getConfig().set(pointPath + name + ".Pitch", pitch);
        getFile().saveConfig();
    }

    public Location loadPoint(String name) {
        double x = getFile().getConfig().getDouble(pointPath + name + ".X");
        double y = getFile().getConfig().getDouble(pointPath + name + ".Y");
        double z = getFile().getConfig().getDouble(pointPath + name + ".Z");
        float yaw = (float) getFile().getConfig().getDouble(pointPath + name + ".Yaw");
        float pitch = (float) getFile().getConfig().getDouble(pointPath + name + ".Pitch");
        return new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
    }

    public List<String> getPoints() {
        List<String> points = new ArrayList<>();
        Set<String> configKeys = getFile().getConfig().getKeys(true);
        for (String key : configKeys) {
            String[] parts = key.split(Pattern.quote("."));

            if (parts.length == 3) {
                String section = parts[1];

                if (section.equalsIgnoreCase("Points")) {
                    String name = parts[2];
                    points.add(name);
                }
            }
        }
        return points;
    }
}
