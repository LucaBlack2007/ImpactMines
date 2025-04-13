package com.lucacando.impactMines.admin.gamemode;

import com.lucacando.impactMines.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeSurvival implements CommandExecutor {

    Main main;
    String usage;

    public GamemodeSurvival(Main main, String usage) {
        this.main = main;
        this.usage = main.prefix + usage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (main.isStaff(player)) {
                if (args.length < 1) {
                    player.sendMessage(main.prefix + "Gamemode set to survival.");
                    player.setGameMode(GameMode.SURVIVAL);
                } else {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        Player p2 = Bukkit.getPlayer(args[0]);
                        if (p2.isOnline()) {
                            player.sendMessage(main.prefix + ChatColor.YELLOW + p2.getDisplayName() + "'s " + ChatColor.BLUE + "gamemode set to survival.");
                            p2.setGameMode(GameMode.SURVIVAL);
                            p2.sendMessage(main.prefix + "Your gamemode has been set to survival.");
                        } else {
                            player.sendMessage(main.prefix + "Player is not online.");
                        }
                    } else {
                        player.sendMessage(usage);
                    }
                }
            } else {
                player.sendMessage(main.noPermission);
            }
        }

        return false;
    }
}
