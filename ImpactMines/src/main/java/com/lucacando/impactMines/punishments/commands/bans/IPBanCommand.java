package com.lucacando.impactMines.punishments.commands.bans;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class IPBanCommand implements CommandExecutor {

    private final Main main;
    private final PunishmentManager manager;
    private final String usage = "Usage: /ipban <player> [<reason>]";

    public IPBanCommand(Main main) {
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
            sender.sendMessage(main.prefix + "§cPlayer could not be found.");
            return true;
        }

        if (!target.isOnline()) {
            sender.sendMessage(main.prefix + "§cYou can only IP ban online players.");
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

        manager.IPBan(target.getPlayer(), reason, sender);
        return true;
    }
}
