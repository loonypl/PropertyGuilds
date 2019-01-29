package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.location.LocationUtils;
import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void playerMove(final PlayerMoveEvent e) {
        LocationUtils from = new LocationUtils(e.getFrom());
        LocationUtils to = new LocationUtils(e.getTo());
        if (from.atGuildProperty()) {
            if (!(to.atGuildProperty())) {
                // greetings
                User user = new User(e.getPlayer());
                user.message(new ConfigUtils(ConfigMessageType.LANG).getString("guild.property.entered").replaceAll("%tag%", to.getGuild()));
            }
        } else if (!(from.atGuildProperty())) {
            if (to.atGuildProperty()) {
                // farewell
                User user = new User(e.getPlayer());
                user.message(new ConfigUtils(ConfigMessageType.LANG).getString("guild.property.left").replaceAll("%tag%", from.getGuild()));
            }
        }
    }

}
