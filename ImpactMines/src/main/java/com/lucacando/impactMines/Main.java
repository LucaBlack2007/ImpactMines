package com.lucacando.impactMines;

import com.lucacando.impactMines.admin.ReloadPlayersCommand;
import com.lucacando.impactMines.listeners.ChatListener;
import com.lucacando.impactMines.listeners.FirstJoinListener;
import com.lucacando.impactMines.ranks.Rank;
import com.lucacando.impactMines.ranks.RankCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RankCommand(this, "/rank [player] [<rank>]"), this);
    }


    public int getPermission(Player player) { return getPlayerRank(player).getPermissionLevel(); }
    public String formatPlayerName(Player player) {
        Rank rank = getPlayerRank(player);
        return rank.getColor() + rank.getPrefix() + rank.getColor() + player.getName();
    }
    public Rank getPlayerRank(Player player) {
        return Rank.valueOf(getConfig().getString("players." + player.getUniqueId().toString() + ".rank", ""));
    }
    public void setPlayerRank(Player player, Rank rank) {
        getConfig().set("players." + player.getUniqueId() + ".rank", rank.getName());
        saveConfig();
    }
    private void instantiateAttribute(Player player, String label, String attribute) {
        if (!getConfig().contains("players." + player.getUniqueId() + "." + label)) {
            getConfig().set("players." + player.getUniqueId() + "." + label, attribute);
            this.saveConfig();
        }
    }
    public void instantiatePlayer(Player player) {
        instantiateAttribute(player, "rank", Rank.DEFAULT.getName());
    }

}
