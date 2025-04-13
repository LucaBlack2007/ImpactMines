package com.lucacando.impactMines.ranks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

public enum Rank {

    DEFAULT("DEFAULT", ChatColor.GRAY, "", Material.LIGHT_GRAY_WOOL, false, 0),
    VIP("VIP", ChatColor.GREEN, "&8[&aVIP&8] ", Material.GREEN_WOOL, false, 1),
    MVP("MVP", ChatColor.AQUA, "&8[&bMVP&8] ", Material.LIGHT_BLUE_WOOL, false, 2),

    EXTREME("EXTREME", ChatColor.GOLD, "&8[&6EXTREME&8] ", Material.GOLD_BLOCK, false, 3),
    EPIC("EPIC", ChatColor.DARK_PURPLE, "&8[&5EPIC&8] ", Material.PURPLE_CONCRETE, false, 3),

    MOD("MOD", ChatColor.DARK_GREEN, "&8[&2MOD&8] ", Material.GREEN_WOOL, true, 4),
    BUILDER("BUILDER", ChatColor.YELLOW, "&8[&eBUILDER&8] ", Material.YELLOW_WOOL, true, 4),
    ADMIN("ADMIN", ChatColor.RED, "&8[&cADMIN&8] ", Material.RED_WOOL, true, 5),
    COOWNER("COOWNER",ChatColor.LIGHT_PURPLE, "&8[&d&lCO-OWNER&8] ", Material.PINK_WOOL, true, 9),
    OWNER("OWNER", ChatColor.DARK_RED, "&8[&4&lOWNER&8] ", Material.REDSTONE_BLOCK, true, 10);

    private String name;
    private ChatColor color;
    private String prefix;
    private Material material;
    private boolean isStaff;
    public int permissionLevel;

    Rank(String name, ChatColor color, String prefix, Material material, boolean isStaff, int permissionLevel) {
        this.permissionLevel = permissionLevel;
        this.name = name;
        this.color = color;
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.material = material;
        this.isStaff = isStaff;
    }

    public boolean isStaff() { return isStaff; }
    public String getName() { return name; }
    //public List<Permission> getPermissions() {return permissions; }
    public ChatColor getColor() { return color; }
    public String getPrefix() { return prefix; }
    public Material getMaterial() { return material; }
    public int getPermissionLevel() { return permissionLevel; }

}