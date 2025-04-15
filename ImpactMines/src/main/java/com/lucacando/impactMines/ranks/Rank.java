package com.lucacando.impactMines.ranks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;

import java.util.Arrays;
import java.util.List;

public enum Rank {

    DEFAULT("DEFAULT", ChatColor.GRAY, "", Material.LIGHT_GRAY_WOOL, false, 1, 0),
    VIP("VIP", ChatColor.GREEN, "&8[&aVIP&8] ", Material.GREEN_WOOL, false, 3,1),
    MVP("MVP", ChatColor.DARK_AQUA, "&8[&3MVP&8] ", Material.CYAN_WOOL, false, 7, 2),

    DIVINE("DIVINE", ChatColor.AQUA, "&8[&bDIVINE&8] ", Material.LIGHT_BLUE_WOOL, false, 12,3),
    EXTREME("EXTREME", ChatColor.DARK_PURPLE, "&8[&5EPIC&8] ", Material.PURPLE_CONCRETE, false, 15,3),
    IMMORTAL("IMMORTAL", ChatColor.GOLD, "&8[&6&lEXTREME&8] ", Material.GOLD_BLOCK, false, 20, 3),

    MOD("MOD", ChatColor.DARK_GREEN, "&8[&2MOD&8] ", Material.GREEN_WOOL, true, 20,4),
    BUILDER("BUILDER", ChatColor.YELLOW, "&8[&eBUILDER&8] ", Material.YELLOW_WOOL, true, 20,4),
    ADMIN("ADMIN", ChatColor.RED, "&8[&cADMIN&8] ", Material.RED_WOOL, true, 20,5),
    COOWNER("COOWNER",ChatColor.LIGHT_PURPLE, "&8[&d&lCO-OWNER&8] ", Material.PINK_WOOL, true, 20, 9),
    OWNER("OWNER", ChatColor.DARK_RED, "&8[&4&lOWNER&8] ", Material.REDSTONE_BLOCK, true, 20, 10);

    private int vaults;
    private String name;
    private ChatColor color;
    private String prefix;
    private Material material;
    private boolean isStaff;
    public int permissionLevel;

    Rank(String name, ChatColor color, String prefix, Material material, boolean isStaff, int vaults, int permissionLevel) {
        this.permissionLevel = permissionLevel;
        this.name = name;
        this.color = color;
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.material = material;
        this.isStaff = isStaff;
        this.vaults = vaults;
    }

    public int getVaults() { return vaults; }
    public boolean isStaff() { return isStaff; }
    public String getName() { return name; }
    //public List<Permission> getPermissions() {return permissions; }
    public ChatColor getColor() { return color; }
    public String getPrefix() { return prefix; }
    public Material getMaterial() { return material; }
    public int getPermissionLevel() { return permissionLevel; }

}