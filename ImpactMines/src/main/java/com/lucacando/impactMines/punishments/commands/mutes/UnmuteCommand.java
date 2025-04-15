package com.lucacando.impactMines.punishments.commands.mutes;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {
    private final Main main;
    private final PunishmentManager manager;

    public UnmuteCommand(Main main) {
        this.main = main;
        this.manager = main.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean canRunCommand = sender instanceof ConsoleCommandSender;

        if (sender instanceof Player) {
            if (main.getPlayerRank((Player) sender).isStaff()) canRunCommand = true;
        }

        if (!canRunCommand) {
            sender.sendMessage(main.noPermission);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(main.prefix + "Usage: /unmute <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (manager.isPlayerMuted(target)) {
            manager.unmutePlayer(target);
            sender.sendMessage(main.prefix + ChatColor.GREEN + target.getName() + ChatColor.BLUE + " has been unmuted.");
        } else {
            sender.sendMessage(main.prefix + "§cThat player isn't unmuted.");
        }

        return true;
    }
}
