package com.lucacando.impactMines.admin;

import com.lucacando.impactMines.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadPlayersCommand implements CommandExecutor {

    Main main;

    public ReloadPlayersCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.isOp()) {
            Bukkit.getOnlinePlayers().forEach(p -> main.instantiatePlayer(p));
            sender.sendMessage(main.prefix + "All players reloaded.");
        }

        return false;
    }
}
