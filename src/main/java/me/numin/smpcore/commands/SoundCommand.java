package me.numin.smpcore.commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundCommand {

    public SoundCommand(Player player, String[] args) {

        if (args.length < 1) {
            player.sendMessage("Format: /testsound [sound ID] [volume] [pitch]");
        } else {
            String input = args[0];
            float volume = Float.parseFloat(args[1]);
            float pitch = Float.parseFloat(args[2]);

            for (Sound sound : Sound.values()) {
                String soundID = sound.toString();

                if (soundID.equalsIgnoreCase(input)) {
                    player.playSound(player.getLocation(), sound, volume, pitch);
                    return;
                }
                player.sendMessage("That is an invalid sound ID.");
            }
        }
    }
}
