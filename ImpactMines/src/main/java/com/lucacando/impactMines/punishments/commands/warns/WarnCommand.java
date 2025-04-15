package com.lucacando.impactMines.punishments.commands.warns;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    Main main;
    PunishmentManager manager;

    public WarnCommand(Main main) {
        this.main = main;
        manager = main.getPunishmentManager();
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

        if (args.length < 2) {
            sender.sendMessage(main.prefix + "Usage: /warn <player> <reason>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || (target.getName() == null && !target.hasPlayedBefore())) {
            sender.sendMessage(main.prefix + "§cPlayer could not be found.");
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        manager.warnPlayer(target, reason, sender);

        return false;
    }
}
