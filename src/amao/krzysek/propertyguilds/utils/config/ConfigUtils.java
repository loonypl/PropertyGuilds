package amao.krzysek.propertyguilds.utils.config;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ConfigUtils {

    protected FileConfiguration cfg;

    public ConfigUtils(final ConfigMessageType type) {
        if (type == ConfigMessageType.CONFIG) {
            cfg = PropertyGuilds.getInstance().getConfig();
        } else if (type == ConfigMessageType.LANG) {
            cfg = PropertyGuilds.getInstance().getLang();
        }
    }

    public String getString(final String path) {
        return cfg.getString(path);
    }

    public int getInt(final String path) {
        return cfg.getInt(path);
    }

    public ArrayList<String> getArrayList(final String path) {
        return (ArrayList<String>) cfg.getStringList(path);
    }

    public boolean getBoolean(final String path) {
        return cfg.getBoolean(path);
    }

}
