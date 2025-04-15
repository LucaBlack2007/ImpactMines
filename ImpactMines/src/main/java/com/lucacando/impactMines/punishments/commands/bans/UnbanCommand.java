package com.lucacando.impactMines.punishments.commands.bans;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class UnbanCommand implements CommandExecutor {

    private final Main main;
    private final PunishmentManager manager;

    public UnbanCommand(Main main) {
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
            sender.sendMessage(main.prefix + "Usage: /unban <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        // Check both ban lists
        boolean wasBanned = false;

        if (Bukkit.getBanList(BanList.Type.NAME).isBanned(target.getName())) {
            Bukkit.getBanList(BanList.Type.NAME).pardon(target.getName());
            wasBanned = true;
        }

        if (Bukkit.getBanList(BanList.Type.IP).isBanned(target.getName())) {
            Bukkit.getBanList(BanList.Type.IP).pardon(target.getName());
            wasBanned = true;
        }

        if (wasBanned) {
            sender.sendMessage(main.prefix + ChatColor.GREEN + target.getName() + ChatColor.BLUE + " has been unbanned.");
        } else {
            sender.sendMessage(main.prefix + "§cThat player isn't banned.");
        }

        return true;
    }
}
