package com.lucacando.impactMines.admin.tools;

import com.lucacando.impactMines.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ToolCommand implements CommandExecutor, Listener {

    Main main;
    String usage;

    public ToolCommand(Main main, String usage) {
        this.main = main;
        this.usage = main.prefix + usage;
    }
    public ToolCommand(Main main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Must be a player to run this command.");
            return true;
        }

        Player player = (Player) sender;
        if (!main.isStaff(player)) {
            player.sendMessage(main.noPermission);
            return true;
        }

        Player given = player;

        if (args.length > 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                given = target;
            } else {
                player.sendMessage(main.prefix + "Player is not online.");
                return true;
            }
        }

        Inventory gui = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Tool Selection Menu");

        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        glass.getItemMeta().setDisplayName(" ");
        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                45, 46, 47, 48, 49, 50, 51, 52, 53,
                9, 18, 27, 36,
                17, 26, 35, 44
        };
        for (int slot : borderSlots) gui.setItem(slot, glass);


        for (Tool t : main.tools) gui.addItem(t.getItem());
        player.openInventory(gui);
        return false;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        if (e.getView().getTitle().contains("Tool Selection Menu")) {
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item != null && item.getType() != Material.AIR) {
                if (item.getItemMeta().getDisplayName() != " ") {
                    Player p = (Player) e.getWhoClicked();
                    e.setCancelled(true);
                    p.getInventory().addItem(item.clone());
                    p.sendMessage(main.prefix + item.getItemMeta().getDisplayName() + ChatColor.BLUE + " given to you.");
                }
            }
        }
    }
}