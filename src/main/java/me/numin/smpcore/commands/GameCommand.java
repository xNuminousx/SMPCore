package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand {

    public GameCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Formats");
            player.sendMessage("- /game start [name] [duration] [do respawn (true/false)]");
            player.sendMessage("- /game stop");
            player.sendMessage("- /game join");
            player.sendMessage("- /game leave");
            player.sendMessage("- /game time");
            player.sendMessage("- /game info");
            if (SMPCore.staff.contains(player.getName())) {
                player.sendMessage("- /game addpoint <point name>");
            }
        } else {
            String input = args[0];

            if (input.equalsIgnoreCase("start")) {
                if (!SMPCore.games.isEmpty()) {
                    player.sendMessage("There is already an active game. Try /game join");
                    return;
                }

                if (args.length == 4) {
                    player.sendMessage("Setting up game...");
                    String name = args[1];
                    long duration = Long.parseLong(args[2]);
                    boolean doRespawn = Boolean.getBoolean(args[3]);
                    new Game(player, name, duration, doRespawn);
                } else if (args.length == 2) {
                    player.sendMessage("Setting up game...");
                    String name = args[1];
                    new Game(player, name);
                } else if (args.length == 1) {
                    player.sendMessage("Setting up game...");
                    new Game(player);
                } else {
                    if (SMPCore.staff.contains(player.getName())) {
                        player.sendMessage("Format: /game start <name> <duration in seconds> <respawning true/false>");
                    } else {
                        player.sendMessage("Format: /game start");
                    }
                }
            } else if (input.equalsIgnoreCase("stop")) {
                if (SMPCore.games.isEmpty()) {
                    player.sendMessage("There are no games to stop.");
                }

                List<Game> toStop = new ArrayList<>();
                for (Game game : SMPCore.games) {
                    if (game.getHost().getUniqueId().equals(player.getUniqueId()) || SMPCore.staff.contains(player.getName())) {
                        player.sendMessage("Stopping the game...");
                        toStop.add(game);
                    } else {
                        player.sendMessage("You are not the host of the game, so you cannot force stop it.");
                    }
                }

                for (Game game : toStop) {
                    game.stop();
                }
                toStop.clear();
            } else if (input.equalsIgnoreCase("join")) {
                if (SMPCore.games.isEmpty()) {
                    player.sendMessage("There are no active games to join. Try /game start");
                    return;
                }

                for (Game game : SMPCore.games) {
                    if (game.getPlayers().contains(player)) {
                        player.sendMessage("You are already in a game.");
                        return;
                    }
                    game.addPlayer(player);
                    return;
                }
            } else if (input.equalsIgnoreCase("leave")) {
                for (Game game : SMPCore.games) {
                    if (game.getPlayers().contains(player)) {
                        game.leaveGame(player);
                        return;
                    }
                }
                player.sendMessage("You are not in a game.");
            } else if (input.equalsIgnoreCase("time")) {
                int index = 0;

                if (SMPCore.games.isEmpty()) {
                    player.sendMessage("There are no active games.");
                    return;
                }

                for (Game game : SMPCore.games) {
                    if (game.getPlayers().contains(player)) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Game: " + game.getName());
                        player.sendMessage("Time remaining: " + game.getTimeRemainingAsString());
                        return;
                    }
                }

                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Active Games");
                for (Game game : SMPCore.games) {
                    index++;
                    player.sendMessage(ChatColor.YELLOW + "Game #" + index + " - " + game.getName() + ": " + ChatColor.RESET + game.getTimeRemainingAsString());
                }
            } else if (input.equalsIgnoreCase("info")) {
                if (SMPCore.games.isEmpty()) {
                    player.sendMessage("There are no active games.");
                    return;
                }
                int index = 0;
                for (Game game : SMPCore.games) {
                    index++;
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Game #" + index + ": " + game.getName());
                    player.sendMessage("Host: " + game.getHost().getDisplayName());
                    player.sendMessage("Time remaining: " + game.getTimeRemainingAsString());
                    player.sendMessage("Respawning: " + game.doRespawn());
                    player.sendMessage("Spawn Points: " + game.getSpawnPoints().size());

                    List<String> playerNames = new ArrayList<>();
                    for (Player thisPlayer : game.getPlayers()) {
                        playerNames.add(thisPlayer.getName());
                    }
                    player.sendMessage("Players: " + String.join(", ", playerNames));
                }
            } else if (input.equalsIgnoreCase("addpoint")) {
                if (!SMPCore.staff.contains(player.getName())) {
                    player.sendMessage("You do not have access to this command.");
                    return;
                }
                if (args.length == 2) {
                    String name = args[1];
                    SMPCore.plugin.getGameData().definePoint(name, player.getLocation());
                    player.sendMessage("Adding a spawn point named [" + name + "]");
                } else {
                    player.sendMessage("Format: /game addpoint <name>");
                }
            }
        }
    }
}
