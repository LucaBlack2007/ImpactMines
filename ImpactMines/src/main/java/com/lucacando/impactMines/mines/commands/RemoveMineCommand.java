package com.lucacando.impactMines.mines.commands;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.mines.Mine;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveMineCommand implements CommandExecutor {

    private final Main main;

    public RemoveMineCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /removemine <mine_id>");
            return true;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid mine ID. It should be a number.");
            return true;
        }

        if (id < 0 || id >= main.activeMines.size()) {
            sender.sendMessage(ChatColor.RED + "No mine exists with that ID.");
            return true;
        }

        Mine mine = main.activeMines.get(id);
        mine.removeFromWorld();
        main.activeMines.remove(id);
        main.saveMines();

        sender.sendMessage(ChatColor.GREEN + "Mine " + id + " has been removed.");
        return false;
    }
}
