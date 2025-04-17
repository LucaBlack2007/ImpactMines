package com.lucacando.impactMines.tools;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.tools.enums.CompactLevel;
import com.lucacando.impactMines.tools.enums.ImpactItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCompacted implements CommandExecutor {

    Main main;

    public GiveCompacted(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (ImpactItem value : ImpactItem.values()) {
                player.sendMessage(String.valueOf(value));
            }
            for (CompactLevel value : CompactLevel.values()) {
                player.sendMessage(String.valueOf(value));
            }
            ItemStack item = ImpactItem.valueOf(args[0]).getBlock(CompactLevel.valueOf(args[1]));
            player.getInventory().addItem(item);
        }

        return false;
    }
}
