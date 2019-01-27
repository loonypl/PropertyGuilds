package amao.krzysek.propertyguilds.utils.user;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.mysql.MySQL;

import java.sql.PreparedStatement;
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

}
