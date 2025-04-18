package com.lucacando.impactMines.tools;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.tools.enums.CompactLevel;
import com.lucacando.impactMines.tools.enums.ImpactItem;
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
            int lowerBound = (fortune - 2) > 0 ? (fortune) : 2;
            int random = lowerBound * new Random().nextInt(fortune+2);
            amount = random;
        }
        if (amount == 0) amount = 1;

        // fortune 5 -> 3x 6;

        // fortune - 2 -> fortune + 1


        blocks.put(Material.OAK_WOOD, ImpactItem.OAK_WOOD.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.STRIPPED_OAK_WOOD, ImpactItem.OAK_WOOD.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.STONE, ImpactItem.STONE.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.SMOOTH_STONE, ImpactItem.STONE.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.COAL_ORE, ImpactItem.COAL.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.COAL_BLOCK, ImpactItem.COAL.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.IRON_ORE, ImpactItem.IRON.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.RAW_IRON_BLOCK, ImpactItem.IRON.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.COPPER_ORE, ImpactItem.COPPER.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.RAW_COPPER_BLOCK, ImpactItem.COPPER.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.GOLD_ORE, ImpactItem.GOLD.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.RAW_GOLD_BLOCK, ImpactItem.GOLD.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.REDSTONE_ORE, ImpactItem.REDSTONE.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.DEEPSLATE_REDSTONE_ORE, ImpactItem.REDSTONE.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.NETHER_QUARTZ_ORE, ImpactItem.QUARTZ.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.QUARTZ_BLOCK, ImpactItem.QUARTZ.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.LAPIS_ORE, ImpactItem.LAPIS.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.DEEPSLATE_LAPIS_ORE, ImpactItem.LAPIS.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.DIAMOND_ORE, ImpactItem.DIAMOND.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.DEEPSLATE_DIAMOND_ORE, ImpactItem.DIAMOND.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.EMERALD_ORE, ImpactItem.EMERALD.getBlock(CompactLevel.NONE, amount));
        blocks.put(Material.DEEPSLATE_EMERALD_ORE, ImpactItem.EMERALD.getBlock(CompactLevel.NONE, amount * 4));

        blocks.put(Material.LIGHT_BLUE_STAINED_GLASS, ImpactItem.BEACON.getBlock(CompactLevel.NONE, 4));
        blocks.put(Material.BEACON, ImpactItem.BEACON.getBlock(CompactLevel.NONE, 1));


        return blocks;
    }

}
