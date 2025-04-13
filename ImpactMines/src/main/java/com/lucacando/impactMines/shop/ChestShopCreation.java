package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class ChestShopCreation implements Listener {

    Main main;

    public ChestShopCreation(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onClickChest(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (player.getEquipment().getItemInMainHand().getType().equals(Material.STICK)) {
                if (e.getClickedBlock().getState() instanceof Chest) {
                    e.setCancelled(true);
                    Chest chest = (Chest) e.getClickedBlock().getState();
                    Inventory chestInventory = chest.getInventory();
                    if (chestInventory.getItem(0) == null) return;
                    ItemStack egg = chestInventory.getItem(0);
                    if (egg != null && egg.getType().name().endsWith("_SPAWN_EGG")) {
                        // Convert the material name to an EntityType safely
                        String entityName = egg.getType().name().replace("_SPAWN_EGG", "");
                        EntityType type = EntityType.valueOf(entityName); // use valueOf for strict match

                        if (type.isSpawnable() && type.isAlive()) {
                            Entity entity = chest.getWorld().spawnEntity(chest.getLocation().add(0.5, 1, 0.5), type);
                            entity.setInvulnerable(true);
                            entity.setGravity(false);
                        }
                    }
                }
            }
        }
    }
}
