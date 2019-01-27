package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerServerJoin implements Listener {

    @EventHandler
    public void playerServerJoin(final PlayerLoginEvent e) {
        User user = new User(e.getPlayer());
        // check if player has data in mysql `users`
        user.insertFirstDataMySQL();
    }

}
