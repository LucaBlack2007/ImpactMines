package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {

    Main main;

    public ShopListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        main.shops.forEach(shop -> {
            Inventory shopGUI = shop.getShopGUI();
            if (e.getView().getTopInventory() == shopGUI) {
                // in the shop gui
                Inventory clickedInventory = e.getClickedInventory();
                int slotNum = e.getSlot();

                if (ShopManager.greenPanes.contains(slotNum) ||
                    ShopManager.grayPanes.contains(slotNum) ||
                    ShopManager.whitePanes.contains(slotNum)
                ) {
                    e.setCancelled(true);
                }

                if (clickedInventory.getItem(slotNum).getType() == Material.GREEN_CONCRETE) {
                    e.setCancelled(true);
                    if (clickedInventory.getItem(19) == null) {
                        player.sendMessage(main.prefix + "No item in the item slot.");
                        return;
                    }

                }
            }
        });
    }
}
