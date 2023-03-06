package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class GameData {

    private final SMPCore plugin;
    private final StorageFile file;

    private final String basePath = "Game.";
    private final String pointPath = basePath + "Points.";

    public GameData(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("game");
    }

    public StorageFile getFile() {
        return file;
    }

    public void definePoint(String game, String name, Location location) {
        String path = pointPath + game + "." + name + ".";
        double x = location.getX(), y = location.getY(), z = location.getZ();
        float yaw = location.getYaw(), pitch = location.getPitch();
        getFile().getConfig().set(path + "World", location.getWorld().getName());
        getFile().getConfig().set(path + "X", x);
        getFile().getConfig().set(path + "Y", y);
        getFile().getConfig().set(path + "Z", z);
        getFile().getConfig().set(path + "Yaw", yaw);
        getFile().getConfig().set(path + "Pitch", pitch);
        getFile().saveConfig();
    }

    public Location loadPoint(String game, String name) {
        HashMap<String, Location> pointMap = getPoints(game);

        for (String label : pointMap.keySet()) {
            if (label.contains(name))
                return pointMap.get(label);
        }
        return null;
    }

    public HashMap<String, Location> getPoints(String game) {
        ConfigurationSection section = file.getConfig().getConfigurationSection("Game.Points." + game);
        HashMap<String, Location> pointMap = new HashMap<>();

        for (String key : section.getKeys(false)) {
            ConfigurationSection pointSection = section.getConfigurationSection(key);

            String sWorld = pointSection.getString("World");
            World world = Bukkit.getWorld(sWorld);
            double x = pointSection.getDouble("X");
            double y = pointSection.getDouble("Y");
            double z = pointSection.getDouble("Z");
            float yaw = (float) pointSection.getDouble("Yaw");
            float pitch = (float) pointSection.getDouble("Pitch");

            Location point = new Location(world, x, y, z, yaw, pitch);
            pointMap.put(key, point);
        }
        return pointMap;
    }
}
