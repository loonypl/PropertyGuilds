package amao.krzysek.propertyguilds.utils.user;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ChatType;
import amao.krzysek.propertyguilds.mysql.MySQL;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils2;
import amao.krzysek.propertyguilds.utils.config.MessageUtils;
import amao.krzysek.propertyguilds.utils.guild.Guild;
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
import java.util.LinkedHashMap;
import java.util.LinkedList;

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
                //final int guildRadius = rs.getInt("location_radius") + (new ConfigUtils(ConfigMessageType.CONFIG).getInt("guild.property.radius"));
                final int guildRadius = rs.getInt("location_radius") + ((int) new ConfigUtils2().getSetting("GUILD-PROPERTY-RADIUS"));
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
        //final int spawnRadius = new ConfigUtils(ConfigMessageType.CONFIG).getInt("guild.property.spawn-radius");
        final int spawnRadius = (int) new ConfigUtils2().getSetting("GUILD-PROPERTY-SPAWN-RADIUS");
        if (((spawnLocation.getX() - spawnRadius < player.getLocation().getX()) && (player.getLocation().getX() < spawnLocation.getX() + spawnRadius)) && ((spawnLocation.getY() - spawnRadius < player.getLocation().getY()) && (player.getLocation().getY() < spawnLocation.getY() + spawnRadius)) && ((spawnLocation.getZ() - spawnRadius < player.getLocation().getZ()) && (player.getLocation().getZ() < spawnLocation.getZ() + spawnRadius))) {
            return false;
        } else return true;
    }

    public boolean hasItemsForGuild() {
        //final ArrayList<String> items = new ConfigUtils(ConfigMessageType.CONFIG).getArrayList("create-require-items.list");
        final ArrayList<String> items = (ArrayList<String>) new ConfigUtils2().getSetting("CREATE-REQUIRE-ITEMS-LIST");
        Inventory inv = player.getInventory();
        for (final String split : items) {
            final int amount = Integer.parseInt(split.split("@")[0]);
            final Material material = Material.getMaterial(split.split("@")[1]);
            if (!(inv.containsAtLeast(new ItemStack(material), amount))) return false;
        }
        return true;
    }

    public void removeItemsForGuild() {
        final ArrayList<String> items = (ArrayList<String>) new ConfigUtils2().getSetting("CREATE-REQUIRE-ITEMS-LIST");
        Inventory inv = player.getInventory();
        for (final String split : items) {
            final int amount = Integer.parseInt(split.split("@")[0]);
            final Material material = Material.getMaterial(split.split("@")[1]);
            inv.removeItem(new ItemStack(material, amount));
        }
    }

    public void sendCreateSuccessMessage(final String tag, final String name, final String leader) {
        //ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);
        //ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
        ConfigUtils2 configUtils2 = new ConfigUtils2();
        MessageUtils messageUtils = new MessageUtils();
        //final boolean broadcastEnable = config.getBoolean("guild.create.broadcast.enable");
        final boolean broadcastEnable = (boolean) configUtils2.getSetting("GUILD-CREATE-BROADCAST-ENABLE");
        //final boolean privateEnable = config.getBoolean("guild.create.private.enable");
        final boolean privateEnable = (boolean) configUtils2.getSetting("GUILD-CREATE-PRIVATE-ENABLE");
        //final String broadcastMessage = lang.getString("guild.create.broadcast.message");
        final String broadcastMessage = (String) messageUtils.getMessage("GUILD-CREATE-BROADCAST-MESSAGE");
        //final String privateMessage = lang.getString("guild.create.private.message");
        final String privateMessage = (String) messageUtils.getMessage("GUILD-CREATE-PRIVATE-MESSAGE");
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
        //ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);
        //ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
        ConfigUtils2 configUtils2 = new ConfigUtils2();
        MessageUtils messageUtils = new MessageUtils();
        //final boolean broadcastEnable = config.getBoolean("guild.delete.broadcast.enable");
        final boolean broadcastEnable = (boolean) configUtils2.getSetting("GUILD-DELETE-BROADCAST-ENABLE");
        //final boolean privateEnable = config.getBoolean("guild.delete.private.enable");
        final boolean privateEnable = (boolean) configUtils2.getSetting("GUILD-DELETE-PRIVATE-ENABLE");
        //final String broadcastMessage = lang.getString("guild.delete.broadcast.message");
        final String broadcastMessage = (String) messageUtils.getMessage("GUILD-DELETE-BROADCAST-MESSAGE");
        //final String privateMessage = lang.getString("guild.delete.private.message");
        final String privateMessage = (String) messageUtils.getMessage("GUILD-DELETE-PRIVATE-MESSAGE");
        if (broadcastEnable) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage).replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
        if (privateEnable) message(privateMessage.replaceAll("%tag%", tag).replaceAll("%name%", name).replaceAll("%leader%", leader));
    }

    public boolean isLeader() {
        return new Guild(getGuild()).getInfo("leader").equals(this.player.getName());
    }

    public boolean isInvited(final String guild) {
        final LinkedHashMap<String, LinkedList<String>> invites = PropertyGuilds.getInstance().getInvites();
        return invites.containsKey(this.player.getName()) && (invites.get(this.player.getName()).contains(guild));
    }

    public void setInvite(final String guild) {
        final LinkedHashMap<String, LinkedList<String>> invites = PropertyGuilds.getInstance().getInvites();
        if (invites.containsKey(this.player.getName())) invites.get(this.player.getName()).add(guild);
        else {
            invites.put(this.player.getName(), new LinkedList<>());
            invites.get(this.player.getName()).add(guild);
        }
    }

    public void removeInvite(final String guild) {
        final LinkedHashMap<String, LinkedList<String>> invites = PropertyGuilds.getInstance().getInvites();
        if (invites.containsKey(this.player.getName())) invites.get(this.player.getName()).remove(guild);
    }

    public void setGuild(final String tag) {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement psPlayer = mysql.getConnection().prepareStatement(
                    "UPDATE `users` SET `guild`=? WHERE `name`=?"
            );
            psPlayer.setString(1, tag);
            psPlayer.setString(2, this.player.getName());
            psPlayer.executeUpdate();
            psPlayer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStats(final String stat, final int p) {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement psPlayer = mysql.getConnection().prepareStatement(
                    "UPDATE `users` SET `" + stat + "`=? WHERE `name`=?"
            );
            psPlayer.setInt(1, Integer.parseInt(getInfo(stat)) + p);
            psPlayer.setString(2, this.player.getName());
            psPlayer.executeUpdate();
            psPlayer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean setBaseTeleportListener() {
        LinkedHashMap<String, Location> baseTeleport = PropertyGuilds.getInstance().getBaseCooldown();
        if (!(baseTeleport.containsKey(this.player.getName()))) {
            baseTeleport.put(this.player.getName(), this.player.getLocation());
            return true;
        } else return false;
    }

    public boolean waitingForBaseTeleport() {
        return (PropertyGuilds.getInstance().getBaseCooldown().containsKey(this.player.getName()));
    }

    public boolean changedLocationBaseTeleport(final Location location) {
        LinkedHashMap<String, Location> baseTeleport = PropertyGuilds.getInstance().getBaseCooldown();
        final Location start = baseTeleport.get(this.player.getName());
        return (!((int)start.getX() == (int)location.getX() && (int)start.getY() == (int)location.getY() && (int)start.getZ() == (int)location.getZ()));
    }

    public void removeBaseTeleportListener() {
        LinkedHashMap<String, Location> baseTeleport = PropertyGuilds.getInstance().getBaseCooldown();
        baseTeleport.remove(this.player.getName());
    }

    public ChatType getChatType() {
        if (PropertyGuilds.getInstance().getChatToggle().containsKey(this.player.getName())) return PropertyGuilds.getInstance().getChatToggle().get(this.player.getName());
        else return ChatType.NULL;
    }

}
