package me.numin.smpcore.commands;

import me.numin.smpcore.SMPCore;
import me.numin.smpcore.game.MobBattle;
import me.numin.smpcore.game.PvPGame;
import me.numin.smpcore.game.api.Game;
import me.numin.smpcore.game.api.Wave;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand {

    public GameCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Formats");
            player.sendMessage("- /game start <PVP / MobBattle> <duration> <respawning true/false>");
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
                //TODO: Remove when ready to allow multiple games running at the same time.
                if (!Game.games.isEmpty()) {
                    player.sendMessage("There is already an active game. Try /game join");
                    return;
                }
                //TODO: Re-enable custom games once done testing.
                if (args.length == 4) {
                    String name = args[1].toLowerCase();
                    long duration = Long.parseLong(args[2]);
                    boolean doRespawn = Boolean.getBoolean(args[3]);

                    initiateGame(player, name, duration, doRespawn);
                } else if (args.length == 2) {
                    String name = args[1];

                    initiateGame(player, name, 120000, true); // default 2 minute game with respawn
                } else if (args.length == 1) {
                    player.sendMessage("No game defined. Setting up a 2 minute Mob Battle by default.");
                    new MobBattle(player, 120000, true); // default 2 minute game with respawn
                } else {
                    player.sendMessage("Format: /game start <PVP / MobBattle> <duration> <respawning true/false>");
                }
            } else if (input.equalsIgnoreCase("stop")) {
                if (Game.games.isEmpty()) {
                    player.sendMessage("There are no games to stop.");
                }

                List<Game> toStop = new ArrayList<>();
                for (Game game : Game.games) {
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
                if (Game.games.isEmpty()) {
                    player.sendMessage("There are no active games to join. Try /game start");
                    return;
                }

                for (Game game : Game.games) {
                    if (game.getPlayers().contains(player)) {
                        player.sendMessage("You are already in a game.");
                        return;
                    }
                    game.addPlayer(player);
                    return;
                }
            } else if (input.equalsIgnoreCase("leave")) {
                for (Game game : Game.games) {
                    if (game.getPlayers().contains(player)) {
                        player.teleport(game.getSpectate());
                        game.removePlayer(player);
                        return;
                    }
                }
                player.sendMessage("You are not in a game.");
            } else if (input.equalsIgnoreCase("time")) {
                int index = 0;

                if (Game.games.isEmpty()) {
                    player.sendMessage("There are no active games.");
                    return;
                }

                for (Game game : Game.games) {
                    if (game.getPlayers().contains(player)) {
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Your Game: " + game.getName());
                        player.sendMessage("Time remaining: " + game.getTimeRemainingAsString());
                        return;
                    }
                }

                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Active Games");
                for (Game game : Game.games) {
                    index++;
                    player.sendMessage(ChatColor.YELLOW + "Game #" + index + " - " + game.getName() + ": " + ChatColor.RESET + game.getTimeRemainingAsString());
                }
            } else if (input.equalsIgnoreCase("info")) {
                if (Game.games.isEmpty()) {
                    player.sendMessage("There are no active games.");
                    return;
                }
                int index = 0;
                for (Game game : Game.games) {
                    index++;
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Game #" + index + ": " + game.getName());
                    player.sendMessage("Host: " + game.getHost().getDisplayName());
                    player.sendMessage("Time remaining: " + game.getTimeRemainingAsString());
                    player.sendMessage("Respawning: " + game.doRespawn());
                    player.sendMessage("Spawn Points: " + game.getPlayerSpawnPoints().size());

                    if (game instanceof MobBattle) {
                        MobBattle mobBattle = (MobBattle) game;
                        Wave wave = mobBattle.getWave();
                        player.sendMessage("Wave: " + wave.getStage());
                        player.sendMessage("Mobs Remaining: " + wave.getMobsRemaining());
                        player.sendMessage("Grace Period: " + wave.isInGracePeriod());
                    }

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
                if (args.length == 3) {
                    String gameName = args[1];
                    if (gameName.equalsIgnoreCase("pvp")) gameName = "PvP";
                    else if (gameName.equalsIgnoreCase("mobbattle")) gameName = "MobBattle";
                    String pointName = args[2];
                    SMPCore.plugin.getGameData().definePoint(gameName, pointName, player.getLocation());
                    player.sendMessage("Adding a spawn point for " + gameName + " named: " + pointName);
                } else {
                    player.sendMessage("Format: /game addpoint <game name> <point name>");
                }
            }
        }
    }

    public void initiateGame(Player player, String name, long duration, boolean doRespawn) {
        String gameName = name.toLowerCase();
        switch (gameName) {
            case "pvp":
                new PvPGame(player, duration, doRespawn);
                break;
            case "mobbattle":
                new MobBattle(player, duration, doRespawn);
                break;
            default:
                player.sendMessage("`" + gameName + "` is not a valid game. Try `pvp` or `mobbattle`");

        }
    }
}
