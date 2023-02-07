package me.numin.smpcore.commands;

import org.bukkit.command.CommandSender;

public class HelpCommand {

    public HelpCommand(CommandSender sender) {
        sender.sendMessage("This is the help command.");
    }
}
