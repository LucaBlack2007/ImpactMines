package com.lucacando.impactMines.mines.commands;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.mines.Mine;
import com.lucacando.impactMines.mines.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FillMineCommand implements CommandExecutor {

    Main main;

    public FillMineCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Only players may use this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(main.prefix + ChatColor.RED + "Usage: /fillmine <block1> <block2>");
            return true;
        }
        Player player = (Player) sender;
        Selection sel = main.selections.get(player.getUniqueId());
        if (sel == null || !sel.isComplete()) {
            player.sendMessage(main.prefix + ChatColor.RED + "You need to make a selection with the Mine Wand first!");
            return true;
        }
        Material block1 = Material.matchMaterial(args[0].toUpperCase());
        Material block2 = Material.matchMaterial(args[1].toUpperCase());
        if (block1 == null || block2 == null) {
            player.sendMessage(main.prefix + ChatColor.RED + "One or both block types are invalid!");
            return true;
        }
        World world = player.getWorld();
        Mine mine = new Mine(world, sel.getPos1(), sel.getPos2(), block1, block2);
        mine.fillInitial();
        main.activeMines.add(mine);
        main.saveMines();
        player.sendMessage(main.prefix + ChatColor.GREEN + "Mine has been filled!");
        main.selections.remove(player.getUniqueId().toString());
        return true;
    }
}