package com.lucacando.impactMines.listeners;

import com.lucacando.impactMines.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockBreakListener implements Listener {

    Main main;

    public BlockBreakListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (player.getInventory().getItemInMainHand() == null) return;
        if (!main.isTool(player.getInventory().getItemInMainHand())) return;

        Map<Material, ItemStack> blocks = translateBlocks(player);
        if (!blocks.containsKey(e.getBlock().getType())) return;

        e.setDropItems(false);
        e.getPlayer().getInventory().addItem(blocks.get(e.getBlock().getType()));
    }

    public HashMap<Material, ItemStack> translateBlocks(Player player) {
        HashMap<Material, ItemStack> blocks = new HashMap<>();

        ItemStack itemInHand = player.getEquipment().getItemInMainHand();
        int fortune = itemInHand.getEnchantmentLevel(Enchantment.FORTUNE);
        int amount = 1;
        if (fortune != 0) {
            int lowerBound = (fortune - 2) > 0 ? (fortune - 2) : 1;
            int random = lowerBound * new Random().nextInt(fortune);
            amount = random;
        }
        if (amount == 0) amount = 1;

        // fortune 5 -> 3x 6x

        // fortune - 2 -> fortune + 1


        blocks.put(Material.OAK_WOOD, new ItemStack(Material.OAK_WOOD, amount));
        blocks.put(Material.STRIPPED_OAK_WOOD, new ItemStack(Material.OAK_WOOD, 4 * amount));
        blocks.put(Material.STONE, new ItemStack(Material.STONE, amount));
        blocks.put(Material.SMOOTH_STONE, new ItemStack(Material.STONE, 4 * amount));
        blocks.put(Material.COAL_ORE, new ItemStack(Material.COAL, amount));
        blocks.put(Material.COAL_BLOCK, new ItemStack(Material.COAL, 4 * amount));
        blocks.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT, amount));
        blocks.put(Material.RAW_IRON_BLOCK, new ItemStack(Material.IRON_INGOT, 4 * amount));
        blocks.put(Material.COPPER_ORE, new ItemStack(Material.COPPER_INGOT, amount));
        blocks.put(Material.RAW_COPPER_BLOCK, new ItemStack(Material.COPPER_INGOT, 4 * amount));
        blocks.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT, amount));
        blocks.put(Material.RAW_GOLD_BLOCK, new ItemStack(Material.GOLD_INGOT, 4 * amount));
        blocks.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE, amount));
        blocks.put(Material.DEEPSLATE_REDSTONE_ORE, new ItemStack(Material.REDSTONE, 4 * amount));
        blocks.put(Material.LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, amount));
        blocks.put(Material.DEEPSLATE_LAPIS_ORE, new ItemStack(Material.LAPIS_LAZULI, 4 * amount));
        blocks.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND, amount));
        blocks.put(Material.DEEPSLATE_DIAMOND_ORE, new ItemStack(Material.DIAMOND, 4 * amount));

        return blocks;
    }
}
