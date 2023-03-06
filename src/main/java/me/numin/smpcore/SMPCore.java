package me.numin.smpcore;

import me.numin.smpcore.commands.CommandRegistry;
import me.numin.smpcore.database.Database;
import me.numin.smpcore.database.PlayerStatsCache;
import me.numin.smpcore.effects.api.Effect;
import me.numin.smpcore.effects.api.EffectManager;
import me.numin.smpcore.files.Config;
import me.numin.smpcore.files.GameData;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.game.api.GameManager;
import me.numin.smpcore.inventories.api.CoreInventory;
import me.numin.smpcore.listeners.*;
import me.numin.smpcore.reporting.Report;
import me.numin.smpcore.spells.api.Spell;
import me.numin.smpcore.spells.api.SpellManager;
import me.numin.smpcore.spells.api.Wands;
import me.numin.smpcore.utils.Familiar;
import me.numin.smpcore.database.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public final class SMPCore extends JavaPlugin {

    //TODO: Fix all the array lists. Put in their respective classes?
    public static SMPCore plugin;
    public static ArrayList<Effect> effects = new ArrayList<>();
    public static ArrayList<Familiar> familiars = new ArrayList<>();
    public static ArrayList<CoreInventory> inventories = new ArrayList<>();
    public static Map<Player, Spell> spells = new HashMap<>();
    public static List<String> staff = new ArrayList<>();

    private final ArrayList<Report> reports = new ArrayList<>();

    private Database database;
    private PlayerStatsCache playerStatsCache;

    private Config config;
    private GameData gameData;

    @Override
    public void onEnable() {
        plugin = this;
        gameData = new GameData(plugin);
        config = new Config(plugin);
        staff = config.getFile().getConfig().getStringList("Staff");

        database = new Database();
        database.loadReports();
        playerStatsCache = new PlayerStatsCache(database);
        playerStatsCache.loadMap();

        CommandRegistry.registerCommands();
        registerListeners();
        registerRunnables();
        setupSpellRecipes();
    }

    @Override
    public void onDisable() {
        //TODO: Fix all of the for loops. Put into their respective classes?
        for (Familiar familiar : familiars)
            familiar.kill();

        for (Game game : Game.games)
            game.stop();

        for (PlayerStats playerStats : getPlayerStatsCache().getPlayerStatsMap().values())
            getDatabase().injectPlayerStats(playerStats);

        for (Report report : getReports())
            getDatabase().saveReport(report);

        try {
            getDatabase().getConnection().close();
        } catch (SQLException e) {
            getLogger().info("Unable to close database.");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    public Database getDatabase() {
        return database;
    }

    public PlayerStatsCache getPlayerStatsCache() {
        return playerStatsCache;
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
        getServer().getPluginManager().registerEvents(new HardcoreListener(), plugin);
        getServer().getPluginManager().registerEvents(new ReportListener(), plugin);
        getServer().getPluginManager().registerEvents(new SpellListener(), plugin);
        getServer().getPluginManager().registerEvents(new StaffListener(), plugin);
        getServer().getPluginManager().registerEvents(new WandInventoryListener(), plugin);
    }

    public void registerRunnables() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new EffectManager(), 20, 2);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new SpellManager(), 20, 1);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new GameManager(), 20, 1);
    }

    public void setupSpellRecipes() {
        new Wands();
    }
}
