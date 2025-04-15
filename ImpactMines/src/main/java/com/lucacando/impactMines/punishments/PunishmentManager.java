package com.lucacando.impactMines.punishments;

import com.lucacando.impactMines.Main;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PunishmentManager {

    private JavaPlugin plugin;
    private File punishmentsFile;
    private FileConfiguration punishmentsConfig;
    private Main main;

    public PunishmentManager(JavaPlugin plugin, Main main) {
        this.plugin = plugin;
        this.main = main;
        punishmentsFile = new File(plugin.getDataFolder(), "punishments.yml");
        if (!punishmentsFile.exists()) {
            punishmentsFile.getParentFile().mkdirs();
            try {
                punishmentsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        punishmentsConfig = YamlConfiguration.loadConfiguration(punishmentsFile);
    }

    public void banPlayer(OfflinePlayer player, String duration, boolean permanent, String reason, CommandSender staff) {
        reason = ChatColor.translateAlternateColorCodes('&', reason);
        BanList banList = Bukkit.getBanList(BanList.Type.NAME);
        Date expirationDate = null;

        if (!permanent && duration != null && !duration.isEmpty()) {
            Pattern pattern = Pattern.compile("(\\d+)([yMdhms])");
            Matcher matcher = pattern.matcher(duration);
            Calendar cal = Calendar.getInstance();

            while (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                String unit = matcher.group(2);
                switch (unit) {
                    case "y": cal.add(Calendar.YEAR, value); break;
                    case "M": cal.add(Calendar.MONTH, value); break;
                    case "d": cal.add(Calendar.DAY_OF_MONTH, value); break;
                    case "h": cal.add(Calendar.HOUR_OF_DAY, value); break;
                    case "m": cal.add(Calendar.MINUTE, value); break;
                    case "s": cal.add(Calendar.SECOND, value); break;
                }
            }

            expirationDate = cal.getTime();
        }

        String staffName = (staff != null) ? staff.getName() : "Console";
        banList.addBan(player.getName(), reason, expirationDate, staffName);

        if (player.isOnline()) {
            Player online = player.getPlayer();
            String kickMessage = "§cYou are banned!\n§fReason: " + reason;
            if (!permanent && expirationDate != null) {
                kickMessage += "\n§7Time remaining: " + getTimeRemainingString(expirationDate);
            }
            online.kickPlayer(kickMessage);
        }

        staff.sendMessage(main.prefix + "You banned " +
                main.getPlayerRank(player).getColor() + player.getName() +
                ChatColor.BLUE + (!permanent ? " for " + ChatColor.RED + getTimeRemainingString(expirationDate) : ChatColor.RED + " permanently§9."));

        String staffUUID = (staff instanceof Player) ? ((Player) staff).getUniqueId().toString() : "Console";
        String staffUsername = (staff instanceof Player) ? ((Player) staff).getDisplayName() : "Console";

        Date current = new Date();
        punishmentsConfig.set(player.getUniqueId() + ".username", player.getName());
        String path = player.getUniqueId() + ".bans." + current + ".";
        punishmentsConfig.set(path + "end-date", expirationDate);
        punishmentsConfig.set(path + "permanent", permanent);
        punishmentsConfig.set(path + "reason", reason);
        punishmentsConfig.set(path + "staff.uuid", staffUUID);
        punishmentsConfig.set(path + "staff.username", staffUsername);

        try {
            punishmentsConfig.save(punishmentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void IPBan(OfflinePlayer player, String reason, CommandSender staff) {
        BanList banList = Bukkit.getBanList(BanList.Type.IP);
        String staffName = (staff != null) ? staff.getName() : "Console";
        banList.addBan(player.getName(), reason, (Date) null, staffName);
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, staffName);

        if (player.isOnline()) {
            Player online = player.getPlayer();
            online.kickPlayer("§cYou are permanently IP banned!\n§fReason: " + reason);
        }

        staff.sendMessage(main.prefix + "You IP banned " +
                main.getPlayerRank(player).getColor() + player.getName() + ChatColor.RED + " permanently§9.");

        String staffUUID = (staff instanceof Player) ? ((Player) staff).getUniqueId().toString() : "Console";
        String staffUsername = (staff instanceof Player) ? ((Player) staff).getDisplayName() : "Console";

        Date current = new Date();
        punishmentsConfig.set(player.getUniqueId() + ".username", player.getName());
        String path = player.getUniqueId() + ".bans." + current + ".";
        punishmentsConfig.set(path + "end-date", null);
        punishmentsConfig.set(path + "permanent", true);
        punishmentsConfig.set(path + "reason", reason);
        punishmentsConfig.set(path + "ip", true);
        punishmentsConfig.set(path + "staff.uuid", staffUUID);
        punishmentsConfig.set(path + "staff.username", staffUsername);

        try {
            punishmentsConfig.save(punishmentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mutePlayer(OfflinePlayer player, String duration, boolean permanent, String reason, CommandSender staff) {
        reason = ChatColor.translateAlternateColorCodes('&', reason);
        Date expirationDate = null;

        if (!permanent && duration != null && !duration.isEmpty()) {
            Pattern pattern = Pattern.compile("(\\d+)([yMdhms])");
            Matcher matcher = pattern.matcher(duration);
            Calendar cal = Calendar.getInstance();

            while (matcher.find()) {
                int value = Integer.parseInt(matcher.group(1));
                String unit = matcher.group(2);
                switch (unit) {
                    case "y": cal.add(Calendar.YEAR, value); break;
                    case "M": cal.add(Calendar.MONTH, value); break;
                    case "d": cal.add(Calendar.DAY_OF_MONTH, value); break;
                    case "h": cal.add(Calendar.HOUR_OF_DAY, value); break;
                    case "m": cal.add(Calendar.MINUTE, value); break;
                    case "s": cal.add(Calendar.SECOND, value); break;
                }
            }

            expirationDate = cal.getTime();
        }

        String permText = !permanent ? " for " + ChatColor.RED + getTimeRemainingString(expirationDate) : ChatColor.RED + " permanently§9.";

        if (player.isOnline()) {
            player.getPlayer().sendMessage(main.prefix + "You were muted " + ChatColor.BLUE + permText);
        }

        staff.sendMessage(main.prefix + "You muted " +
                main.getPlayerRank(player).getColor() + player.getName() + ChatColor.BLUE + permText);

        String staffUUID = (staff instanceof Player) ? ((Player) staff).getUniqueId().toString() : "Console";
        String staffUsername = (staff instanceof Player) ? ((Player) staff).getDisplayName() : "Console";

        Date current = new Date();
        String path = player.getUniqueId() + ".mutes." + current + ".";
        punishmentsConfig.set(player.getUniqueId() + ".username", player.getName());
        punishmentsConfig.set(path + "end-date", expirationDate);
        punishmentsConfig.set(path + "permanent", permanent);
        punishmentsConfig.set(path + "reason", reason);
        punishmentsConfig.set(path + "staff.uuid", staffUUID);
        punishmentsConfig.set(path + "staff.username", staffUsername);

        try {
            punishmentsConfig.save(punishmentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void warnPlayer(OfflinePlayer player, String reason, CommandSender staff) {
        reason = ChatColor.translateAlternateColorCodes('&', reason);

        if (player.isOnline()) {
            player.getPlayer().sendMessage(main.prefix + "§eYou have received a warning: §f" + reason);
        }

        String staffUUID = (staff instanceof Player) ? ((Player) staff).getUniqueId().toString() : "Console";
        String staffUsername = (staff instanceof Player) ? ((Player) staff).getDisplayName() : "Console";

        staff.sendMessage(main.prefix + "You warned " +
                main.getPlayerRank(player).getColor() + player.getName() +
                ChatColor.YELLOW + " for: §f" + reason);

        UUID uuid = player.getUniqueId();
        String basePath = uuid + ".warnings";
        int index = 0;
        if (punishmentsConfig.contains(basePath)) {
            index = punishmentsConfig.getConfigurationSection(basePath).getKeys(false).size();
        }

        String path = basePath + "." + index + ".";
        Date now = new Date();

        punishmentsConfig.set(uuid + ".username", player.getName());
        punishmentsConfig.set(path + "date", now);
        punishmentsConfig.set(path + "reason", reason);
        punishmentsConfig.set(path + "staff.uuid", staffUUID);
        punishmentsConfig.set(path + "staff.username", staffUsername);

        try {
            punishmentsConfig.save(punishmentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unmutePlayer(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        String mutesPath = uuid + ".mutes";

        if (!punishmentsConfig.contains(mutesPath)) return;

        ConfigurationSection mutesSection = punishmentsConfig.getConfigurationSection(mutesPath);
        if (mutesSection == null) return;

        Date now = new Date();

        for (String key : new HashSet<>(mutesSection.getKeys(false))) {
            String basePath = mutesPath + "." + key;
            boolean permanent = punishmentsConfig.getBoolean(basePath + ".permanent");
            Date endDate = punishmentsConfig.getObject(basePath + ".end-date", Date.class);

            if (permanent || (endDate != null && now.before(endDate))) {
                punishmentsConfig.set(basePath + ".end-date", new Date(System.currentTimeMillis() - 1000));
                punishmentsConfig.set(basePath + ".permanent", false); // <-- important for manual unmute
                punishmentsConfig.set(basePath + ".unmuted", true);
            }
        }

        if (player.isOnline()) {
            player.getPlayer().sendMessage(main.prefix + "§aYou have been unmuted.");
        }

        try {
            punishmentsConfig.save(punishmentsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean isPlayerMuted(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        if (!punishmentsConfig.contains(uuid + ".mutes")) return false;

        ConfigurationSection section = punishmentsConfig.getConfigurationSection(uuid + ".mutes");
        if (section == null) return false;

        for (String key : section.getKeys(false)) {
            String base = uuid + ".mutes." + key;
            boolean permanent = punishmentsConfig.getBoolean(base + ".permanent");
            if (permanent) return true;

            Date end = punishmentsConfig.getObject(base + ".end-date", Date.class);
            if (end != null && new Date().before(end)) return true;
        }
        return false;
    }

    public AbstractMap.SimpleEntry<Date, String> getMuteExpirationInfo(OfflinePlayer player) {
        String uuid = player.getUniqueId().toString();
        if (!punishmentsConfig.contains(uuid + ".mutes")) return null;

        ConfigurationSection mutes = punishmentsConfig.getConfigurationSection(uuid + ".mutes");
        if (mutes == null) return null;

        Date soonest = null;
        for (String key : mutes.getKeys(false)) {
            String base = uuid + ".mutes." + key;
            boolean permanent = punishmentsConfig.getBoolean(base + ".permanent");
            if (permanent) return new AbstractMap.SimpleEntry<>(null, "Permanent");

            Date end = punishmentsConfig.getObject(base + ".end-date", Date.class);
            if (end != null && new Date().before(end)) {
                if (soonest == null || end.before(soonest)) soonest = end;
            }
        }

        if (soonest == null) return null;
        long millis = soonest.getTime() - System.currentTimeMillis();
        return new AbstractMap.SimpleEntry<>(soonest, getTimeRemainingString(soonest));
    }

    public boolean isValidDurationFormat(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.matches("^(\\d+[yMwdhms])+$");
    }

    private String getTimeRemainingString(Date expirationDate) {
        long millisLeft = expirationDate.getTime() - System.currentTimeMillis();
        if (millisLeft <= 0) return "Expired";

        long seconds = millisLeft / 1000;
        long days = seconds / 86400; seconds %= 86400;
        long hours = seconds / 3600; seconds %= 3600;
        long minutes = seconds / 60; seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0 || sb.length() == 0) sb.append(seconds).append("s");

        return sb.toString().trim();
    }
}
