package me.numin.smpcore.files;

import me.numin.smpcore.SMPCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private final SMPCore plugin;
    private final StorageFile file;

    public Config(SMPCore plugin) {
        this.plugin = plugin;
        this.file = new StorageFile("config");
        load();
    }

    public StorageFile getFile() {
        return file;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();

        List<String> staffNames = new ArrayList<>();
        staffNames.add("Numin");
        staffNames.add("Tay3600");
        config.addDefault("Staff", staffNames);

        config.addDefault("Effects.Ender.Amount", 1);
        config.addDefault("Effects.Ender.Speed", 0.4);
        config.addDefault("Effects.Ender.Offset.X", 0.3);
        config.addDefault("Effects.Ender.Offset.Y", 0.5);
        config.addDefault("Effects.Ender.Offset.Z", 0.3);

        config.addDefault("Effects.Rainbow.Amount", 2);

        config.addDefault("Effects.Redstone.Amount", 1);
        config.addDefault("Effects.Redstone.Offset.X", 0.3);
        config.addDefault("Effects.Redstone.Offset.Y", 0.5);
        config.addDefault("Effects.Redstone.Offset.Z", 0.3);

        config.addDefault("Spells.SpellOfDamaging.AoE", 1);
        config.addDefault("Spells.SpellOfDamaging.Damage", 3);
        config.addDefault("Spells.SpellOfDamaging.Range", 10);
        config.addDefault("Spells.SpellOfDamaging.Speed", 1);

        config.addDefault("Spells.SpellOfFamiliar.Range", 15);
        config.addDefault("Spells.SpellOfFamiliar.SoundFrequency", 3);

        config.addDefault("Spells.SpellOfHealing.Amplifier", 1);
        config.addDefault("Spells.SpellOfHealing.Duration", 200);

        config.addDefault("Language.ReportExit", "You have exited your report.");
        config.addDefault("Language.InvalidIdentifier", "That is an invalid report identifier.");
        config.addDefault("Language.MiscBugTitlePrompt", "Please enter 1 to 2 words to title your report or say 'exit' to exit the report.");
        config.addDefault("Language.MiscPlayerTitlePrompt", "Please enter the name of who you're reporting and hit enter or say 'exit' to exit the report.");
        config.addDefault("Language.MissingIdentifier", "You must include an identifier. Ex: Index number or report title.");
        config.addDefault("Language.NoReports", "There are no unresolved reports.");
        config.addDefault("Language.DescriptionPrompt", "Please type a brief description of the report or say 'exit' to exit the report.");
        config.addDefault("Language.ResolvedReport", "That report has been successfully resolved.");
        config.addDefault("Language.SuccessfulReport", "Thank you for your report!");
        config.addDefault("Language.NuminMessage", "Hey, that tickled!");

        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
}
