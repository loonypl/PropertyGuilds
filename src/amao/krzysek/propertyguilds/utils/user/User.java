package amao.krzysek.propertyguilds.utils.user;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.mysql.MySQL;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    protected Player player;

    public User(final Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void message(final String message) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6> " + message));
    }

    public void insertFirstDataMySQL() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `users` WHERE name=?"
            );
            checkStatement.setString(1, this.player.getName());
            ResultSet result = checkStatement.executeQuery();
            final boolean hasData = result.next();
            result.close();
            checkStatement.close();
            if (!(hasData)) {
                PreparedStatement setStatement = mysql.getConnection().prepareStatement(
                        "INSERT INTO `users` (`name`, `points`, `kills`, `deaths`, `guild`) VALUES (?, 0, 0, 0, null)"
                );
                setStatement.setString(1, this.player.getName());
                setStatement.execute();
                setStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasGuild() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT `guild` FROM `users` WHERE name=?"
            );
            checkStatement.setString(1, this.player.getName());
            ResultSet result = checkStatement.executeQuery();
            boolean has;
            if (result.next()) {
                result.getString("guild");
                if (result.wasNull()) has = false;
                else has = true;
            } else return false;
            checkStatement.close();
            result.close();
            return has;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getGuild() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT `guild` FROM `users` WHERE name=?"
            );
            checkStatement.setString(1, this.player.getName());
            ResultSet result = checkStatement.executeQuery();
            String has;
            if (result.next()) {
                String guildTag = result.getString("guild");
                if (result.wasNull()) has = null;
                else has = guildTag;
            } else return null;
            checkStatement.close();
            result.close();
            return has;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean atGuildProperty() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `guilds`"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String[] split = rs.getString("location").split(";");
                final int guildRadius = rs.getInt("location_radius");
                final Location guildLocation = new Location(Bukkit.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                if (((guildLocation.getX() - guildRadius < player.getLocation().getX()) && (player.getLocation().getX() < guildLocation.getX() + guildRadius)) && ((guildLocation.getY() - guildRadius < player.getLocation().getY()) && (player.getLocation().getY() < guildLocation.getY() + guildRadius)) && ((guildLocation.getZ() - guildRadius < player.getLocation().getZ()) && (player.getLocation().getZ() < guildLocation.getZ() + guildRadius))) {
                    rs.close();
                    ps.close();
                    return false;
                }
            }
            rs.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean nearToGuildProperty() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `guilds`"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String[] split = rs.getString("location").split(";");
                final int guildRadius = rs.getInt("location_radius") + (new ConfigUtils(ConfigMessageType.CONFIG).getInt("guild.property.radius"));
                final Location guildLocation = new Location(Bukkit.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                if (((guildLocation.getX() - guildRadius < player.getLocation().getX()) && (player.getLocation().getX() < guildLocation.getX() + guildRadius)) && ((guildLocation.getY() - guildRadius < player.getLocation().getY()) && (player.getLocation().getY() < guildLocation.getY() + guildRadius)) && ((guildLocation.getZ() - guildRadius < player.getLocation().getZ()) && (player.getLocation().getZ() < guildLocation.getZ() + guildRadius))) {
                    rs.close();
                    ps.close();
                    return false;
                }
            }
            rs.close();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean nearToSpawn() {
        final Location spawnLocation = player.getLocation().getWorld().getSpawnLocation();
        final int spawnRadius = new ConfigUtils(ConfigMessageType.CONFIG).getInt("guild.property.spawn-radius");
        if (((spawnLocation.getX() - spawnRadius < player.getLocation().getX()) && (player.getLocation().getX() < spawnLocation.getX() + spawnRadius)) && ((spawnLocation.getY() - spawnRadius < player.getLocation().getY()) && (player.getLocation().getY() < spawnLocation.getY() + spawnRadius)) && ((spawnLocation.getZ() - spawnRadius < player.getLocation().getZ()) && (player.getLocation().getZ() < spawnLocation.getZ() + spawnRadius))) {
            return false;
        } else return true;
    }

    public boolean hasItemsForGuild() {
        final ArrayList<String> items = new ConfigUtils(ConfigMessageType.CONFIG).getArrayList("create-require-items.list");
        Inventory inv = player.getInventory();
        for (final String split : items) {
            final int amount = Integer.parseInt(split.split("@")[0]);
            final Material material = Material.getMaterial(split.split("@")[1]);
            if (!(inv.containsAtLeast(new ItemStack(material), amount))) return false;
        }
        return true;
    }

    public void removeItemsForGuild() {
        final ArrayList<String> items = new ConfigUtils(ConfigMessageType.CONFIG).getArrayList("create-require-items.list");
        Inventory inv = player.getInventory();
        for (final String split : items) {
            final int amount = Integer.parseInt(split.split("@")[0]);
            final Material material = Material.getMaterial(split.split("@")[1]);
            inv.removeItem(new ItemStack(material, amount));
        }
    }

    public void sendCreateSuccessMessage(final String tag, final String name, final String leader) {
        ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);
        ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
        final boolean broadcastEnable = config.getBoolean("guild.create.broadcast.enable");
        final boolean privateEnable = config.getBoolean("guild.create.private.enable");
        final String broadcastMessage = lang.getString("guild.create.broadcast.message");
        final String privateMessage = lang.getString("guild.create.private.message");
        if (broadcastEnable) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage).replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
        if (privateEnable) message(privateMessage.replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
    }

    public String getInfo(final String info) {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `users` WHERE name=?"
            );
            checkStatement.setString(1, this.player.getName());
            ResultSet result = checkStatement.executeQuery();
            String has;
            if (result.next()) {
                String str = result.getString(info);
                if (result.wasNull()) has = "-";
                else has = str;
            } else return "-";
            checkStatement.close();
            result.close();
            return has;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendDeleteSuccessMessage(final String tag, final String name, final String leader) {
        ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);
        ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
        final boolean broadcastEnable = config.getBoolean("guild.delete.broadcast.enable");
        final boolean privateEnable = config.getBoolean("guild.delete.private.enable");
        final String broadcastMessage = lang.getString("guild.delete.broadcast.message");
        final String privateMessage = lang.getString("guild.delete.private.message");
        if (broadcastEnable) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage).replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
        if (privateEnable) message(privateMessage.replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
    }

}
