package amao.krzysek.propertyguilds.commands;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ConfigMessageType;
import amao.krzysek.propertyguilds.utils.config.ConfigUtils;
import amao.krzysek.propertyguilds.utils.guild.Guild;
import amao.krzysek.propertyguilds.utils.map.MapUtils;
import amao.krzysek.propertyguilds.utils.mysql.MySQLUtils;
import amao.krzysek.propertyguilds.utils.user.OfflineUser;
import amao.krzysek.propertyguilds.utils.user.User;
import net.agentlv.namemanager.api.NameManagerAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Commands implements CommandExecutor {

    protected ConfigUtils config = new ConfigUtils(ConfigMessageType.CONFIG);
    protected ConfigUtils lang = new ConfigUtils(ConfigMessageType.LANG);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("guilds")) {
            if (sender instanceof Player) {
                User user = new User((Player) sender);
                if (args.length == 0) {
                    user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                    for (final String line : lang.getArrayList("guilds-help")) user.message(line);
                    user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                } else {
                    if (args[0].equalsIgnoreCase("items")) {
                        if (config.getBoolean("create-require-items.enable")) {
                            Inventory gui = Bukkit.createInventory(null, config.getInt("create-require-items.gui-size"), ChatColor.translateAlternateColorCodes('&', lang.getString("create-require-items-gui-name")));
                            for (final String list : config.getArrayList("create-require-items.list")) {
                                final int amount = Integer.parseInt(list.split("@")[0]);
                                final Material material = Material.getMaterial(list.split("@")[1]);
                                ItemStack is = new ItemStack(material, amount);
                                ItemMeta im = is.getItemMeta();
                                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&l" + amount + " &f&l" + material.name().toUpperCase()));
                                is.setItemMeta(im);
                                gui.addItem(is);
                            }
                            user.getPlayer().openInventory(gui);
                        } else user.message(lang.getString("none-create-require-items"));
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (user.getPlayer().hasPermission("propertyguilds.reload") || user.getPlayer().isOp()) {
                            PropertyGuilds.getInstance().loadFiles();
                            PropertyGuilds.getInstance().saveFiles();
                            user.message(lang.getString("plugin-reload"));
                        } else user.message(lang.getString("no-permission"));
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (args.length == 3) {
                            final String tag = args[1].toUpperCase();
                            final String name = args[2];
                            if (tag.length() >= config.getInt("guild.tag.min") && tag.length() <= config.getInt("guild.tag.max")) {
                                if (name.length() >= config.getInt("guild.name.min") && name.length() <= config.getInt("guild.name.max")) {
                                    if (!(user.hasGuild())) {
                                        MySQLUtils sqlUtils = new MySQLUtils();
                                        if (!(sqlUtils.guildTagExists(tag))) {
                                            if (!(sqlUtils.guildNameExists(name))) {
                                                if (user.atGuildProperty()) {
                                                    if (user.nearToGuildProperty()) {
                                                        if (user.nearToSpawn()) {
                                                            if (config.getBoolean("create-require-items.enable")) {
                                                                if (user.hasItemsForGuild()) {
                                                                    user.removeItemsForGuild();
                                                                } else
                                                                    user.message(lang.getString("guild.items.not-enough"));
                                                                Guild guild = new Guild(tag, name, user.getPlayer().getName(), 1, user.getPlayer().getName(), 0, "", user.getPlayer().getLocation().getWorld().getName() + ";" + (int) user.getPlayer().getLocation().getX() + ";" + (int) user.getPlayer().getLocation().getY() + ";" + (int) user.getPlayer().getLocation().getZ(), config.getInt("guild.property.radius"), config.getInt("guild.points.default"));
                                                                guild.create();
                                                                user.sendCreateSuccessMessage(tag, name, user.getPlayer().getName());
                                                                user.getPlayer().getLocation().getBlock().setType(Material.BEDROCK);
                                                                NameManagerAPI.setNametagPrefix(user.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&8[&c" + tag + "&8] "));
                                                            } else {
                                                                Guild guild = new Guild(tag, name, user.getPlayer().getName(), 1, user.getPlayer().getName(), 0, "", user.getPlayer().getLocation().getWorld().getName() + ";" + (int) user.getPlayer().getLocation().getX() + ";" + (int) user.getPlayer().getLocation().getY() + ";" + (int) user.getPlayer().getLocation().getZ(), config.getInt("guild.property.radius"), config.getInt("guild.points.default"));
                                                                guild.create();
                                                                user.sendCreateSuccessMessage(tag, name, user.getPlayer().getName());
                                                                user.getPlayer().getLocation().getBlock().setType(Material.BEDROCK);
                                                                NameManagerAPI.setNametagPrefix(user.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&8[&c" + tag + "&8] "));
                                                            }
                                                        } else user.message(lang.getString("guild.property.too-close-spawn").replaceAll("%distance%", String.valueOf(config.getInt("guild.property.spawn-radius"))));
                                                    } else user.message(lang.getString("guild.property.too-close").replaceAll("%distance%", String.valueOf(config.getInt("guild.property.radius"))));
                                                } else user.message(lang.getString("guild.property.exists"));
                                            } else user.message(lang.getString("guild.name.exists"));
                                        } else user.message(lang.getString("guild.tag.exists"));
                                    } else user.message(lang.getString("has-guild"));
                                } else user.message(lang.getString("guild.name.length").replaceAll("%min%", String.valueOf(config.getInt("guild.name.min"))).replaceAll("%max%", String.valueOf(config.getInt("guild.name.max"))));
                            } else user.message(lang.getString("guild.tag.length").replaceAll("%min%", String.valueOf(config.getInt("guild.tag.min"))).replaceAll("%max%", String.valueOf(config.getInt("guild.tag.max"))));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (args.length == 2) {
                            if (user.hasGuild()) {
                                final String tag = args[1].toUpperCase();
                                if (new MySQLUtils().guildTagExists(tag)) {
                                    Guild guild = new Guild(tag);
                                    if (user.getPlayer().getName().equals(guild.getInfo("leader"))) {
                                        final String[] mysqlMembersList = guild.getInfo("members_list").split(";");
                                        for (final String memberString : mysqlMembersList) {
                                            OfflineUser offlineUser = new OfflineUser(memberString);
                                            offlineUser.setGuild(null);
                                        }
                                        final String[] location = guild.getInfo("location").split(";");
                                        new Location(Bukkit.getServer().getWorld(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3])).getBlock().setType(Material.AIR);
                                        user.sendDeleteSuccessMessage(tag, guild.getInfo("name"),user.getPlayer().getName());
                                        guild.delete();
                                        NameManagerAPI.setNametagPrefix(user.getPlayer(), "");
                                    } else user.message(lang.getString("guild.delete.not-leader"));
                                } else user.message(lang.getString("guild-not-exists").replaceAll("%tag%", tag));
                            } else user.message(lang.getString("has-not-guild"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("info")) {
                        if (args.length == 1) {
                            if (user.hasGuild()) {
                                Guild guild = new Guild(user.getGuild());
                                final String tag = guild.getInfo("tag");
                                final String name = guild.getInfo("name");
                                final String leader = guild.getInfo("leader");
                                final String members = guild.getInfo("members");
                                final String alliances = guild.getInfo("alliances");
                                final String members_list = guild.getInfo("members_list");
                                final String alliances_list = guild.getInfo("alliances_list");
                                final String points = guild.getInfo("points");
                                user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                                user.message("&7" + lang.getString("guild-info.tag") + " &a" + tag);
                                user.message("&7" + lang.getString("guild-info.name") + " &a" + name);
                                user.message("&7" + lang.getString("guild-info.leader") + " &a" + leader);
                                user.message("&7" + lang.getString("guild-info.members") + " &a" + members);
                                user.message("&7" + lang.getString("guild-info.alliances") + " &a" + alliances);
                                user.message("&7" + lang.getString("guild-info.members-list") + " &a" + members_list.replaceAll(";", ", "));
                                user.message("&7" + lang.getString("guild-info.alliances-list") + " &a" + alliances_list);
                                user.message("&7" + lang.getString("guild-info.points") + " &a" + points);
                                user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                            } else user.message(lang.getString("has-not-guild"));
                        } else if (args.length == 2) {
                            if (new MySQLUtils().guildTagExists(args[1])) {
                                Guild guild = new Guild(args[1]);
                                final String tag = guild.getInfo("tag");
                                final String name = guild.getInfo("name");
                                final String leader = guild.getInfo("leader");
                                final String members = guild.getInfo("members");
                                final String alliances = guild.getInfo("alliances");
                                final String members_list = guild.getInfo("members_list");
                                final String alliances_list = guild.getInfo("alliances_list");
                                final String points = guild.getInfo("points");
                                user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                                user.message("&7" + lang.getString("guild-info.tag") + " &a" + tag);
                                user.message("&7" + lang.getString("guild-info.name") + " &a" + name);
                                user.message("&7" + lang.getString("guild-info.leader") + " &a" + leader);
                                user.message("&7" + lang.getString("guild-info.members") + " &a" + members);
                                user.message("&7" + lang.getString("guild-info.alliances") + " &a" + alliances);
                                user.message("&7" + lang.getString("guild-info.members-list") + " &a" + members_list.replaceAll(";", ", "));
                                user.message("&7" + lang.getString("guild-info.alliances-list") + " &a" + alliances_list);
                                user.message("&7" + lang.getString("guild-info.points") + " &a" + points);
                                user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                            } else user.message(lang.getString("guild-not-exists").replaceAll("%tag%", args[1]));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("base")) {
                        if (user.hasGuild()) {
                            // TODO: better checking for teleport abort
                            final int teleportCooldown = config.getInt("guild.cooldowns.base-teleport");
                            user.message(lang.getString("guild.teleport.base.remaining").replaceAll("%time%", String.valueOf(teleportCooldown)));
                            final Location start = user.getPlayer().getLocation();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    final Location end = user.getPlayer().getLocation();
                                    if ((int)start.getX() == (int)end.getX() && (int)start.getY() == (int)end.getY() && (int)start.getZ() == (int)end.getZ()) {
                                        Guild guild = new Guild(user.getGuild());
                                        final String[] loc = guild.getInfo("location").split(";");
                                        user.getPlayer().teleport(new Location(Bukkit.getServer().getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]) + 1, Integer.parseInt(loc[3])));
                                        user.message(lang.getString("guild.teleport.base.done"));
                                    } else user.message(lang.getString("guild.teleport.base.abort"));
                                }
                            }.runTaskLater(PropertyGuilds.getInstance(), teleportCooldown * 20L);
                        } else user.message(lang.getString("has-not-guild"));
                    } else if (args[0].equalsIgnoreCase("top")) {
                        final LinkedHashMap<String, Integer> sorted = MapUtils.sortValue(new MySQLUtils().getGuildsWithPoints());
                        user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                        int count = 0;
                        for (final Entry<String, Integer> entry : sorted.entrySet()) {
                            user.message("&c&l" + (count + 1) + ". &8&l" + entry.getKey() + " &c" + entry.getValue());
                            count++;
                        }
                        user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                    } else if (args[0].equalsIgnoreCase("chat")) {
                        if (user.hasGuild()) {
                            LinkedHashMap<String, Boolean> chatToggle = PropertyGuilds.getInstance().getChatToggle();
                            if (chatToggle.containsKey(user.getPlayer().getName())) {
                                chatToggle.replace(user.getPlayer().getName(), (!(chatToggle.get(user.getPlayer().getName()))));
                                if (chatToggle.get(user.getPlayer().getName())) user.message(lang.getString("guild.chat.guild-toggle"));
                                else user.message(lang.getString("guild.chat.global-toggle"));
                            }
                            else {
                                chatToggle.put(user.getPlayer().getName(), true);
                                user.message(lang.getString("guild.chat.guild-toggle"));
                            }
                        } else user.message(lang.getString("has-not-guild"));
                    }

                    // TODO: wrong syntax
                }
            } else {
                sender.sendMessage(lang.getString("console-execute"));
            }
        }
        return false;
    }

}
