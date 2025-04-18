package com.lucacando.impactMines.tools.enums;

import kotlin.text.CharCategory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ImpactItem {

    OAK_WOOD(Material.OAK_WOOD, ChatColor.GOLD, "Oak Wood"),
    STONE(Material.STONE, ChatColor.GRAY, "Stone"),
    COAL(Material.COAL, ChatColor.DARK_GRAY, "Coal"),
    IRON(Material.IRON_INGOT, ChatColor.WHITE, "Iron"),
    COPPER(Material.COPPER_INGOT, ChatColor.GOLD, "Copper"),
    GOLD(Material.GOLD_INGOT, ChatColor.YELLOW, "Gold"),
    REDSTONE(Material.REDSTONE, ChatColor.RED, "Redstone"),
    QUARTZ(Material.QUARTZ, ChatColor.WHITE, "Quartz"),
    LAPIS(Material.LAPIS_LAZULI, ChatColor.BLUE, "Lapis"),
    DIAMOND(Material.DIAMOND, ChatColor.AQUA, "Diamond"),
    EMERALD(Material.EMERALD, ChatColor.GREEN, "Emerald"),

    OAK_BLOCK(Material.OAK_WOOD, ChatColor.GOLD, "Oak Block"),
    STONE_BRICK(Material.STONE_BRICKS, ChatColor.GRAY, "Stone Block"),
    COAL_BLOCK(Material.COAL_BLOCK, ChatColor.DARK_GRAY, "Coal Block"),
    IRON_BLOCK(Material.IRON_BLOCK, ChatColor.WHITE, "Iron Block"),
    COPPER_BLOCK(Material.COPPER_BLOCK, ChatColor.GOLD, "Copper Block"),
    GOLD_BLOCK(Material.GOLD_BLOCK, ChatColor.YELLOW, "Gold Block"),
    REDSTONE_BLOCK(Material.REDSTONE_BLOCK, ChatColor.RED, "Redstone Block"),
    QUARTZ_BLOCK(Material.QUARTZ_BLOCK, ChatColor.WHITE, "Quartz Block"),
    LAPIS_BLOCK(Material.LAPIS_BLOCK, ChatColor.BLUE, "Lapis Block"),
    DIAMOND_BLOCK(Material.DIAMOND_BLOCK, ChatColor.AQUA, "Diamond Block"),
    EMERALD_BLOCK(Material.EMERALD_BLOCK, ChatColor.GREEN, "Emerald Block"),
    GLASS(Material.LIGHT_BLUE_STAINED_GLASS, ChatColor.AQUA, "Glass"),
    BEACON(Material.BEACON, ChatColor.WHITE, "Beacon");

    private Material material;
    private ChatColor nameColor;
    private String name;

    ImpactItem(Material material, ChatColor nameColor, String name) {
        this.material = material;
        this.nameColor = nameColor;
        this.name = nameColor + name;
    }

    public ItemStack getBlock(CompactLevel compactLevel) {
        ItemStack impactBlock = new ItemStack(material);
        String name = compactLevel.getCompactedName(this);
        ItemMeta meta = impactBlock.getItemMeta();
        meta.setDisplayName(name);
        if (compactLevel.getCompactedLevel() > 0) {
            meta.addEnchant(Enchantment.MENDING, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        impactBlock.setItemMeta(meta);
        return impactBlock;
    }

    public ItemStack getBlock(CompactLevel compactLevel, int amount) {
        ItemStack impactBlock = new ItemStack(material, amount);
        String name = compactLevel.getCompactedName(this);
        ItemMeta meta = impactBlock.getItemMeta();
        meta.setDisplayName(name);
        if (compactLevel.getCompactedLevel() > 0) {
            meta.addEnchant(Enchantment.MENDING, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        impactBlock.setItemMeta(meta);
        return impactBlock;
    }

    public Material getMaterial() {
        return material;
    }

    public ChatColor getNameColor() {
        return nameColor;
    }

    public String getName() {
        return name;
    }
}
