package com.lucacando.impactMines;

import com.lucacando.impactMines.admin.ReloadPlayersCommand;
import com.lucacando.impactMines.admin.gamemode.GamemodeAdventure;
import com.lucacando.impactMines.admin.gamemode.GamemodeCreative;
import com.lucacando.impactMines.admin.gamemode.GamemodeSpectator;
import com.lucacando.impactMines.admin.gamemode.GamemodeSurvival;
import com.lucacando.impactMines.shop.CustomRecipes;
import com.lucacando.impactMines.tools.GiveCompacted;
import com.lucacando.impactMines.tools.Tool;
import com.lucacando.impactMines.tools.ToolCommand;
import com.lucacando.impactMines.discord.DiscordChatListener;
import com.lucacando.impactMines.discord.DiscordListener;
import com.lucacando.impactMines.tools.BlockBreakListener;
import com.lucacando.impactMines.listeners.ChatListener;
import com.lucacando.impactMines.listeners.FirstJoinListener;
import com.lucacando.impactMines.listeners.JoinLeaveListener;
import com.lucacando.impactMines.mines.Mine;
import com.lucacando.impactMines.mines.MineRegenerationTask;
import com.lucacando.impactMines.mines.MineWandListener;
import com.lucacando.impactMines.mines.Selection;
import com.lucacando.impactMines.mines.commands.FillMineCommand;
import com.lucacando.impactMines.mines.commands.MineWandCommand;
import com.lucacando.impactMines.mines.commands.RemoveMineCommand;
import com.lucacando.impactMines.playervaults.InventoryListener;
import com.lucacando.impactMines.playervaults.PlayerVaultCommand;
import com.lucacando.impactMines.playervaults.VaultManager;
import com.lucacando.impactMines.punishments.MutedListener;
import com.lucacando.impactMines.punishments.PunishmentManager;
import com.lucacando.impactMines.punishments.commands.bans.BanCommand;
import com.lucacando.impactMines.punishments.commands.bans.IPBanCommand;
import com.lucacando.impactMines.punishments.commands.bans.TempBanCommand;
import com.lucacando.impactMines.punishments.commands.bans.UnbanCommand;
import com.lucacando.impactMines.punishments.commands.mutes.MuteCommand;
import com.lucacando.impactMines.punishments.commands.mutes.TempMuteCommand;
import com.lucacando.impactMines.punishments.commands.mutes.UnmuteCommand;
import com.lucacando.impactMines.punishments.commands.warns.WarnCommand;
import com.lucacando.impactMines.punishments.commands.warns.WarningsCommand;
import com.lucacando.impactMines.ranks.Rank;
import com.lucacando.impactMines.ranks.RankCommand;
import com.lucacando.impactMines.shop.ChestShopCreation;
import com.lucacando.impactMines.spawn.SetSpawnCommand;
import com.lucacando.impactMines.spawn.SpawnCommand;
import com.lucacando.impactMines.spawn.SpawnListener;
import com.lucacando.impactMines.tools.enums.ImpactItem;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public final class Main extends JavaPlugin {

    public Map<String, Command> knownCommands;
    public Map<UUID, UUID> selectedTemp = new HashMap<>();
    public String prefix = ChatColor.translateAlternateColorCodes('&',
            "&8&l[&bImpactMines&8&l] &9");
    public String noPermission = prefix + ChatColor.RED + "You do not have permission.";
    public Map<UUID, Selection> selections = new HashMap<>();
    public List<Mine> activeMines = new ArrayList<>();

    private File minesFile;
    public FileConfiguration minesConfig;
    public JDA jda;

    private VaultManager vaultManager;
    private PunishmentManager punishmentManager;
    private CustomRecipes customRecipes;

    @Override
    public void onEnable() {
        getLogger().info("ImpactMines core plugin has been enabled!");

        registerNoPermissionMessages();

        customRecipes = new CustomRecipes(this);

        if (!getDataFolder().exists()) getDataFolder().mkdir();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);

        punishmentManager = new PunishmentManager(this, this);
        getServer().getPluginManager().registerEvents(new MutedListener(this), this);
        getCommand("tempban").setExecutor(new TempBanCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("ipban").setExecutor(new IPBanCommand(this));
        getCommand("unban").setExecutor(new UnbanCommand(this));

        getCommand("tempmute").setExecutor(new TempMuteCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));

        getCommand("warn").setExecutor(new WarnCommand(this));
        getCommand("warnings").setExecutor(new WarningsCommand(this));

        vaultManager = new VaultManager(this);
        getCommand("playervault").setExecutor(new PlayerVaultCommand(this, vaultManager));
        getServer().getPluginManager().registerEvents(new InventoryListener(vaultManager), this);

        getCommand("removemine").setExecutor(new RemoveMineCommand(this));
        getCommand("minewand").setExecutor(new MineWandCommand(this));
        getCommand("fillmine").setExecutor(new FillMineCommand(this));
        getServer().getPluginManager().registerEvents(new MineWandListener(this), this);
        loadMines();
        new MineRegenerationTask(this).runTaskTimer(this, 15 * 20L, 15 * 20L);

        getCommand("rank").setExecutor(new RankCommand(this, "/rank [player] [<rank>]"));
        getCommand("reloadplayers").setExecutor(new ReloadPlayersCommand(this));

        getCommand("gmc").setExecutor(new GamemodeCreative(this, "/gmc [<player>]"));
        getCommand("gms").setExecutor(new GamemodeSurvival(this, "/gms [<player>]"));
        getCommand("gmsp").setExecutor(new GamemodeSpectator(this, "/gmsp [<player>]"));
        getCommand("gma").setExecutor(new GamemodeAdventure(this, "/gma [<player>]"));

        getCommand("toolselect").setExecutor(new ToolCommand(this, "/tool [<player>]"));
        getCommand("givecompacted").setExecutor(new GiveCompacted(this));

        getServer().getPluginManager().registerEvents(new DiscordChatListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new RankCommand(this), this);
        getServer().getPluginManager().registerEvents(new ToolCommand(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestShopCreation(this), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(this), this);

        JDABuilder builder = JDABuilder.createDefault("MTM2MTEyNjI5ODEwMTA5MjQ5NA.GRVWtK.MDoe5CWJ_S9g97Y8ikuTdkDuUk9xMGgePSi1YY");
        builder.setActivity(Activity.watching("your server."));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.addEventListeners(new DiscordListener());
        builder.setEnabledIntents(GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EXPRESSIONS,
                GatewayIntent.SCHEDULED_EVENTS
        );



        try {
            jda = builder.build();
            System.out.println("Successfully imported discord API!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        saveMines();
        vaultManager.saveAllVaults();
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public void loadMines() {
        minesFile = new File(getDataFolder(), "mines.yml");
        if (!minesFile.exists()) {
            getDataFolder().mkdirs();
            saveResource("mines.yml", false);
        }
        minesConfig = YamlConfiguration.loadConfiguration(minesFile);
        activeMines.clear();

        // Assuming mines are stored under "mines" as individual entries (mine0, mine1, etc.)
        if (minesConfig.contains("mines")) {
            ConfigurationSection section = minesConfig.getConfigurationSection("mines");
            for (String key : section.getKeys(false)) {
                try {
                    String worldName = minesConfig.getString("mines." + key + ".world");
                    World world = getServer().getWorld(worldName);
                    if (world == null) {
                        getLogger().warning("World '" + worldName + "' not found for mine " + key);
                        continue;
                    }
                    int pos1X = minesConfig.getInt("mines." + key + ".pos1.x");
                    int pos1Y = minesConfig.getInt("mines." + key + ".pos1.y");
                    int pos1Z = minesConfig.getInt("mines." + key + ".pos1.z");
                    int pos2X = minesConfig.getInt("mines." + key + ".pos2.x");
                    int pos2Y = minesConfig.getInt("mines." + key + ".pos2.y");
                    int pos2Z = minesConfig.getInt("mines." + key + ".pos2.z");
                    org.bukkit.Location pos1 = new org.bukkit.Location(world, pos1X, pos1Y, pos1Z);
                    org.bukkit.Location pos2 = new org.bukkit.Location(world, pos2X, pos2Y, pos2Z);
                    String b1Str = minesConfig.getString("mines." + key + ".block1");
                    String b2Str = minesConfig.getString("mines." + key + ".block2");
                    org.bukkit.Material block1 = org.bukkit.Material.matchMaterial(b1Str);
                    org.bukkit.Material block2 = org.bukkit.Material.matchMaterial(b2Str);
                    if (block1 == null || block2 == null) {
                        getLogger().warning("Invalid block types for mine " + key);
                        continue;
                    }
                    Mine mine = new Mine(world, pos1, pos2, block1, block2);
                    // Optionally, you can refill the mine immediately or rely on player mining to change blocks.
                    activeMines.add(mine);
                } catch (Exception e) {
                    getLogger().warning("Error loading mine " + key + ": " + e.getMessage());
                }
            }
        }
        getLogger().info("Loaded " + activeMines.size() + " mines.");
    }

    public void saveMines() {
        // Clear existing data
        minesConfig.set("mines", null);
        for (int i = 0; i < activeMines.size(); i++) {
            Mine mine = activeMines.get(i);
            String key = "mines.mine" + i;
            minesConfig.set(key + ".world", mine.getWorld().getName());
            // Save the two corner positions (using min and max from mine)
            minesConfig.set(key + ".pos1.x", mine.getMin().getBlockX());
            minesConfig.set(key + ".pos1.y", mine.getMin().getBlockY());
            minesConfig.set(key + ".pos1.z", mine.getMin().getBlockZ());
            minesConfig.set(key + ".pos2.x", mine.getMax().getBlockX());
            minesConfig.set(key + ".pos2.y", mine.getMax().getBlockY());
            minesConfig.set(key + ".pos2.z", mine.getMax().getBlockZ());
            minesConfig.set(key + ".block1", mine.getBlock1().name());
            minesConfig.set(key + ".block2", mine.getBlock2().name());
        }
        try {
            minesConfig.save(minesFile);
            getLogger().info("Mines saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerNoPermissionMessages() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
            for (Command cmd : knownCommands.values()) {
                cmd.setPermissionMessage(prefix + "You don't have permission to use this command.");
            }
            for (Command cmd : knownCommands.values()) {
                String usage = cmd.getUsage();
                if (usage != null && !usage.isEmpty()) {
                    cmd.setUsage(prefix + "Incomplete command. Correct usage: " + usage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatPlayerName(Player player) {
        Rank rank = getPlayerRank(player);
        return rank.getColor() + rank.getPrefix() + rank.getColor() + player.getName();
    }

    public CustomRecipes getCustomRecipes() {
        return customRecipes;
    }

    // RANK STUFF /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean isStaff(Player player) { return getPlayerRank(player).isStaff(); }
    public int getPermission(Player player) { return getPlayerRank(player).getPermissionLevel(); }
    public Rank getPlayerRank(Player player) {
        return Rank.valueOf(getConfig().getString("players." + player.getUniqueId().toString() + ".rank", ""));
    }
    public Rank getPlayerRank(OfflinePlayer player) {
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

    public Location getSpawn() {
        Location spawnLoc = new Location(
                Bukkit.getWorld(this.getConfig().getString("spawn.world")),
                this.getConfig().getDouble("spawn.x"),
                this.getConfig().getDouble("spawn.y"),
                this.getConfig().getDouble("spawn.z"),
                (float) this.getConfig().getDouble("spawn.yaw"),
                (float) this.getConfig().getDouble("spawn.pitch")
        );
        return spawnLoc;
    }
}
