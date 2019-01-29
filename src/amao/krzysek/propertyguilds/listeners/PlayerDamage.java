package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void playerDamage(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            final User user = new User((Player) e.getEntity());
            if (user.hasGuild()) {
                final User damager = new User((Player) e.getDamager());
                if (damager.hasGuild()) {
                    if (user.getGuild().equals(damager.getGuild())) {
                        ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);
                        if (lang.getBoolean("guild.damage.friendly-fire.enable")) damager.message(lang.getString("guild.damage.friendly-fire.message"));
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

}
