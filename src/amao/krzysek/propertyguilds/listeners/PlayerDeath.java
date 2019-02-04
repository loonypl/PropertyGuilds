package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.guild.Guild;
import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void playerDeath(final PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
            User user = new User(e.getEntity());
            User killer = new User(e.getEntity().getKiller());
            if (user.hasGuild()) {
                killer.updateStats("points", config.getInt("points.kill.player-with-guild"));
                user.updateStats("points", config.getInt("points.death.player-with-guild"));
                if (killer.hasGuild()) new Guild(killer.getGuild()).points(config.getInt("points.kill.to-guild"));
                new Guild(user.getGuild()).points(config.getInt("points.death.to-guild"));
            }
            else {
                killer.updateStats("points", config.getInt("points.kill.player-without-guild"));
                user.updateStats("points", config.getInt("points.death.player-without-guild"));
                if (killer.hasGuild()) new Guild(killer.getGuild()).points(config.getInt("points.kill.to-guild"));
            }
            killer.updateStats("kills", 1);
            user.updateStats("deaths", 1);
        }
    }

}
