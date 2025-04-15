package com.lucacando.impactMines.punishments.commands.bans;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    private final Main main;
    private final PunishmentManager manager;
    private final String usage = "Usage: /ban <player> [<reason>]";

    public BanCommand(Main main) {
        this.main = main;
        this.manager = main.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        boolean canRunCommand = sender instanceof ConsoleCommandSender;
        if (sender instanceof Player && main.getPlayerRank((Player) sender).isStaff()) {
            canRunCommand = true;
        }

        if (!canRunCommand) {
            sender.sendMessage(main.noPermission);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(main.prefix + usage);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || (target.getName() == null && !target.hasPlayedBefore())) {
            sender.sendMessage(main.prefix + "§cPlayer not found.");
            return true;
        }

        String reason = "No reason given.";
        if (args.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            reason = sb.toString().trim();
        }

        boolean permanent = true;
        String duration = ""; // empty string for permanent
        manager.banPlayer(target, duration, permanent, reason, sender);

        return true;
    }
}
