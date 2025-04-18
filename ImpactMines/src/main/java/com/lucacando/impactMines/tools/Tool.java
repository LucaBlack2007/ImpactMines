package com.lucacando.impactMines.tools;

import com.lucacando.impactMines.Main;
import com.lucacando.impactMines.tools.enums.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Tool {

    private final ItemStack item;
    private final Main main;

    // new Tool(this, Material.WOODEN_AXE, "Wooden Axe", Rarity.COMMON, 1, 0, 1);
    public Tool(Main main, Material material, String name, Rarity rarity, int efficiencyLevel, int fortuneLevel, int tier) {
        this.main = main;
        this.item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§8§l[§eT" + tier + "§8§l] " + rarity.getColor() + name);

        // Lore
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.GREEN + " ");
        if (efficiencyLevel > 0) itemLore.add("§8§l⏩ §aEfficiency " + efficiencyLevel);
        if (fortuneLevel > 0) itemLore.add("§8§l⏩ §bFortune "  + fortuneLevel);
        if (fortuneLevel + efficiencyLevel > 0) itemLore.add(ChatColor.GOLD + " ");
        itemLore.add(rarity.getName());
        meta.setLore(itemLore);

        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.EFFICIENCY, efficiencyLevel, true);
        meta.addEnchant(Enchantment.FORTUNE, fortuneLevel, true);
        meta.addItemFlags(
                org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS,
                org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE,
                org.bukkit.inventory.ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
                org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES
        );

        // Hide attributes
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(NamespacedKey.minecraft("hide_damage"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(NamespacedKey.minecraft("hide_speed"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
        meta.addAttributeModifier(Attribute.MINING_EFFICIENCY, new AttributeModifier(NamespacedKey.minecraft("hide_mining"), 0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));

        item.setItemMeta(meta);
    }

    public ItemStack getItem() {
        return item;
    }
}