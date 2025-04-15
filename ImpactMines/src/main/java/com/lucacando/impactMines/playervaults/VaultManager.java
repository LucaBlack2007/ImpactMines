package com.lucacando.impactMines.playervaults;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VaultManager {
    private JavaPlugin plugin;
    private File vaultsFile;
    private FileConfiguration vaultsConfig;
    private Map<UUID, Map<Integer, Inventory>> vaults;

    public VaultManager(JavaPlugin plugin) {
        this.plugin = plugin;
        vaults = new HashMap<>();
        vaultsFile = new File(plugin.getDataFolder(), "vaults.yml");
        if (!vaultsFile.exists()) {
            vaultsFile.getParentFile().mkdirs();
            try {
                vaultsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vaultsConfig = YamlConfiguration.loadConfiguration(vaultsFile);
    }

    public Inventory getVault(Player player, int vaultNumber) {
        UUID uuid = player.getUniqueId();
        if (!vaults.containsKey(uuid)) {
            vaults.put(uuid, new HashMap<>());
        }
        Map<Integer, Inventory> playerVaults = vaults.get(uuid);
        if (!playerVaults.containsKey(vaultNumber)) {
            String path = uuid.toString() + ".vault" + vaultNumber;
            if (vaultsConfig.contains(path)) {
                String invString = vaultsConfig.getString(path);
                try {
                    Inventory inv = inventoryFromBase64(invString, "Vault " + vaultNumber);
                    playerVaults.put(vaultNumber, inv);
                    return inv;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Inventory inv = Bukkit.createInventory(null, 54, "Vault " + vaultNumber);
            playerVaults.put(vaultNumber, inv);
            return inv;
        } else {
            return playerVaults.get(vaultNumber);
        }
    }

    public void saveVault(Player player, int vaultNumber, Inventory inventory) {
        UUID uuid = player.getUniqueId();
        String path = uuid.toString() + ".vault" + vaultNumber;
        try {
            String invString = inventoryToBase64(inventory);
            vaultsConfig.set(path, invString);
            vaultsConfig.save(vaultsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAllVaults() {
        for (Map.Entry<UUID, Map<Integer, Inventory>> entry : vaults.entrySet()) {
            UUID uuid = entry.getKey();
            Map<Integer, Inventory> playerVaults = entry.getValue();
            for (Map.Entry<Integer, Inventory> vaultEntry : playerVaults.entrySet()) {
                int vaultNumber = vaultEntry.getKey();
                Inventory inv = vaultEntry.getValue();
                String path = uuid.toString() + ".vault" + vaultNumber;
                try {
                    String invString = inventoryToBase64(inv);
                    vaultsConfig.set(path, invString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            vaultsConfig.save(vaultsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String inventoryToBase64(Inventory inventory) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("size", inventory.getSize());
        for (int i = 0; i < inventory.getSize(); i++){
            config.set("items." + i, inventory.getItem(i));
        }
        return config.saveToString();
    }

    public static Inventory inventoryFromBase64(String data, String title) throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(data);
        } catch (org.bukkit.configuration.InvalidConfigurationException e) {
            throw new IOException("Failed to load configuration from vault data: " + e.getMessage(), e);
        }
        int size = config.getInt("size", 54);
        Inventory inventory = Bukkit.createInventory(null, size, title);
        for (int i = 0; i < size; i++){
            inventory.setItem(i, config.getItemStack("items." + i));
        }
        return inventory;
    }

}
