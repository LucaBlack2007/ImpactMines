package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShopManager implements Listener {

    Main main;
    JavaPlugin plugin;

    public ShopManager(JavaPlugin plugin, Main main) {
        this.main = main;
        this.plugin = plugin;
    }

    public static List<Integer> grayPanes = Arrays.asList(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 17,
            35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    );
    public static List<Integer> whitePanes = Arrays.asList(
            45, 46, 47, 48, 49, 50, 51, 52, 53
    );
    public static List<Integer> greenPanes = Arrays.asList(
            9, 10, 11,
            18,     20,
            27, 28, 29
    );

    public Shop createShop(String id) {
        Inventory shopCreation = Bukkit.createInventory(null, 54, ChatColor.BLUE + "Creating shop: " + ChatColor.GRAY + id);

        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta0 = grayPane.getItemMeta();
        meta0.setDisplayName(" ");
        grayPane.setItemMeta(meta0);

        ItemStack whitePane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta meta1 = whitePane.getItemMeta();
        meta1.setDisplayName(" ");
        whitePane.setItemMeta(meta1);

        ItemStack greenPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta2 = greenPane.getItemMeta();
        meta2.setDisplayName(" ");
        greenPane.setItemMeta(meta2);

        ItemStack selectItem = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta selectItemMeta = selectItem.getItemMeta();
        selectItemMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() +
                "Create this trade");
        selectItemMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Press this to create a trade.",
                ChatColor.BLUE + " ",
                ChatColor.BLUE + "Item: " + ChatColor.RED + "None",
                ChatColor.BLUE + "Trade: " + ChatColor.RED + "None"
        ));
        selectItem.setItemMeta(selectItemMeta);

        grayPanes.forEach(pane -> shopCreation.setItem(pane, grayPane));
        whitePanes.forEach(pane -> shopCreation.setItem(pane, whitePane));
        greenPanes.forEach(pane -> shopCreation.setItem(pane, greenPane));
        shopCreation.setItem(26, selectItem);

        HashMap<ItemStack, ArrayList<ItemStack>> trades = new HashMap<>();
        return new Shop(shopCreation, trades, id);
    }
}
