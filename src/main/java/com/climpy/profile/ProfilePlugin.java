package com.climpy.profile;

import com.climpy.profile.commands.UserCommand;
import com.climpy.profile.commands.coin.CoinsCommand;
import com.climpy.profile.commands.essentials.*;
import com.climpy.profile.commands.rank.ListRankCommand;
import com.climpy.profile.commands.rank.SetRankCommand;
import com.climpy.profile.commands.staff.FreezeCommand;
import com.climpy.profile.commands.staff.StaffModeCommand;
import com.climpy.profile.commands.symbol.SetSymbolCommand;
import com.climpy.profile.commands.symbol.SymbolCommand;
import com.climpy.profile.config.FileConfig;
import com.climpy.profile.inv.InventoryManager;
import com.climpy.profile.listeners.*;
import com.climpy.profile.managers.FrozenManager;
import com.climpy.profile.managers.StaffModeManager;
import com.climpy.profile.mongo.MongoConnection;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.user.UserManager;
import com.climpy.profile.utils.TaskUtil;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

@Getter
public class ProfilePlugin extends JavaPlugin  {
    public static ProfilePlugin instance;

    public UserManager userManager;
    private MongoConnection mongoConnection;
    private static InventoryManager invManager;
    private boolean loaded;
    public FileConfig mainConfig;

    private StaffModeManager staffModeManager;
    private FrozenManager frozenManager;

    public static Type LIST_STRING_TYPE = new TypeToken<List<String>>() {
    }.getType();

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getMessenger().registerOutgoingPluginChannel(this,"BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ChannelListener());

        setupConfig();

        mongoConnection = new MongoConnection();
        mongoConnection.setupDatabaseConnection();

        userManager = new UserManager();

        registerManagers();
        registerListener();
        registerCommand();

        invManager = new InventoryManager(this);
        invManager.init();

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            User user = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
            if (user != null) {
                this.userManager.getUsers().put(user.getUniqueUUID(), user);
            } else {
                onlinePlayer.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cÜzgünüm sunucuya giremezsin!\n Oyuncu verilerin oluşturmamış."));
            }
        });

        TaskUtil.runLater(() -> this.loaded = true, 60L);
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[cProfile] Sunucu: &aAcik"));
    }

    private void registerManagers() {
        this.staffModeManager = new StaffModeManager(this);
        this.frozenManager = new FrozenManager(this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == null)
                continue;
            User onlineUser = this.userManager.getUser(onlinePlayer.getUniqueId());
            if(onlineUser == null)
                continue;
            if (onlineUser.getRankType().isAboveOrEqual(RankType.MOD) && onlineUser.isStaffMode()) {
                onlinePlayer.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.RED + "- moderatör modu devredışı.");
                this.staffModeManager.setStaffMode(onlineUser.getUniqueUUID(), false);
            }
        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[cProfile] Sunucu: &cKapali"));
        instance = null;
    }

    public void registerListener() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new ProfileListener(), this);
        pm.registerEvents(new StaffModeListener(), this);
        pm.registerEvents(new FrozenListener(), this);
        pm.registerEvents(new ServerListener(), this);
        pm.registerEvents(new SecurityListener(), this);
    }

    public void registerCommand() {
        getCommand("user").setExecutor(new UserCommand());
        getCommand("setrank").setExecutor(new SetRankCommand());
        getCommand("listrank").setExecutor(new ListRankCommand());
        getCommand("setsymbol").setExecutor(new SetSymbolCommand());
        getCommand("symbol").setExecutor(new SymbolCommand());
        getCommand("coins").setExecutor(new CoinsCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("staff").setExecutor(new StaffModeCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("day").setExecutor(new DayCommand());
        getCommand("night").setExecutor(new NightCommand());
        getCommand("world").setExecutor(new WorldTeleportCommand());
        getCommand("createworld").setExecutor(new WorldCreateCommand());
        getCommand("deleteworld").setExecutor(new WorldDeleteCommand());
    }

    public static String separatorCheck(StringBuilder stringBuilder) {
        String STRING_SEPARATOR = "\n";
        return separatorCheck(stringBuilder, "\n");
    }

    public static String separatorCheck(StringBuilder stringBuilder, String STRING_SEPARATOR) {
        String toReturn = stringBuilder.toString();
        if (toReturn.endsWith(STRING_SEPARATOR)) {
            toReturn = toReturn.substring(0, toReturn.length() - STRING_SEPARATOR.length());
        }

        return toReturn;
    }

    private void fetchConfig() {
        File file = new File(getDataFolder().getPath(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    }

    private void setupConfig() {
        try {
            File file;
            if (!this.getDataFolder().exists()) {
                this.getDataFolder().mkdirs();
            }
            if (!(file = new File(this.getDataFolder().getAbsolutePath(), "config.yml")).exists()) {
                this.mainConfig = new FileConfig(this, "config.yml");
                this.saveConfig();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        fetchConfig();
    }

    public static InventoryManager manager() { return invManager; }
    public static ProfilePlugin getInstance() { return instance; }
}
