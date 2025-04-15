package com.lucacando.impactMines.ranks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.lucacando.impactMines.Main;

import java.util.Arrays;
import java.util.UUID;

public class RankCommand implements CommandExecutor, Listener {

    Main main;
    String usage;

    public RankCommand(Main main, String usage) {
        this.main = main;
        this.usage = main.prefix + usage;
    }
    public RankCommand(Main main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            if (args.length < 2) {
                sender.sendMessage(main.prefix + "please specify a player and a rank.");
                return true;
            }

            OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
            if (targetPlayer == null || (!targetPlayer.hasPlayedBefore() && Bukkit.getPlayer(args[0]) == null)) {
                sender.sendMessage(usage);
                return true;
            }

            String ranks = "";
            for (Rank r : Rank.values()) {
                ranks += "\n" + r.getColor().toString() + r.getName();
                if (r.getName().equalsIgnoreCase(args[1])) {
                    main.setPlayerRank(targetPlayer.getPlayer(), r);
                    sender.sendMessage(main.prefix + ChatColor.YELLOW + targetPlayer.getName() + "'s" + ChatColor.GREEN + " Rank set to " + r.getColor().toString() + r.getName());
                    return true;
                }
            }
            sender.sendMessage(main.prefix + "Please select a proper rank: " + ChatColor.RESET + ranks);
        }


        if (sender instanceof Player) {
            Player p = (Player) sender;
            int permLevel = main.getPermission(p);
            if (permLevel >= 4) {
                if (args.length == 2) {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (targetPlayer == null || (!targetPlayer.hasPlayedBefore() && Bukkit.getPlayer(args[0]) == null)) {
                        p.sendMessage(usage);
                        return true;
                    }

                    if (main.getPermission(targetPlayer.getPlayer()) >= permLevel) {
                        p.sendMessage(main.prefix + ChatColor.RED + "You can't change this player's rank as it's the same or higher than yours.");
                        return true;
                    }

                    String ranks = "";
                    for (Rank r : Rank.values()) {
                        ranks += "\n" + r.getColor().toString() + r.getName();
                        if (r.getName().equalsIgnoreCase(args[1])) {
                            if (r.getPermissionLevel() >= permLevel) {
                                p.sendMessage(main.prefix +ChatColor.RED + "You can't set player to " + r.getColor() + r.getName() + ChatColor.RED + " as it's the same or higher than your own rank.");
                                return true;
                            }
                            main.setPlayerRank(targetPlayer.getPlayer(), r);
                            p.sendMessage(main.prefix + ChatColor.YELLOW + targetPlayer.getName() + "'s" + ChatColor.GREEN + " Rank set to " + r.getColor().toString() + r.getName());
                            return true;
                        }
                    }
                    p.sendMessage(main.prefix + "Please select a proper rank: " + ChatColor.RESET + ranks);
                } else if (args.length == 1) {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);
                    if (targetPlayer == null || (!targetPlayer.hasPlayedBefore() && Bukkit.getPlayer(args[0]) == null)) {
                        p.sendMessage(usage);
                        //p.sendMessage(String.valueOf(targetPlayer.hasPlayedBefore()));
                        return true;
                    }

                    if (main.getPermission(targetPlayer.getPlayer()) >= permLevel) {
                        p.sendMessage(main.prefix + ChatColor.RED + "You can't change this player's rank as it's the same or higher than yours.");
                        return true;
                    }

                    main.selectedTemp.put(p.getUniqueId(), targetPlayer.getUniqueId());
                    //p.sendMessage(targetPlayer.getUniqueId().toString());
                    //p.sendMessage("that didnt't work");
                    //p.sendMessage(main.selectedTem.get(p.getUniqueId()).toString());

                    Rank rank = main.getPlayerRank(targetPlayer.getPlayer());
                    String name = targetPlayer.getName();

                    Inventory rankGUI = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "Grant rank to " + rank.getColor().toString() + name);
                    //p.openInventory(rankGUI);

                    for (int i = 0; i < rankGUI.getSize(); i++) {
                        if (i < 9 || i >= 45 || i % 9 == 0 || i % 9 == 8) {
                            rankGUI.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, " "));
                        }
                    }

                    // Add ranks as colored wool
                    Rank[] ranks = Rank.values();
                    int[] validSlots = {
                            10, 11, 12, 13, 14, 15, 16,
                            19, 20, 21, 22, 23, 24, 25,
                            28, 29, 30, 31, 32, 33, 34,
                            37, 38, 39, 40, 41, 42, 43
                    };

                    for (int i = 0; i < ranks.length; i++) {
                        Rank r = ranks[i];
                        ItemStack rankItem = createRankItem(r);

                        if (i < validSlots.length) {
                            rankGUI.setItem(validSlots[i], rankItem); // Place rank items in valid slots
                        }
                    }

                    p.openInventory(rankGUI);


                } else {
                    p.sendMessage(usage);
                    return true;
                }
            } else {
                p.sendMessage(main.noPermission);
            }
        }

        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("Grant rank to ")) return;
        event.setCancelled(true); // Prevent taking items from the GUI

        Player player = (Player) event.getWhoClicked();
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null || currentItem.getType() == Material.AIR) return;

        // Ensure the clicked item is a rank item
        for (Rank rank : Rank.values()) {
            if (currentItem.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + rank.getName())) {

                //player.sendMessage(player.getUniqueId().toString());
                UUID targetUUID = main.selectedTemp.get(player.getUniqueId());
                //player.sendMessage(targetUUID.toString());
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetUUID);
                if (targetPlayer == null || (!targetPlayer.hasPlayedBefore() && Bukkit.getPlayer(targetUUID) == null)) {
                    return;
                }

                if (rank.getPermissionLevel() >= main.getPermission(player)) {
                    player.sendMessage(main.prefix + ChatColor.RED + "You can't set player to " + rank.getColor() + rank.getName() + ChatColor.RED + " as it's the same or higher than your own rank.");
                    return;
                }

                main.setPlayerRank(targetPlayer.getPlayer(), rank);
                player.sendMessage(main.prefix + ChatColor.GREEN + "Assigned rank " + rank.getColor().toString() + rank.getName() + ChatColor.GREEN + " to " + ChatColor.YELLOW + targetPlayer.getName());

                player.closeInventory();
                break; //
            }
        }
    }

    private ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createRankItem(Rank rank) {
        ItemStack item = new ItemStack(rank.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_AQUA + rank.getName());
        meta.setLore(Arrays.asList(ChatColor.YELLOW + "Prefix: " + rank.getColor() + rank.getPrefix()));
        item.setItemMeta(meta);
        return item;
    }
}
