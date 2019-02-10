package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.utils.config.ConfigUtils2;
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
            //ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
            ConfigUtils2 configUtils2 = new ConfigUtils2();
            User user = new User(e.getEntity());
            User killer = new User(e.getEntity().getKiller());
            if (user.hasGuild()) {
                //killer.updateStats("points", config.getInt("points.kill.player-with-guild"));
                //user.updateStats("points", config.getInt("points.death.player-with-guild"));
                //if (killer.hasGuild()) new Guild(killer.getGuild()).points(config.getInt("points.kill.to-guild"));
                //new Guild(user.getGuild()).points(config.getInt("points.death.to-guild"));
                killer.updateStats("points", (int) configUtils2.getSetting("POINTS-KILL-PLAYER-WITH-GUILD"));
                user.updateStats("points", (int) configUtils2.getSetting("POINTS-DEATH-PLAYER-WITH-GUILD"));
                if (killer.hasGuild()) new Guild(killer.getGuild()).points((int) configUtils2.getSetting("POINTS-KILL-TO-GUILD"));
                new Guild(user.getGuild()).points((int) configUtils2.getSetting("POINTS-DEATH-TO-GUILD"));
            }
            else {
                //killer.updateStats("points", config.getInt("points.kill.player-without-guild"));
                //user.updateStats("points", config.getInt("points.death.player-without-guild"));
                //if (killer.hasGuild()) new Guild(killer.getGuild()).points(config.getInt("points.kill.to-guild"));
                killer.updateStats("points", (int) configUtils2.getSetting("POINTS-KILL-PLAYER-WITHOUT-GUILD"));
                user.updateStats("points", (int) configUtils2.getSetting("POINTS-DEATH-PLAYER-WITHOUT-GUILD"));
                if (killer.hasGuild()) new Guild(killer.getGuild()).points((int) configUtils2.getSetting("POINTS-KILL-TO-GUILD"));
            }
            killer.updateStats("kills", 1);
            user.updateStats("deaths", 1);
        }
    }

}
