package com.lucacando.impactMines;

import com.lucacando.impactMines.punishments.PunishmentManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarningsCommand implements CommandExecutor {

    Main main;
    PunishmentManager manager;

    public WarningsCommand(Main main) {
        this.main = main;
        manager = main.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }
}
