package com.lucacando.impactMines.spawn;

import com.lucacando.impactMines.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    Main main;

    public SpawnCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        // yaw pitch
        Location spawnLoc = main.getSpawn();

        player.teleport(spawnLoc);

        return false;
    }
}
