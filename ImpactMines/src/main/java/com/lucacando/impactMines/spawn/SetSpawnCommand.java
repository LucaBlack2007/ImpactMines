package com.lucacando.impactMines.spawn;

import com.lucacando.impactMines.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    Main main;

    public SetSpawnCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (main.getPlayerRank(player).getPermissionLevel() < 5) {
            player.sendMessage(main.noPermission);
            return true;
        }

        World world = player.getWorld();
        Location location = player.getLocation();

        main.getConfig().set("spawn.world", world.getUID());
        main.getConfig().set("spawn.x", location.getX());
        main.getConfig().set("spawn.y", location.getY());
        main.getConfig().set("spawn.z", location.getZ());
        main.getConfig().set("spawn.pitch", location.getPitch());
        main.getConfig().set("spawn.yaw", location.getYaw());

        main.saveConfig();

        player.sendMessage(main.prefix + "Spawn location set to your location!");

        return false;
    }
}
