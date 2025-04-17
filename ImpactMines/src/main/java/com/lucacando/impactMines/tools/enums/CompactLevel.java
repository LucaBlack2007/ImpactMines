package com.lucacando.impactMines.tools.enums;

import org.bukkit.ChatColor;

public enum CompactLevel {

    NONE("",0),
    COMPACTED("Compacted ", 1),
    SUPER("Super Compacted ", 2),
    ULTRA("Ultra Compacted ", 3),
    OMEGA("Omega Compacted ", 4);

    private String compactedName;
    private int compactedLevel;

    CompactLevel(String compactedName, int compactedLevel) {
        this.compactedName = compactedName;
        this.compactedLevel = compactedLevel;
    }

    public String getCompactedName(ImpactItem impactItem) {
        String magicSize = String.valueOf(Math.floor(Math.pow(10, compactedLevel) / 100));
        if (compactedLevel == 0) return impactItem.getName();
        return impactItem.getNameColor().toString() + ChatColor.MAGIC + ChatColor.BOLD + magicSize + " " +
                ChatColor.RESET + impactItem.getNameColor().toString() + ChatColor.BOLD + compactedName +
                impactItem.getNameColor() + ChatColor.BOLD + ChatColor.stripColor(impactItem.getName()) + " "
                + impactItem.getNameColor().toString() + ChatColor.MAGIC + ChatColor.BOLD + magicSize;
    }

    public int getCompactedLevel() {
        return compactedLevel;
    }
}
