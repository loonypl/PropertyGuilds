package amao.krzysek.propertyguilds.listeners;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.guild.Guild;
import amao.krzysek.propertyguilds.utils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.LinkedHashMap;

public class PlayerChat implements Listener {

    @EventHandler
    public void playerChat(final AsyncPlayerChatEvent e) {
        ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
        final LinkedHashMap<String, Boolean> chatToggle = PropertyGuilds.getInstance().getChatToggle();
        if ((!(chatToggle.containsKey(e.getPlayer().getName()))) || (!(chatToggle.get(e.getPlayer().getName())))) {
            if (config.getBoolean("chat.enable")) {
                User user = new User(e.getPlayer());
                String message;
                if (e.getMessage().contains("&")) {
                    if (user.getPlayer().hasPermission("propertyguilds.chatcolor") || user.getPlayer().isOp())
                        message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
                    else message = e.getMessage();
                } else message = e.getMessage();
                final String guildTag = user.getInfo("guild");
                Guild guild = new Guild(guildTag);
                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', config.getString("chat.format"))
                        .replaceAll("%guild-tag%", guildTag)
                        .replaceAll("%guild-name%", guild.getInfo("name"))
                        .replaceAll("%guild-points%", guild.getInfo("points"))
                        .replaceAll("%name%", user.getPlayer().getDisplayName())
                        .replaceAll("%kills%", user.getInfo("kills"))
                        .replaceAll("%deaths%", user.getInfo("deaths"))
                        .replaceAll("%points%", user.getInfo("points"))
                        .replaceAll("%world%", user.getPlayer().getWorld().getName())
                        .replaceAll("%message%", message)
                );
                e.setCancelled(true);
            }
        } else if (chatToggle.get(e.getPlayer().getName())) {
            User user = new User(e.getPlayer());
            String message;
            if (e.getMessage().contains("&")) {
                if (user.getPlayer().hasPermission("propertyguilds.chatcolor") || user.getPlayer().isOp())
                    message = ChatColor.translateAlternateColorCodes('&', e.getMessage());
                else message = e.getMessage();
            } else message = e.getMessage();
            final String guildTag = user.getInfo("guild");
            Guild guild = new Guild(guildTag);
            final String members = guild.getInfo("members_list");
            for (final String nickname : members.split(";")) {
                if (Bukkit.getServer().getPlayer(nickname) != null) {
                    new User(Bukkit.getServer().getPlayer(nickname)).message(ChatColor.translateAlternateColorCodes('&', config.getString("guild-chat.format"))
                            .replaceAll("%guild-tag%", guildTag)
                            .replaceAll("%guild-name%", guild.getInfo("name"))
                            .replaceAll("%guild-points%", guild.getInfo("points"))
                            .replaceAll("%name%", user.getPlayer().getDisplayName())
                            .replaceAll("%kills%", user.getInfo("kills"))
                            .replaceAll("%deaths%", user.getInfo("deaths"))
                            .replaceAll("%points%", user.getInfo("points"))
                            .replaceAll("%world%", user.getPlayer().getWorld().getName())
                            .replaceAll("%message%", message)
                    );
                }
            }
            e.setCancelled(true);
        }
    }

}
