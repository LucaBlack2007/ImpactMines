package com.lucacando.impactMines.admin;

import com.lucacando.impactMines.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class InvseeCommand implements CommandExecutor {

    Main main;

    public InvseeCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Must be a player to run this command.");
            return false;
        }

        Player player = (Player) sender;
        if (!main.isStaff(player)) {
            player.sendMessage(main.noPermission);
            return false;
        }

        if (args.length < 1) {
            player.sendMessage(main.prefix + "Usage: /invsee <player>");
            return false;
        }

        OfflinePlayer player2 = Bukkit.getOfflinePlayer(args[0]);
        if (player2 == null || !player2.isOnline()) {
            player.sendMessage(main.prefix + "Player not found.");
            return false;
        }

        player.openInventory(player2.getPlayer().getInventory());
        player.sendMessage(main.prefix + "Opening " + main.formatPlayerName(player2.getPlayer()) + ChatColor.BLUE + "'s inventory.");

        return false;
    }
}
