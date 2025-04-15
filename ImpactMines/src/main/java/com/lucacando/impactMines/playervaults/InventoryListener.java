package com.lucacando.impactMines.playervaults;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {
    private VaultManager vaultManager;

    public InventoryListener(VaultManager vaultManager) {
        this.vaultManager = vaultManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        String title = event.getView().getTitle();
        if (title.startsWith("Vault ")) {
            try {
                String[] parts = title.split(" ");
                int vaultNumber = Integer.parseInt(parts[1]);
                vaultManager.saveVault(player, vaultNumber, event.getInventory());
            } catch (Exception e) {
            }
        }
    }
}
