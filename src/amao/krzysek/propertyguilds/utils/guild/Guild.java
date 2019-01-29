package amao.krzysek.propertyguilds.utils.guild;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Guild {

    protected String tag, name, leader, members_list, alliances_list, location;
    protected int members, alliances, locationRadius, points;

    public Guild(final String tag, final String name, final String leader, final int members, final String members_list, final int alliances, final String alliances_list, final String location, final int locationRadius, final int points) {
        this.tag = tag;
        this.name = name;
        this.leader = leader;
        this.members_list = members_list;
        this.alliances_list = alliances_list;
        this.location = location;
        this.members = members;
        this.alliances = alliances;
        this.locationRadius = locationRadius;
        this.points = points;
    }

    public Guild(final String tag) {
        this.tag = tag;
    }

    public void create() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement psGuild = mysql.getConnection().prepareStatement(
                    "INSERT INTO `guilds`(`tag`, `name`, `leader`, `members`, `members_list`, `alliances`, `alliances_list`, `location`, `location_radius`, `points`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            psGuild.setString(1, tag);
            psGuild.setString(2, name);
            psGuild.setString(3, leader);
            psGuild.setInt(4, members);
            psGuild.setString(5, leader);
            psGuild.setInt(6, alliances);
            psGuild.setString(7, alliances_list);
            psGuild.setString(8, location);
            psGuild.setInt(9, locationRadius);
            psGuild.setInt(10, points);
            psGuild.executeUpdate();
            psGuild.close();
            PreparedStatement psPlayer = mysql.getConnection().prepareStatement(
                    "UPDATE `users` SET `guild`='" + tag + "' WHERE `name`='" + leader + "'"
            );
            psPlayer.executeUpdate();
            psPlayer.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getInfo(final String info) {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement checkStatement = mysql.getConnection().prepareStatement(
                    "SELECT * FROM `guilds` WHERE tag=?"
            );
            checkStatement.setString(1, this.tag);
            ResultSet result = checkStatement.executeQuery();
            String has;
            if (result.next()) {
                String str = result.getString(info);
                if (result.wasNull()) has = null;
                else has = str;
            } else return null;
            checkStatement.close();
            result.close();
            return has;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete() {
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "DELETE FROM `guilds` WHERE `tag`=?"
            );
            ps.setString(1, this.tag);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMember(final String member) {
        String members = getInfo("members");
        String members_list = getInfo("members_list");
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "UPDATE `guilds` SET `members`=?, `members_list`=? WHERE `tag`='" + this.tag + "'"
            );
            ps.setInt(1, Integer.parseInt(members) + 1);
            ps.setString(2, members_list + ";" + member);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeMember(final String member) {
        String members = getInfo("members");
        final String members_list = getInfo("members_list");
        String new_members_list = "";
        for (final String player : members_list.split(";")) {
            if (!(player.equalsIgnoreCase(member))) {
                if (new_members_list.equals("")) new_members_list = player;
                else new_members_list = new_members_list + ";" + player;
            }
        }
        MySQL mysql = PropertyGuilds.getInstance().getMySQL();
        try {
            PreparedStatement ps = mysql.getConnection().prepareStatement(
                    "UPDATE `guilds` SET `members`=?, `members_list`=? WHERE `tag` = '" + this.tag + "'"
            );
            ps.setInt(1, Integer.parseInt(members) - 1);
            ps.setString(2, new_members_list);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
