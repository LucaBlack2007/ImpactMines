package com.lucacando.impactMines.playervaults;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerVaultCommand implements CommandExecutor {
    private Main main;
    private VaultManager vaultManager;

    public PlayerVaultCommand(Main main, VaultManager vaultManager) {
        this.main = main;
        this.vaultManager = vaultManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.prefix + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(main.prefix + ChatColor.RED + "Usage: /pv <vault_number>");
            return true;
        }
        int vaultNumber;
        try {
            vaultNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage(main.prefix + ChatColor.RED + "You must enter a valid number.");
            return true;
        }
        if (vaultNumber < 1 || vaultNumber > 20) {
            player.sendMessage(main.prefix + ChatColor.RED + "Vault number must be between 1 and " + main.getPlayerRank(player).getVaults() + ".");
            return true;
        }
        if (vaultNumber > main.getPlayerRank(player).getVaults()) {
            player.sendMessage(main.prefix + ChatColor.RED + "Vault number but be " + main.getPlayerRank(player).getVaults() + " or lower due to your " + main.getPlayerRank(player).getColor() + main.getPlayerRank(player).getName() + ChatColor.RED + " rank.");
            return true;
        }
        Inventory vaultInv = vaultManager.getVault(player, vaultNumber);
        player.openInventory(vaultInv);
        return true;
    }
}
