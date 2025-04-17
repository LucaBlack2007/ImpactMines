package com.lucacando.impactMines.shop;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.tools.enums.CompactLevel;
import com.lucacando.impactMines.tools.enums.ImpactItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomRecipes {

    Main main;

    public CustomRecipes(Main main) {
        this.main = main;

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

        for (int i = 0; i < baseItems.size(); i++) {
            ImpactItem matImpactItem = baseItems.get(i);
            ItemStack matItem = matImpactItem.getBlock(CompactLevel.NONE);
            List<ItemStack> craftingMaterials = new ArrayList<>();
            for (int i1 = 0; i1 < 9; i1++) {
                craftingMaterials.add(matItem);
            }

            NamespacedKey key = new NamespacedKey(main, matImpactItem.name() + (i+1)*10);
            ShapelessRecipe recipe = new ShapelessRecipe(key, items.get(i).getBlock(CompactLevel.NONE));
            for (ItemStack craftingMaterial : craftingMaterials) {
                recipe.addIngredient(new RecipeChoice.ExactChoice(craftingMaterial));
            }

            Bukkit.addRecipe(recipe);
            Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                    ChatColor.RED + items.get(i).getName() +
                    ChatColor.BLUE + " registered.");
        }

        for (int i = 0; i < baseItems.size(); i++) {
            ImpactItem matImpactItem = items.get(i);
            ItemStack matItem = matImpactItem.getBlock(CompactLevel.NONE);
            List<ItemStack> craftingMaterials = new ArrayList<>();
            for (int i1 = 0; i1 < 9; i1++) {
                craftingMaterials.add(matItem);
            }

            NamespacedKey key = new NamespacedKey(main, matImpactItem.name() + (i+1)*100);
            ShapelessRecipe recipe = new ShapelessRecipe(key, items.get(i).getBlock(CompactLevel.COMPACTED));
            for (ItemStack craftingMaterial : craftingMaterials) {
                recipe.addIngredient(new RecipeChoice.ExactChoice(craftingMaterial));
            }

            Bukkit.addRecipe(recipe);
            Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                    ChatColor.RED + "COMPACTED " + ChatColor.RED + items.get(i).getName() +
                    ChatColor.BLUE + " registered.");
        }

        for (ImpactItem item : items) {
            for (int i = 1; i < CompactLevel.values().length; i++) {
                CompactLevel level = CompactLevel.values()[i];
                NamespacedKey key = new NamespacedKey(main, item.getMaterial().toString() + level.getCompactedLevel());

                List<ItemStack> craftingMaterials = new ArrayList<>();
                if (level.getCompactedLevel() >= 1) {
                    CompactLevel matLevel = CompactLevel.values()[i-1];
                    ItemStack matItem = item.getBlock(matLevel,matLevel.getCompactedLevel()+1);
                    for (int i1 = 0; i1 < 9; i1++) {
                        craftingMaterials.add(matItem);
                    }

                    ShapelessRecipe recipe = new ShapelessRecipe(key, item.getBlock(level));
                    for (ItemStack craftingMaterial : craftingMaterials) {
                        recipe.addIngredient(new RecipeChoice.ExactChoice(craftingMaterial));
                    }

                    Bukkit.addRecipe(recipe);
                    Bukkit.getConsoleSender().sendMessage(main.prefix + "Recipe for " +
                            ChatColor.RED + level + " " + ChatColor.RED + item.getName() +
                            ChatColor.BLUE + " registered.");
                }
            }
        }
    }

}
