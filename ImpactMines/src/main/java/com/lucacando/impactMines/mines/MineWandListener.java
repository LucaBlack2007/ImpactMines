package com.lucacando.impactMines.mines;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MineWandListener implements Listener {

    private final Main main;

    public MineWandListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || item.getItemMeta().getDisplayName() == null) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GRAY + "Mine Wand")) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null)
            return;
        Location loc = clickedBlock.getLocation();

        UUID uuid = player.getUniqueId();
        Selection sel;
        if (!main.selections.containsKey(uuid)) {
            sel = new Selection();
            main.selections.put(uuid, sel);
        } else {
            sel = main.selections.get(uuid);
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            sel.setPos1(loc);
            player.sendMessage(main.prefix + ChatColor.YELLOW + "First position set.");
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            sel.setPos2(loc);
            player.sendMessage(main.prefix + ChatColor.YELLOW + "Second position set.");
        }
        if (sel.isComplete()) {
            int volume = sel.getVolume();
            player.sendMessage(main.prefix + ChatColor.AQUA + "Selection complete: " + volume + " blocks.");
        }
        event.setCancelled(true);
    }
}
