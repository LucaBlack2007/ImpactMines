package com.lucacando.impactMines;

import com.lucacando.impactMines.admin.ReloadPlayersCommand;
import com.lucacando.impactMines.admin.gamemode.GamemodeAdventure;
import com.lucacando.impactMines.admin.gamemode.GamemodeCreative;
import com.lucacando.impactMines.admin.gamemode.GamemodeSpectator;
import com.lucacando.impactMines.admin.gamemode.GamemodeSurvival;
import com.lucacando.impactMines.admin.tools.Tool;
import com.lucacando.impactMines.admin.tools.ToolCommand;
import com.lucacando.impactMines.listeners.BlockBreakListener;
import com.lucacando.impactMines.listeners.ChatListener;
import com.lucacando.impactMines.listeners.FirstJoinListener;
import com.lucacando.impactMines.ranks.Rank;
import com.lucacando.impactMines.ranks.RankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public Map<UUID, UUID> selectedTemp = new HashMap<>();
    public String prefix = ChatColor.translateAlternateColorCodes('&',
            "&8&l[&bImpactMines&8&l] &9");
    public String noPermission = prefix + ChatColor.RED + "You do not have permission.";

    @Override
    public void onEnable() {
        getLogger().info("ImpactMines core plugin has been enabled!");

        if (!getDataFolder().exists()) getDataFolder().mkdir();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("rank").setExecutor(new RankCommand(this, "/rank [player] [<rank>]"));
        getCommand("reloadplayers").setExecutor(new ReloadPlayersCommand(this));

        getCommand("gmc").setExecutor(new GamemodeCreative(this, "/gmc [<player>]"));
        getCommand("gms").setExecutor(new GamemodeSurvival(this, "/gms [<player>]"));
        getCommand("gmsp").setExecutor(new GamemodeSpectator(this, "/gmsp [<player>]"));
        getCommand("gma").setExecutor(new GamemodeAdventure(this, "/gma [<player>]"));

        getCommand("toolselect").setExecutor(new ToolCommand(this, "/tool [<player>]"));

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RankCommand(this), this);
        getServer().getPluginManager().registerEvents(new ToolCommand(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
    }


    public String formatPlayerName(Player player) {
        Rank rank = getPlayerRank(player);
        return rank.getColor() + rank.getPrefix() + rank.getColor() + player.getName();
    }

    // RANK STUFF /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isStaff(Player player) { return getPlayerRank(player).isStaff(); }
    public int getPermission(Player player) { return getPlayerRank(player).getPermissionLevel(); }
    public Rank getPlayerRank(Player player) {
        return Rank.valueOf(getConfig().getString("players." + player.getUniqueId().toString() + ".rank", ""));
    }
    public void setPlayerRank(Player player, Rank rank) {
        getConfig().set("players." + player.getUniqueId() + ".rank", rank.getName());
        saveConfig();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void instantiateAttribute(Player player, String label, String attribute) {
        if (!getConfig().contains("players." + player.getUniqueId() + "." + label)) {
            getConfig().set("players." + player.getUniqueId() + "." + label, attribute);
            this.saveConfig();
        }
    }
    public void instantiatePlayer(Player player) {
        instantiateAttribute(player, "rank", Rank.DEFAULT.getName());
    }
    public ArrayList<String> stringsToLore(String... strings) {
        ArrayList<String> ret = new ArrayList<>();
        int i = 0;
        for (String string : strings) {
            i++;
            if (i == strings.length)
                ret.add(string);
            else ret.add(string + "\n");
        }
        return ret;
    }

    public boolean isTool(ItemStack item) {
        for (Tool tool : tools) {
            if (item.isSimilar(tool.getItem())) {
                return true;
            }
        }
        return false;
    }

    public Tool[] tools = new Tool[] {
            new Tool(this, Material.WOODEN_AXE, ChatColor.GRAY + "Oak Axe", 1, 2, 0, ChatColor.GRAY + "A basic axe made from oak wood."),
            new Tool(this, Material.WOODEN_AXE, ChatColor.GREEN + "Birch Axe", 2, 4, 1, ChatColor.GRAY + "Slightly sharper and faster."),
            new Tool(this, Material.WOODEN_AXE, ChatColor.DARK_GREEN + "Spruce Axe", 3, 6, 1, ChatColor.GRAY + "Chops with better control."),
            new Tool(this, Material.STONE_AXE, ChatColor.GRAY + "Stone Axe", 2, 5, 1, ChatColor.GRAY + "Stronger than wood, but still basic."),
            new Tool(this, Material.GOLDEN_AXE, ChatColor.YELLOW + "Golden Axe", 3, 8, 1, ChatColor.GRAY + "Fast but fragile axe."),
            new Tool(this, Material.DIAMOND_AXE, ChatColor.BLUE + "Diamond Axe", 5, 10, 2, ChatColor.GRAY + "Precise and durable."),
            new Tool(this, Material.NETHERITE_AXE, ChatColor.DARK_GRAY + "Netherite Axe", 6, 12, 3, ChatColor.GRAY + "Heavy and powerful."),

            new Tool(this, Material.STONE_PICKAXE, ChatColor.GOLD + "Copper Pickaxe", 5, 11, 2, ChatColor.GRAY + "Mines softer materials with ease."),
            new Tool(this, Material.STONE_PICKAXE, ChatColor.WHITE + "Tin Pickaxe", 5, 10, 2, ChatColor.GRAY + "Basic upgrade for early stone."),
            new Tool(this, Material.IRON_PICKAXE, ChatColor.DARK_GRAY + "Lead Pickaxe", 6, 13, 3, ChatColor.GRAY + "Heavy yet effective."),
            new Tool(this, Material.IRON_PICKAXE, ChatColor.DARK_GRAY + "Coal Pickaxe", 6, 13, 2, ChatColor.GRAY + "Solid entry-tier ore tool."),
            new Tool(this, Material.IRON_PICKAXE, ChatColor.GRAY + "" + ChatColor.BOLD + "Silver Pickaxe", 8, 18, 3, ChatColor.GRAY + "Clean and refined miner's tool."),
            new Tool(this, Material.IRON_PICKAXE, ChatColor.WHITE + "Iron Pickaxe", 7, 15, 2, ChatColor.GRAY + "Tough and balanced."),
            new Tool(this, Material.GOLDEN_PICKAXE, ChatColor.YELLOW + "Gold Pickaxe", 8, 17, 3, ChatColor.GRAY + "Fast but brittle."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.WHITE + "" + ChatColor.BOLD + "Platinum Pickaxe", 11, 21, 4, ChatColor.GRAY + "A luxurious, efficient tool."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.BLUE + "" + ChatColor.BOLD + "Lapis Pickaxe", 10, 20, 3, ChatColor.GRAY + "Saturated with mystical energy."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.RED + "Redstone Pickaxe", 12, 22, 4, ChatColor.GRAY + "Crackling with circuitry."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.GREEN + "" + ChatColor.BOLD + "Emerald Pickaxe", 15, 25, 4, ChatColor.GRAY + "Trades power for precision."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Amethyst Pickaxe", 18, 29, 5, ChatColor.GRAY + "Shimmers with fractured brilliance."),
            new Tool(this, Material.NETHERITE_PICKAXE, ChatColor.WHITE + "" + ChatColor.BOLD + "Quartz Pickaxe", 21, 32, 5, ChatColor.GRAY + "Honed for Nether precision."),
            new Tool(this, Material.NETHERITE_PICKAXE, ChatColor.DARK_PURPLE + "Obsidian Pickaxe", 24, 36, 6, ChatColor.GRAY + "Crushes what others cannot."),
            new Tool(this, Material.NETHERITE_PICKAXE, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Ancient Pickaxe", 28, 42, 6, ChatColor.GRAY + "Forged in time-lost flame."),
            new Tool(this, Material.DIAMOND_PICKAXE, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Crystalline Pickaxe", 32, 47, 6, ChatColor.GRAY + "Gleams with flawless purity."),
            new Tool(this, Material.NETHERITE_PICKAXE, ChatColor.AQUA + "" + ChatColor.BOLD + "Lunar Pickaxe", 38, 55, 7, ChatColor.GRAY + "Draws strength from moonlight."),
            new Tool(this, Material.NETHERITE_PICKAXE, ChatColor.DARK_RED + "" + ChatColor.BOLD + "Ωmega Pickaxe", 50, 70, 8, ChatColor.GRAY + "The final pick. Beyond comprehension.")
    };

}
