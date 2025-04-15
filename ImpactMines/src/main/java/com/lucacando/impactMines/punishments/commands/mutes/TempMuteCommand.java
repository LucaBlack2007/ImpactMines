package com.lucacando.impactMines.punishments.commands.mutes;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TempMuteCommand implements CommandExecutor {

    Main main;
    PunishmentManager manager;
    private final String usage = "Usage: /tempmute <player> <time> [<reason>]";

    public TempMuteCommand(Main main) {
        this.main = main;
        manager = main.getPunishmentManager();
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

        if (args.length < 2) {
            sender.sendMessage(main.prefix + usage);
            return true;
        }

        String playerName = args[0];
        String duration = args[1];
        boolean permanent = false;

        if (!manager.isValidDurationFormat(duration)) {
            sender.sendMessage(main.prefix + "Invalid duration format.");
            sender.sendMessage(main.prefix + usage);
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (target == null || (target.getName() == null && !target.hasPlayedBefore())) {
            sender.sendMessage(main.prefix + "§cPlayer could not be found.");
            return true;
        }

        String reason = "No reason given.";
        if (args.length >= 3) {
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            reason = sb.toString().trim();
        }

        manager.mutePlayer(target, duration, permanent, reason, sender);
        return true;
    }
}
