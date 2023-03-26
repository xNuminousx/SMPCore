package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandRegistry implements CommandExecutor {

    private static final Map<String, Map<String, Object>> ali = SMPCore.plugin.getDescription().getCommands();

    public static void registerCommands() {
        for (String alias : ali.keySet()) {
            SMPCore.plugin.getCommand(alias).setExecutor(new CommandRegistry());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Command format: /label arg[0] arg[1] arg[2] ...

        if (ali.containsKey(label)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to execute commands.");
            } else {
                Player player = (Player)sender;

                if (label.equalsIgnoreCase("core")) {
                    new CoreCommand(player, args);
                } else if (label.equalsIgnoreCase("effect")) {
                    new EffectCommand(player, args);
                } else if (label.equalsIgnoreCase("game")) {
                    new GameCommand(player, args);
                } else if (label.equalsIgnoreCase("report")) {
                    new ReportCommand(player, args);
                } else if (label.equalsIgnoreCase("testsound")) {
                    new SoundCommand(player, args);
                } else if (label.equalsIgnoreCase("wand")) {
                    new WandCommand(player, args);
                }
            }
            return true;
        }
        return false;
    }
}
