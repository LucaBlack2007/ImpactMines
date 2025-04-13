package com.lucacando.impactMines.admin.tools;

import com.lucacando.impactMines.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Tool {

    private final ItemStack item;
    private final Main main;

    public Tool(Main main, Material material, String name, int miningPower, int efficiencyLevel, int fortuneLevel, String... lore) {
        this.main = main;
        this.item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        // Lore
        List<String> itemLore = main.stringsToLore(lore);
        itemLore.add(ChatColor.GREEN + " ");
        itemLore.add(ChatColor.BLUE + "Fortune: " + ChatColor.GOLD + fortuneLevel);
        itemLore.add(ChatColor.BLUE + "Mining Power: " + ChatColor.RED + miningPower);
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