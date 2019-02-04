package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.mysql.MySQL;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInteract implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interactEvent(final PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST) && new ConfigUtils(ConfigMessageType.CONFIG).getBoolean("guild.blocks.chests.allow-open"))) {
            MySQL mysql = PropertyGuilds.getInstance().getMySQL();
            try {
                PreparedStatement ps = mysql.getConnection().prepareStatement(
                        "SELECT * FROM `guilds`"
                );
                ResultSet rs = ps.executeQuery();
                User user = new User(e.getPlayer());
                final String userGuild = user.getGuild();
                while (rs.next()) {
                    final String guildTag = rs.getString("tag");
                    if (userGuild == null || !(userGuild.equalsIgnoreCase(guildTag))) {
                        final String[] split = rs.getString("location").split(";");
                        final int guildRadius = rs.getInt("location_radius");
                        final Location blockLocation = e.getClickedBlock().getLocation();
                        final Location guildLocation = new Location(Bukkit.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                        if (((guildLocation.getX() - guildRadius < blockLocation.getX()) && (blockLocation.getX() < guildLocation.getX() + guildRadius)) && ((guildLocation.getY() - guildRadius < blockLocation.getY()) && (blockLocation.getY() < guildLocation.getY() + guildRadius)) && ((guildLocation.getZ() - guildRadius < blockLocation.getZ()) && (blockLocation.getZ() < guildLocation.getZ() + guildRadius))) {
                            rs.close();
                            ps.close();
                            e.setCancelled(true);
                            final ConfigUtils configUtils = new ConfigUtils(ConfigMessageType.LANG);
                            user.message(configUtils.getString("guild.blocks.chests.cannot-open"));
                            break;
                        }
                    }
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
