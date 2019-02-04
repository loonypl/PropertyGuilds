package amao.krzysek.propertyguilds;

import amao.krzysek.propertyguilds.commands.Commands;
import amao.krzysek.propertyguilds.enums.ChatType;
import amao.krzysek.propertyguilds.listeners.*;
import amao.krzysek.propertyguilds.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PropertyGuilds extends JavaPlugin {

    protected final File configFile = new File(getDataFolder(), "config.yml");
    protected final File langFile = new File(getDataFolder(), "lang.yml");
    protected final FileConfiguration config = new YamlConfiguration();
    protected final FileConfiguration lang = new YamlConfiguration();
    protected static PropertyGuilds instance;
    protected MySQL mysql;
    protected LinkedHashMap<String, ChatType> chatToggle = new LinkedHashMap<>();
    protected LinkedHashMap<String, LinkedList<String>> invites = new LinkedHashMap<>();
    protected LinkedHashMap<String, LinkedList<String>> alliances = new LinkedHashMap<>();
    protected LinkedHashMap<String, Location> baseCooldown = new LinkedHashMap<>();

    public static PropertyGuilds getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Configurations
        hookFiles(true);
        // MySQL
        hookMySQL(true);
        // commands
        hookCommands();
        // listeners
        hookListeners();
    }

    @Override
    public void onDisable() {
        // Configurations
        hookFiles(false);
        // MySQL
        hookMySQL(false);
    }

    // Listeners

    protected void hookListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerServerJoin(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
    }

    // Commands

    protected void hookCommands() {
        getCommand("guilds").setExecutor(new Commands());
    }

    // LinkedHashMaps

    public LinkedHashMap<String, ChatType> getChatToggle() {
        return this.chatToggle;
    }

    public LinkedHashMap<String, LinkedList<String>> getInvites() {
        return this.invites;
    }

    public LinkedHashMap<String, LinkedList<String>> getAlliances() {
        return this.alliances;
    }

    public LinkedHashMap<String, Location> getBaseCooldown() {
        return this.baseCooldown;
    }

    // Configurations

    protected void copy(InputStream in, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hookFiles(boolean bool) {
        if (bool) {
            checkFiles();
            loadFiles();
            saveFiles();
        } else {
            saveFiles();
        }
    }

    protected void checkFiles() {
        try {
            if (!(configFile.exists())) {
                configFile.getParentFile().mkdirs();
                copy(getResource("config.yml"), configFile);
            }
            if (!(langFile.exists())) {
                langFile.getParentFile().mkdirs();
                copy(getResource("lang.yml"), langFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFiles() {
        try {
            config.load(configFile);
            lang.load(langFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFiles() {
        try {
            config.save(configFile);
            lang.save(langFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getLang() {
        return lang;
    }

    // MySQL

    protected void hookMySQL(boolean bool) {
        if (bool) {
            mysql = new MySQL(getConfig().getString("mysql.host"), getConfig().getString("mysql.port"), getConfig().getString("mysql.user"), getConfig().getString("mysql.password"), getConfig().getString("mysql.database"));
            try {
                mysql.openConnection();
            } finally {
                mysql.createTables();
            }
        } else {
            mysql.closeConnection();
        }
    }

    public MySQL getMySQL() {
        return mysql;
    }

}
