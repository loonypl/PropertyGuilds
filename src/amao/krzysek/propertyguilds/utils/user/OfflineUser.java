package amao.krzysek.propertyguilds.utils.user;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.mysql.MySQL;
import amao.krzysek.propertyguilds.utils.guild.Guild;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OfflineUser {

    protected String player;

    public OfflineUser(final String player) {
        this.player = player;
    }

    public void setGuild(final String tag) {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement psPlayer = mysql.getConnection().prepareStatement(
                    "UPDATE `users` SET `guild`=? WHERE `name`=?"
            );
            psPlayer.setString(1, tag);
            psPlayer.setString(2, this.player);
            psPlayer.executeUpdate();
            psPlayer.close();
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
            checkStatement.setString(1, this.player);
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
            checkStatement.setString(1, this.player);
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

    public String getName() {
        return this.player;
    }

    public boolean isOnline() {
        return (Bukkit.getServer().getPlayer(this.player) != null);
    }

    public boolean isLeader() {
        return new Guild(getGuild()).getInfo("leader").equals(this.player);
    }

}
