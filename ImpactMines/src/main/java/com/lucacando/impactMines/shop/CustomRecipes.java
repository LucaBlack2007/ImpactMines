package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.tools.enums.CompactLevel;
import com.lucacando.impactMines.tools.enums.ImpactItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CustomRecipes implements Listener {

    Main main;
    private final Set<Material> compactedMaterials = new HashSet<>();

    public CustomRecipes(Main main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        Bukkit.clearRecipes();

        List<ImpactItem> baseItems = Arrays.asList(
                ImpactItem.OAK_WOOD, ImpactItem.STONE,
                ImpactItem.IRON, ImpactItem.COPPER,
                ImpactItem.GOLD, ImpactItem.REDSTONE,
                ImpactItem.LAPIS, ImpactItem.DIAMOND
        );

        List<ImpactItem> items = Arrays.asList(
                ImpactItem.OAK_BLOCK, ImpactItem.STONE_BRICK,
                ImpactItem.IRON_BLOCK, ImpactItem.COPPER_BLOCK,
                ImpactItem.GOLD_BLOCK, ImpactItem.REDSTONE_BLOCK,
                ImpactItem.LAPIS_BLOCK, ImpactItem.DIAMOND_BLOCK
        );

        registerBlockRecipes(items, baseItems);
        registerCompactedBlockRecipes(items, baseItems);
        registerAllCompactedBlockRecipes(items);
        registerReverseCrafts(items, baseItems);
    }

    private void registerReverseCrafts(List<ImpactItem> items, List<ImpactItem> baseItems) {
        int index = 0;
        for (ImpactItem item : items) {
            CompactLevel[] levels = CompactLevel.values();
            for (int i = levels.length - 1; i >= 0; i--) {
                ItemStack material = item.getBlock(levels[i]);
                ItemStack product = (i == 0) ?
                        baseItems.get(index).getBlock(CompactLevel.NONE) :
                        item.getBlock(levels[i-1]);
                product.setAmount(8);

                NamespacedKey key = new NamespacedKey(main, String.valueOf(Math.random() * 10000000));
                ShapelessRecipe recipe = new ShapelessRecipe(key, product);
                recipe.addIngredient(new RecipeChoice.ExactChoice(material));
                Bukkit.addRecipe(recipe);
            }
            index++;
        }
    }

    private void registerAllCompactedBlockRecipes(List<ImpactItem> items) {
        for (ImpactItem item : items) {
            for (int i = 1; i < CompactLevel.values().length; i++) {
                CompactLevel level = CompactLevel.values()[i];
                NamespacedKey key = new NamespacedKey(main, String.valueOf(Math.random() * 10000000));
                if (level.getCompactedLevel() >= 1) {
                    CompactLevel matLevel = CompactLevel.values()[i-1];
                    ItemStack matItem = item.getBlock(matLevel);
                    matItem.setAmount(8);

                    ShapedRecipe recipe = new ShapedRecipe(key, item.getBlock(level));
                    recipe.shape("XXX", "XXX", "XXX");
                    recipe.setIngredient('X', new RecipeChoice.ExactChoice(matItem));
                    Bukkit.addRecipe(recipe);

                    // Track result material for event handling
                    compactedMaterials.add(item.getBlock(level).getType());

                    Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                            ChatColor.RED + level + " " + ChatColor.RED + item.getName() +
                            ChatColor.BLUE + " registered.");
                }
            }
        }
    }

    private void registerBlockRecipes(List<ImpactItem> items, List<ImpactItem> baseItems) {
        for (int i = 0; i < baseItems.size(); i++) {
            ImpactItem matImpactItem = baseItems.get(i);
            ItemStack matItem = matImpactItem.getBlock(CompactLevel.NONE);
            matItem.setAmount(8);
            NamespacedKey key = new NamespacedKey(main, String.valueOf(Math.random() * 10000000));
            ShapedRecipe recipe = new ShapedRecipe(key, items.get(i).getBlock(CompactLevel.NONE));
            recipe.shape("XXX", "XXX", "XXX");
            recipe.setIngredient('X', new RecipeChoice.ExactChoice(matItem));
            Bukkit.addRecipe(recipe);

            compactedMaterials.add(items.get(i).getBlock(CompactLevel.NONE).getType());

            Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                    ChatColor.RED + items.get(i).getName() +
                    ChatColor.BLUE + " registered.");
        }
    }

    private void registerCompactedBlockRecipes(List<ImpactItem> items, List<ImpactItem> baseItems) {
        for (int i = 0; i < baseItems.size(); i++) {
            ImpactItem matImpactItem = items.get(i);
            ItemStack matItem = matImpactItem.getBlock(CompactLevel.NONE);
            matItem.setAmount(8);
            NamespacedKey key = new NamespacedKey(main, String.valueOf(Math.random() * 10000000));
            ShapedRecipe recipe = new ShapedRecipe(key, items.get(i).getBlock(CompactLevel.COMPACTED));
            recipe.shape("XXX", "XXX", "XXX");
            recipe.setIngredient('X', new RecipeChoice.ExactChoice(matItem));
            Bukkit.addRecipe(recipe);

            compactedMaterials.add(items.get(i).getBlock(CompactLevel.COMPACTED).getType());

            Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                    ChatColor.RED + "COMPACTED " + ChatColor.RED + items.get(i).getName() +
                    ChatColor.BLUE + " registered.");
        }
    }


}
