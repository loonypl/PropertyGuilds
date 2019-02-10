package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.utils.config.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void inventoryClick(final InventoryClickEvent e) {
        //if (e.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', new ConfigUtils(ConfigMessageType.LANG).getString("create-require-items-gui-name")))) e.setCancelled(true);
        if (e.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', (String) new MessageUtils().getMessage("CREATE-REQUIRE-ITEMS-GUI-NAME")))) e.setCancelled(true);
    }

}
