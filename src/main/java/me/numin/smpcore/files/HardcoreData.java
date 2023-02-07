package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HardcoreData {

    private final SMPCore plugin;
    private final StorageFile file;

    public List<UUID> bannedPlayers = new ArrayList<UUID>();

    public HardcoreData(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("hardcore");
    }

    public StorageFile getFile() {
        return file;
    }

    public void banPlayer(Player player) {
        String name = player.getName();
        UUID uuid = player.getUniqueId();
        getFile().getConfig().set("BannedPlayers" + name, uuid);
    }

    public void unbanPlayer(Player player) {
        String name = player.getName();
        Object uuid = getFile().getConfig().get("BannedPlayers" + name);

        assert uuid != null;
        if (!uuid.toString().isEmpty()) {
            getFile().getConfig().set("BannedPlayers" + name, null);
        }
    }

    public void loadBannedPlayers() {

    }
}
