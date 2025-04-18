package com.lucacando.impactMines.tools.enums;

import org.bukkit.ChatColor;

public enum Rarity {
    /*
    -> &4&lARCANE -> &x&0&8&D&4&F&B&lC&x&0&7&B&C&F&C&lE&x&0&6&A&3&F&C&lL&x&0&5&8&B&F&D&lE&x&0&4&7&3&F&D&lS&x&0&3&5&A&F&E&lT&x&0&2&4&2&F&E&lI&x&0&1&2&9&F&F&lA&x&0&0&1&1&F&F&lL -> &x&F&B&0&8&0&8&lG&x&F&C&2&7&0&7&lO&x&F&C&4&7&0&5&lD&x&F&D&6&6&0&4&lL&x&F&E&8&5&0&3&lI&x&F&E&A&5&0&1&lK&x&F&F&C&4&0&0&lE
     */

    WOODEN("&x&9&6&4&B&0&0&lWOODEN", "&x&9&6&4&B&0&0"),
    COMMON("&7&lCOMMON", "&7"),
    UNCOMMON("&x&6&d&d&3&9&d&lUNCOMMON", "&x&6&d&d&3&9&d"),
    RARE("&x&9&e&2&f&e&c&lRARE", "&x&9&e&2&f&e&c"),
    SUPERIOR("&6&lSUPERIOR", "&6"),
    MYTHIC("&d&lMYTHIC", "&d"),
    DIVINE("&b&lDIVINE", "&b"),
    ARCANE("&4&lARCANE", "&4"),
    CELESTIAL("&x&0&8&D&4&F&B&lC&x&0&7&B&C&F&C&lE&x&0&6&A&3&F&C&lL&x&0&5&8&B&F&D&lE&x&0&4&7&3&F&D&lS&x&0&3&5&A&F&E&lT&x&0&2&4&2&F&E&lI&x&0&1&2&9&F&F&lA&x&0&0&1&1&F&F&lL", "&b"),
    GODLIKE("&x&F&B&0&8&0&8&lG&x&F&C&2&7&0&7&lO&x&F&C&4&7&0&5&lD&x&F&D&6&6&0&4&lL&x&F&E&8&5&0&3&lI&x&F&E&A&5&0&1&lK&x&F&F&C&4&0&0&lE", "&4");

    private String name;
    private String color;

    Rarity(String name, String color) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.color = ChatColor.translateAlternateColorCodes('&', color);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
