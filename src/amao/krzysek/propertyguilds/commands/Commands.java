package amao.krzysek.propertyguilds.commands;

import amao.krzysek.propertyguilds.PropertyGuilds;
import amao.krzysek.propertyguilds.enums.ChatType;
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
                                                                    Guild guild = new Guild(tag, name, user.getPlayer().getName(), 1, user.getPlayer().getName(), 0, "", user.getPlayer().getLocation().getWorld().getName() + ";" + (int) user.getPlayer().getLocation().getX() + ";" + (int) user.getPlayer().getLocation().getY() + ";" + (int) user.getPlayer().getLocation().getZ(), config.getInt("guild.property.radius"), config.getInt("guild.points.default"));
                                                                    guild.create();
                                                                    user.sendCreateSuccessMessage(tag, name, user.getPlayer().getName());
                                                                    user.getPlayer().getLocation().getBlock().setType(Material.BEDROCK);
                                                                    NameManagerAPI.setNametagPrefix(user.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&8[&c" + tag + "&8] "));
                                                                } else user.message(lang.getString("guild.items.not-enough"));
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
                                            if (Bukkit.getServer().getPlayer(memberString) != null) NameManagerAPI.clearNametag(Bukkit.getServer().getPlayer(memberString));
                                        }
                                        final String[] location = guild.getInfo("location").split(";");
                                        new Location(Bukkit.getServer().getWorld(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]), Integer.parseInt(location[3])).getBlock().setType(Material.AIR);
                                        user.sendDeleteSuccessMessage(tag, guild.getInfo("name"),user.getPlayer().getName());
                                        guild.delete();
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
                                user.message("&7" + lang.getString("guild-info.alliances-list") + " &a" + alliances_list.replaceAll(";", ", "));
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
                                user.message("&7" + lang.getString("guild-info.alliances-list") + " &a" + alliances_list.replaceAll(";", ", "));
                                user.message("&7" + lang.getString("guild-info.points") + " &a" + points);
                                user.message("&7&l--------------- &e&lGuilds &7&l---------------");
                            } else user.message(lang.getString("guild-not-exists").replaceAll("%tag%", args[1]));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("base")) {
                        if (user.hasGuild()) {
                            final int teleportCooldown = config.getInt("guild.cooldowns.base-teleport");
                            user.message(lang.getString("guild.teleport.base.remaining").replaceAll("%time%", String.valueOf(teleportCooldown)));
                            if (user.setBaseTeleportListener()) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (user.waitingForBaseTeleport()) {
                                            Guild guild = new Guild(user.getGuild());
                                            final String[] loc = guild.getInfo("location").split(";");
                                            user.getPlayer().teleport(new Location(Bukkit.getServer().getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]) + 1, Integer.parseInt(loc[3])));
                                            user.message(lang.getString("guild.teleport.base.done"));
                                            user.removeBaseTeleportListener();
                                        }
                                    }
                                }.runTaskLaterAsynchronously(PropertyGuilds.getInstance(), teleportCooldown * 20L);
                            } else user.message(lang.getString("guild.teleport.base.already"));
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
                        if (args.length == 2) {
                            if (user.hasGuild()) {
                                LinkedHashMap<String, ChatType> chatToggle = PropertyGuilds.getInstance().getChatToggle();
                                if (args[1].equalsIgnoreCase("global")) {
                                    if (chatToggle.containsKey(user.getPlayer().getName())) chatToggle.replace(user.getPlayer().getName(), ChatType.GLOBAL);
                                    else chatToggle.put(user.getPlayer().getName(), ChatType.GLOBAL);
                                    user.message(lang.getString("guild.chat.global-toggle"));
                                } else if (args[1].equalsIgnoreCase("guild")) {
                                    if (chatToggle.containsKey(user.getPlayer().getName())) chatToggle.replace(user.getPlayer().getName(), ChatType.GUILD);
                                    else chatToggle.put(user.getPlayer().getName(), ChatType.GUILD);
                                    user.message(lang.getString("guild.chat.guild-toggle"));
                                } else if (args[1].equalsIgnoreCase("ally")) {
                                    if (chatToggle.containsKey(user.getPlayer().getName())) chatToggle.replace(user.getPlayer().getName(), ChatType.ALLY);
                                    else chatToggle.put(user.getPlayer().getName(), ChatType.ALLY);
                                    user.message(lang.getString("guild.chat.ally-toggle"));
                                } else user.message(lang.getString("wrong-syntax"));
                            } else user.message(lang.getString("has-not-guild"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equals("invite")) {
                        if (args.length == 2) {
                            if (user.hasGuild()) {
                                if (user.isLeader()) {
                                    if (Bukkit.getServer().getPlayer(args[1]) != null) {
                                        User online = new User(Bukkit.getServer().getPlayer(args[1]));
                                        if (!(online.hasGuild())) {
                                            final String tag = user.getGuild();
                                            if (!(online.isInvited(tag))) {
                                                online.setInvite(tag);
                                                user.message(lang.getString("guild.invite.sent").replaceAll("%invited%", args[1]));
                                                online.message(lang.getString("guild.invite.recieved").replaceAll("%guild%", tag));
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        online.removeInvite(tag);
                                                    }
                                                }.runTaskLaterAsynchronously(PropertyGuilds.getInstance(), 20L * 300); // removes invitation after 5 mins
                                            } else user.message(lang.getString("guild.invite.already"));
                                        } else user.message(lang.getString("guild.invite.has-guild"));
                                    } else user.message(lang.getString("guild.invite.offline"));
                                } else user.message(lang.getString("guild.invite.not-leader"));
                            } else user.message(lang.getString("has-not-guild"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("accept")) {
                        if (args.length == 2) {
                            if (!(user.hasGuild())) {
                                final String tag = args[1].toUpperCase();
                                if (new MySQLUtils().guildTagExists(tag)) {
                                    if (user.isInvited(tag)) {
                                        Guild guild = new Guild(tag);
                                        guild.addMember(user.getPlayer().getName());
                                        user.setGuild(tag);
                                        user.removeInvite(tag);
                                        user.message(lang.getString("guild.invite.success-accepted").replaceAll("%guild%", tag));
                                        final String leader = guild.getInfo("leader");
                                        if (Bukkit.getServer().getPlayer(leader) != null) new User(Bukkit.getServer().getPlayer(leader)).message(lang.getString("guild.invite.accepted").replaceAll("%invited%", user.getPlayer().getName()));
                                        NameManagerAPI.setNametagPrefix(user.getPlayer(), ChatColor.translateAlternateColorCodes('&', "&8[&c" + tag + "&8] "));
                                    } else user.message(lang.getString("guild.invite.not-invited"));
                                } else user.message(lang.getString("guild.invite.guild-not-exists"));
                            } else user.message(lang.getString("guild.invite.already-member"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("decline")) {
                        if (args.length == 2) {
                            final String tag = args[1].toUpperCase();
                            if (new MySQLUtils().guildTagExists(tag)) {
                                if (user.isInvited(tag)) {
                                    user.removeInvite(tag);
                                    user.message(lang.getString("guild.invite.success-declined").replaceAll("%guild%", tag));
                                    final String leader = new Guild(tag).getInfo("leader");
                                    if (Bukkit.getServer().getPlayer(leader) != null) new User(Bukkit.getServer().getPlayer(leader)).message(lang.getString("guild.invite.declined").replaceAll("%invited%", user.getPlayer().getName()));
                                } else user.message(lang.getString("guild.invite.not-invited"));
                            } else user.message(lang.getString("guild.invite.guild-not-exists"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (user.hasGuild()) {
                            if (!(user.isLeader())) {
                                final String tag = user.getGuild();
                                user.setGuild(null);
                                new Guild(tag).removeMember(user.getPlayer().getName());
                                if (lang.getBoolean("guild.leave.broadcast.enable")) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("guild.leave.broadcast.left").replaceAll("%player%", user.getPlayer().getName()).replaceAll("%guild%", tag)));
                                user.message(lang.getString("guild.leave.left").replaceAll("%guild%", tag));
                                NameManagerAPI.clearNametag(user.getPlayer());
                            } else user.message(lang.getString("guild.leave.leader"));
                        } else user.message(lang.getString("has-not-guild"));
                    } else if (args[0].equalsIgnoreCase("kick")) {
                        if (args.length == 2) {
                            if (user.hasGuild()) {
                                if (user.isLeader()) {
                                    OfflineUser kick = new OfflineUser(args[1]);
                                    final String tag = user.getGuild();
                                    if (kick.hasGuild()) {
                                        if (!(kick.isLeader())) {
                                            if (tag.equals(kick.getGuild())) {
                                                Guild guild = new Guild(user.getGuild());
                                                guild.removeMember(kick.getName());
                                                kick.setGuild(null);
                                                user.message(lang.getString("guild.kick.kicked").replaceAll("%player%", kick.getName()));
                                                if (kick.isOnline()) {
                                                    User kickOnline = new User(Bukkit.getServer().getPlayer(kick.getName()));
                                                    kickOnline.message(lang.getString("guild.kick.kick-recieve").replaceAll("%guild%", tag));
                                                    NameManagerAPI.clearNametag(kickOnline.getPlayer());
                                                }
                                            } else user.message(lang.getString("guild.kick.not-belong"));
                                        } else user.message(lang.getString("guild.kick.leader-given"));
                                    } else user.message(lang.getString("guild.kick.has-not-guild"));
                                } else user.message(lang.getString("guild.kick.leader"));
                            } else user.message(lang.getString("has-not-guild"));
                        } else user.message(lang.getString("wrong-syntax"));
                    } else if (args[0].equalsIgnoreCase("ally")) {
                        if (args.length == 3) {
                            if (args[1].equalsIgnoreCase("invite")) {
                                if (user.hasGuild()) {
                                    if (user.isLeader()) {
                                        final String tag = args[2].toUpperCase();
                                        if (new MySQLUtils().guildTagExists(tag)) {
                                            final String guildTag = user.getGuild();
                                            Guild guild = new Guild(guildTag);
                                            if (!(guildTag.equalsIgnoreCase(tag))) {
                                                if (!(guild.isAllied(tag))) {
                                                    if (!(guild.hasAllyInviteSent(tag))) {
                                                        Guild invited = new Guild(tag);
                                                        if (invited.isLeaderOnline()) {
                                                            guild.sendAlly(tag);
                                                            user.message(lang.getString("guild.ally.sent").replaceAll("%guild%", tag));
                                                            new User(Bukkit.getServer().getPlayer(invited.getInfo("leader"))).message(lang.getString("guild.ally.ally-recieve").replaceAll("%guild%", guildTag));
                                                            new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    invited.deleteAllyInvite(guildTag);
                                                                }
                                                            }.runTaskLaterAsynchronously(PropertyGuilds.getInstance(), 20L * 300); // remove invitation after 5 mins
                                                        } else
                                                            user.message(lang.getString("guild.ally.leader-not-online").replaceAll("%guild%", tag));
                                                    } else user.message(lang.getString("guild.ally.already-sent"));
                                                } else user.message(lang.getString("guild.ally.already-allied"));
                                            } else user.message(lang.getString("guild.ally.own-guild"));
                                        } else user.message(lang.getString("guild.ally.guild-not-exists"));
                                    } else user.message(lang.getString("guild.ally.leader"));
                                } else user.message(lang.getString("has-not-guild"));
                            } else if (args[1].equalsIgnoreCase("accept")) {
                                if (user.hasGuild()) {
                                    if (user.isLeader()) {
                                        final String tag = args[2].toUpperCase();
                                        if (new MySQLUtils().guildTagExists(tag)) {
                                            Guild guild = new Guild(user.getGuild());
                                            if (!(guild.isAllied(tag))) {
                                                if (guild.recievedAllyInvite(tag)) {
                                                    guild.acceptAlly(tag);
                                                    guild.acceptAllySide(tag);
                                                    user.message(lang.getString("guild.ally.accepted").replaceAll("%guild%", tag));
                                                    Guild ally = new Guild(tag);
                                                    if (ally.isLeaderOnline()) new User(Bukkit.getServer().getPlayer(ally.getInfo("leader"))).message(lang.getString("guild.ally.accepted-recieve").replaceAll("%guild%", user.getGuild()));
                                                } else user.message(lang.getString("guild.ally.not-recieved"));
                                            } else user.message(lang.getString("guild.ally.already-allied"));
                                        } else user.message(lang.getString("guild.ally.guild-not-exists"));
                                    } else user.message(lang.getString("guild.ally.leader"));
                                } else user.message(lang.getString("has-not-guild"));
                            } else if (args[1].equalsIgnoreCase("decline")) {
                                if (user.hasGuild()) {
                                    if (user.isLeader()) {
                                        final String tag = args[2].toUpperCase();
                                        if (new MySQLUtils().guildTagExists(tag)) {
                                            Guild guild = new Guild(user.getGuild());
                                            if (!(guild.isAllied(tag))) {
                                                if (guild.recievedAllyInvite(tag)) {
                                                    guild.declineAlly(tag);
                                                    user.message(lang.getString("guild.ally.declined").replaceAll("%guild%", tag));
                                                } else user.message(lang.getString("guild.ally.not-recieved"));
                                            } else user.message(lang.getString("guild.ally.already-allied"));
                                        } else user.message(lang.getString("guild.ally.guild-not-exists"));
                                    } else user.message(lang.getString("guild.ally.leader"));
                                } else user.message(lang.getString("has-not-guild"));
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                if (user.hasGuild()) {
                                    if (user.isLeader()) {
                                        final String tag = args[2].toUpperCase();
                                        if (new MySQLUtils().guildTagExists(tag)) {
                                            Guild guild = new Guild(user.getGuild());
                                            if (guild.isAllied(tag)) {
                                                guild.removeAlly(tag);
                                                guild.removeAllySide(tag);
                                                user.message(lang.getString("guild.ally.remove.removed-ally").replaceAll("%guild%", tag));
                                                if (lang.getBoolean("guild.ally.remove.broadcast.enable")) Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("guild.ally.remove.broadcast.message").replaceAll("%remover%", user.getGuild()).replaceAll("%ally%", tag)));
                                            } else user.message(lang.getString("guild.ally.not-allied").replaceAll("%guild%", tag));
                                        } else user.message(lang.getString("guild.ally.guild-not-exists"));
                                    } else user.message(lang.getString("guild.ally.leader"));
                                } else user.message(lang.getString("has-not-guild"));
                            } else user.message(lang.getString("wrong-syntax"));
                        } else user.message(lang.getString("wrong-syntax"));
                    }

                    else user.message(lang.getString("wrong-syntax"));
                }
            } else {
                sender.sendMessage(lang.getString("console-execute"));
            }
        }
        return false;
    }

}
