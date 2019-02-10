package amao.krzysek.propertyguilds.utils.config;

import amao.krzysek.propertyguilds.PropertyGuilds;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ConfigUtils2 {

    protected LinkedHashMap<String, Object> configHash = PropertyGuilds.getInstance().getConfigHash();
    protected FileConfiguration config = PropertyGuilds.getInstance().getConfig();

    public ConfigUtils2() {}

    @SuppressWarnings("RedundantCast")
    public void loadConfig() {
        put("MYSQL-HOST", (String) get("mysql.host"));
        put("MYSQL-PORT", (String) get("mysql.port"));
        put("MYSQL-USER", (String) get("mysql.user"));
        put("MYSQL-PASSWORD", (String) get("mysql.password"));
        put("MYSQL-DATABASE", (String) get("mysql.database"));
        put("CHAT-ENABLE", (boolean) get("chat.enable"));
        put("CHAT-FORMAT", (String) get("chat.format"));
        put("GUILD-CHAT-FORMAT", (String) get("guild-chat.format"));
        put("ALLY-CHAT-FORMAT", (String) get("ally-chat.format"));
        put("CREATE-REQUIRE-ITEMS-ENABLE", (boolean) get("create-require-items.enable"));
        put("CREATE-REQUIRE-ITEMS-GUI-SIZE", (int) get("create-require-items.gui-size"));
        put("CREATE-REQUIRE-ITEMS-LIST", (ArrayList<String>) get("create-require-items.list"));
        put("GUILD-TAG-MIN", (int) get("guild.tag.min"));
        put("GUILD-TAG-MAX", (int) get("guild.tag.max"));
        put("GUILD-NAME-MIN", (int) get("guild.name.min"));
        put("GUILD-NAME-MAX", (int) get("guild.name.max"));
        put("GUILD-PROPERTY-RADIUS", (int) get("guild.property.radius"));
        put("GUILD-PROPERTY-SPAWN-RADIUS", (int) get("guild.property.spawn-radius"));
        put("GUILD-POINTS-DEFAULT", (int) get("guild.points.default"));
        put("GUILD-DELETE-BROADCAST-ENABLE", (boolean) get("guild.delete.broadcast.enable"));
        put("GUILD-DELETE-PRIVATE-ENABLE", (boolean) get("guild.delete.private.enable"));
        put("GUILD-CREATE-BROADCAST-ENABLE", (boolean) get("guild.create.broadcast.enable"));
        put("GUILD-CREATE-PRIVATE-ENABLE", (boolean) get("guild.create.private.enable"));
        put("GUILD-BLOCKS-CHESTS-ALLOW-OPEN", (boolean) get("guild.blocks.chests.allow-open"));
        put("GUILD-COOLDOWNS-BASE-TELEPORT", (int) get("guild.cooldowns.base-teleport"));
        put("GUILD-DAMAGE-FRIENDLY-FIRE-ENABLE", (boolean) get("guild.damage.friendly-fire.enable"));
        put("GUILD-DAMAGE-ALLIANCES-ENABLE", (boolean) get("guild.damage.alliances.enable"));
        put("POINTS-KILL-PLAYER-WITHOUT-GUILD", (int) get("points.kill.player-without-guild"));
        put("POINTS-KILL-PLAYER-WITH-GUILD", (int) get("points.kill.player-with-guild"));
        put("POINTS-KILL-TO-GUILD", (int) get("points.kill.to-guild"));
        put("POINTS-DEATH-PLAYER-WITHOUT-GUILD", (int) get("points.death.player-without-guild"));
        put("POINTS-DEATH-PLAYER-WITH-GUILD", (int) get("points.death.player-with-guild"));
        put("POINTS-DEATH-TO-GUILD", (int) get("points.death.to-guild"));
    }

    protected void put(final String key, final Object value) {
        configHash.put(key, value);
    }

    protected Object get(final String get) {
        return config.get(get);
    }

    public Object getSetting(final String get) {
        return configHash.get(get);
    }

}
