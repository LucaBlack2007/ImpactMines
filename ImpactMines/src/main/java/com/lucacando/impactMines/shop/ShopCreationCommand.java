package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

public class ShopCreationCommand implements CommandExecutor {

    Main main;

    public ShopCreationCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "You must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;

        if (main.getPlayerRank(player).getPermissionLevel() < 5) {
            player.sendMessage(main.noPermission);
            return true;
        }

        String id = "";
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i < args.length - 1)
                b.append(args[i] + " ");
            else
                b.append(args[i]);
        }

        id = b.toString();

        if (id.equals("")) {
            id = "shop @ " + Math.random() * 1000000;
        }

        ShopManager manager = new ShopManager(main, main);
        Shop shop = manager.createShop(id);

        main.shops.add(shop);
        player.openInventory(shop.getShopGUI());

        return false;
    }
}
