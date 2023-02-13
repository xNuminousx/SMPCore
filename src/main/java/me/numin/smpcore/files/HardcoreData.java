package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HardcoreData {

    private final SMPCore plugin;
    private final StorageFile file;

    public List<UUID> bannedPlayers = new ArrayList<>();

    public HardcoreData(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("hardcore");
    }

    public StorageFile getFile() {
        return file;
    }

    public List<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public void banPlayer(Player player) {
        getBannedPlayers().add(player.getUniqueId());
    }

    public void unbanPlayer(Player player) {
        getBannedPlayers().remove(player.getUniqueId());
    }

    public void loadBannedPlayers() {
        try {
            plugin.getLogger().info("Loading the hardcore blacklist...");
            List<String> bPlayers = getFile().getConfig().getStringList("BannedPlayers");
            for (String player : bPlayers) {
                UUID uuid = UUID.fromString(player);
                bannedPlayers.add(uuid);
            }
            plugin.getLogger().info("Finished loading hardcore blacklist.");
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load the hardcore blacklist.");
            e.printStackTrace();
        }
    }

    public void saveBannedPlayers() {
        try {
            plugin.getLogger().info("Saving hardcore blacklist...");
            getFile().getConfig().addDefault("Banned Players", bannedPlayers);
            getFile().getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.getLogger().info("Hardcore blacklist saved.");
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save the hardcore blacklist.");
            e.printStackTrace();
        }
    }
}
