package me.numin.smpcore;

import me.numin.smpcore.commands.CommandRegistry;
import me.numin.smpcore.database.Database;
import me.numin.smpcore.effects.api.EffectManager;
import me.numin.smpcore.files.Config;
import me.numin.smpcore.files.GameData;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.game.api.GameManager;
import me.numin.smpcore.listeners.*;
import me.numin.smpcore.spells.api.SpellManager;
import me.numin.smpcore.spells.api.Wands;
import me.numin.smpcore.utils.Familiar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class SMPCore extends JavaPlugin {

    public static SMPCore plugin;
    public static List<String> staff = new ArrayList<>();
    private Database database;
    private GameData gameData;

    @Override
    public void onEnable() {
        plugin = this;
        database = new Database(plugin);
        gameData = new GameData(plugin);
        staff = getConfig().getStringList("Staff");

        new Config(plugin);
        new Wands();

        CommandRegistry.registerCommands();
        registerListeners();
        registerRunnables();

        // Running a scheduler resolves a NPE generated in the report initialization.
        getServer().getScheduler().runTaskLater(this, () -> database.getReportData().loadReports(), 40L);
    }

    @Override
    public void onDisable() {
        Familiar.killAll();
        Game.stopAll();
        database.uploadAllData();
        database.close();
    }

    public Database getDatabase() {
        return database;
    }

    public GameData getGameData() {
        return gameData;
    }

    public Location getSpawn() {
        return new Location(Bukkit.getWorld("world"), 2, 76, -205, -269, 2);
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new CoreListener(plugin), plugin);
        getServer().getPluginManager().registerEvents(new EffectListener(), plugin);
        getServer().getPluginManager().registerEvents(new GameListener(), plugin);
        getServer().getPluginManager().registerEvents(new ReportListener(), plugin);
        getServer().getPluginManager().registerEvents(new SpellListener(), plugin);
        getServer().getPluginManager().registerEvents(new StaffListener(), plugin);
        getServer().getPluginManager().registerEvents(new WandInventoryListener(), plugin);
    }

    public void registerRunnables() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new EffectManager(plugin), 20, 2);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SpellManager(), 20, 1);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameManager(), 20, 1);
    }
}
