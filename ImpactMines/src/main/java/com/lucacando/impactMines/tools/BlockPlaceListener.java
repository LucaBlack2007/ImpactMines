package com.lucacando.impactMines.tools;

import com.lucacando.impactMines.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    Main main;

    public BlockPlaceListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                e.setCancelled(true);
            }
        }
    }
}
