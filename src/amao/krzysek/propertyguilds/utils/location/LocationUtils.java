package amao.krzysek.propertyguilds.utils.location;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationUtils {

    protected Location location;

    public LocationUtils(final Location location) {
        this.location = location;
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
                if (((guildLocation.getX() - guildRadius < location.getX()) && (location.getX() < guildLocation.getX() + guildRadius)) && ((guildLocation.getY() - guildRadius < location.getY()) && (location.getY() < guildLocation.getY() + guildRadius)) && ((guildLocation.getZ() - guildRadius < location.getZ()) && (location.getZ() < guildLocation.getZ() + guildRadius))) {
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

    public String getGuild() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `guilds`"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                final String tag = rs.getString("tag");
                final String[] split = rs.getString("location").split(";");
                final int guildRadius = rs.getInt("location_radius");
                final Location guildLocation = new Location(Bukkit.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                if (((guildLocation.getX() - guildRadius < location.getX()) && (location.getX() < guildLocation.getX() + guildRadius)) && ((guildLocation.getY() - guildRadius < location.getY()) && (location.getY() < guildLocation.getY() + guildRadius)) && ((guildLocation.getZ() - guildRadius < location.getZ()) && (location.getZ() < guildLocation.getZ() + guildRadius))) {
                    rs.close();
                    ps.close();
                    return tag;
                }
            }
            rs.close();
            ps.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
