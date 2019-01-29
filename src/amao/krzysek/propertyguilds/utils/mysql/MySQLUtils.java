package amao.krzysek.propertyguilds.utils.mysql;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MySQLUtils {

    protected MySQL mysql;

    public MySQLUtils() {
        mysql = PropertyGuilds.getInstance().getMySQL();
    }

    public boolean guildTagExists(final String tag) {
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT `tag` FROM `guilds` WHERE tag=?"
            );
            checkStatement.setString(1, tag);
            ResultSet result = checkStatement.executeQuery();
            boolean has;
            if (result.next()) {
                result.getString("tag");
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

    public boolean guildNameExists(final String name) {
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT `name` FROM `guilds` WHERE name=?"
            );
            checkStatement.setString(1, name);
            ResultSet result = checkStatement.executeQuery();
            boolean has;
            if (result.next()) {
                result.getString("name");
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

    public LinkedHashMap<String, Integer> getGuildsWithPoints() {
        LinkedHashMap<String, Integer> guilds = new LinkedHashMap<>();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `guilds`"
            );
            ResultSet rs = ps.executeQuery();
            while(rs.next()) guilds.put(rs.getString("tag"), rs.getInt("points"));
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guilds;
    }

}
