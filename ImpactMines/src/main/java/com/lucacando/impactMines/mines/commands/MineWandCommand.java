package com.lucacando.impactMines.mines.commands;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MineWandCommand implements CommandExecutor {

    Main main;

    public MineWandCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Must be a player to run this command.");
            return true;
        }
        Player player = (Player) sender;

        if (main.getPlayerRank(player).getPermissionLevel() <= 4) {
            player.sendMessage(main.noPermission);
            return true;
        }

        ItemStack mineWand = new ItemStack(Material.ARROW);
        ItemMeta meta = mineWand.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Mine Wand");
        mineWand.setItemMeta(meta);

        player.getInventory().addItem(mineWand);
        player.sendMessage(main.prefix + "You have received the Mine Wand!");
        return false;
    }
}