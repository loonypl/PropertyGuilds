package amao.krzysek.propertyguilds.utils.config;

import amao.krzysek.propertyguilds.PropertyGuilds;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MessageUtils {

    protected LinkedHashMap<String, Object> messages = PropertyGuilds.getInstance().getConfigMessages();
    protected FileConfiguration lang = PropertyGuilds.getInstance().getLang();

    public MessageUtils() {}

    @SuppressWarnings("RedundantCast")
    public void loadMessages() {
        put("CONSOLE-EXECUTE", (String) get("console-execute"));
        put("NO-PERMISSION", (String) get("no-permission"));
        put("WRONG-SYNTAX", (String) get("wrong-syntax"));
        put("PLUGIN-RELOAD", (String) get("plugin-reload"));
        put("GUILDS-HELP", (ArrayList<String>) get("guilds-help"));
        put("NONE-CREATE-REQUIRE-ITEMS", (String) get("none-create-require-items"));
        put("CREATE-REQUIRE-ITEMS-GUI-NAME", (String) get("create-require-items-gui-name"));
        put("HAS-GUILD", (String) get("has-guild"));
        put("HAS-NOT-GUILD", (String) get("has-not-guild"));
        put("GUILD-NOT-EXISTS", (String) get("guild-not-exists"));
        put("CANNOT-BREAK", (String) get("cannot-break"));
        put("GUILD-TAG-LENGTH", (String) get("guild.tag.length"));
        put("GUILD-TAG-EXISTS", (String) get("guild.tag.exists"));
        put("GUILD-NAME-LENGTH", (String) get("guild.name.length"));
        put("GUILD-NAME-EXISTS", (String) get("guild.name.exists"));
        put("GUILD-PROPERTY-EXISTS", (String) get("guild.property.exists"));
        put("GUILD-PROPERTY-TOO-CLOSE", (String) get("guild.property.too-close"));
        put("GUILD-PROPERTY-TOO-CLOSE-SPAWN", (String) get("guild.property.too-close-spawn"));
        put("GUILD-PROPERTY-ENTERED", (String) get("guild.property.entered"));
        put("GUILD-PROPERTY-LEFT", (String) get("guild.property.left"));
        put("NOT-ENOUGH", (String) get("guild.items.not-enough"));
        put("GUILD-CREATE-BROADCAST-MESSAGE", (String) get("guild.create.broadcast.message"));
        put("GUILD-CREATE-PRIVATE-ENABLE", (boolean) get("guild.create.private.enable"));
        put("GUILD-CREATE-PRIVATE-MESSAGE", (String) get("guild.create.private.message"));
        put("GUILD-DELETE-NOT-LEADER", (String) get("guild.delete.not-leader"));
        put("GUILD-DELETE-BROADCAST-MESSAGE", (String) get("guild.delete.broadcast.message"));
        put("GUILD-DELETE-PRIVATE-ENABLE", (boolean) get("guild.delete.private.enable"));
        put("GUILD-DELETE-PRIVATE-MESSAGE", (String) get("guild.delete.private.message"));
        put("GUILD-BLOCKS-CHESTS-CANNOT-OPEN", (String) get("guild.blocks.chests.cannot-open"));
        put("GUILD-TELEPORT-BASE-REMAINING", (String) get("guild.teleport.base.remaining"));
        put("GUILD-TELEPORT-BASE-ABORT", (String) get("guild.teleport.base.abort"));
        put("GUILD-TELEPORT-BASE-DONE", (String) get("guild.teleport.base.done"));
        put("GUILD-TELEPORT-BASE-ALREADY", (String) get("guild.teleport.base.already"));
        put("GUILD-CHAT-GLOBAL-TOGGLE", (String) get("guild.chat.global-toggle"));
        put("GUILD-CHAT-GUILD-TOGGLE", (String) get("guild.chat.guild-toggle"));
        put("GUILD-CHAT-ALLY-TOGGLE", (String) get("guild.chat.ally-toggle"));
        put("GUILD-INVITE-NOT-LEADER", (String) get("guild.invite.not-leader"));
        put("GUILD-INVITE-OFFLINE", (String) get("guild.invite.offline"));
        put("GUILD-INVITE-HAS-GUILD", (String) get("guild.invite.has-guild"));
        put("GUILD-INVITE-ALREADY", (String) get("guild.invite.already"));
        put("GUILD-INVITE-SENT", (String) get("guild.invite.sent"));
        put("GUILD-INVITE-RECIEVED", (String) get("guild.invite.recieved"));
        put("GUILD-INVITE-ALREADY-MEMBER", (String) get("guild.invite.already-member"));
        put("GUILD-INVITE-GUILD-NOT-EXISTS", (String) get("guild.invite.guild-not-exists"));
        put("GUILD-INVITE-NOT-INVITED", (String) get("guild.invite.not-invited"));
        put("GUILD-INVITE-ACCEPTED", (String) get("guild.invite.accepted"));
        put("GUILD-INVITE-SUCCESS-ACCEPTED", (String) get("guild.invite.success-accepted"));
        put("GUILD-INVITE-DECLINED", (String) get("guild.invite.declined"));
        put("GUILD-INVITE-SUCCESS-DECLINED", (String) get("guild.invite.success-declined"));
        put("GUILD-LEAVE-LEADER", (String) get("guild.leave.leader"));
        put("GUILD-LEAVE-LEFT", (String) get("guild.leave.left"));
        put("GUILD-LEAVE-BROADCAST-ENABLE", (boolean) get("guild.leave.broadcast.enable"));
        put("GUILD-LEAVE-BROADCAST-LEFT", (String) get("guild.leave.broadcast.left"));
        put("GUILD-KICK-LEADER", (String) get("guild.kick.leader"));
        put("GUILD-KICK-HAS-NOT-GUILD", (String) get("guild.kick.has-not-guild"));
        put("GUILD-KICK-NOT-BELONG", (String) get("guild.kick.not-belong"));
        put("GUILD-KICK-LEADER-GIVEN", (String) get("guild.kick.leader-given"));
        put("GUILD-KICK-KICKED", (String) get("guild.kick.kicked"));
        put("GUILD-KICK-KICK-RECIEVE", (String) get("guild.kick.kick-recieve"));
        put("GUILD-DAMAGE-FRIENDLY-FIRE-MESSAGE", (String) get("guild.damage.friendly-fire.message"));
        put("GUILD-DAMAGE-ALLIANCES-MESSAGE", (String) get("guild.damage.alliances.message"));
        put("GUILD-ALLY-LEADER", (String) get("guild.ally.leader"));
        put("GUILD-ALLY-GUILD-NOT-EXISTS", (String) get("guild.ally.guild-not-exists"));
        put("GUILD-ALLY-ALREADY-ALLIED", (String) get("guild.ally.already-allied"));
        put("GUILD-ALLY-ALREADY-SENT", (String) get("guild.ally.already-sent"));
        put("GUILD-ALLY-LEADER-NOT-ONLINE", (String) get("guild.ally.leader-not-online"));
        put("GUILD-ALLY-OWN-GUILD", (String) get("guild.ally.own-guild"));
        put("GUILD-ALLY-SENT", (String) get("guild.ally.sent"));
        put("GUILD-ALLY-ALLY-RECIEVE", (String) get("guild.ally.ally-recieve"));
        put("GUILD-ALLY-NOT-RECIEVED", (String) get("guild.ally.not-recieved"));
        put("GUILD-ALLY-DECLINED", (String) get("guild.ally.declined"));
        put("GUILD-ALLY-ACCEPTED", (String) get("guild.ally.accepted"));
        put("GUILD-ALLY-ACCEPTED-RECIEVED", (String) get("guild.ally.accepted-recieved"));
        put("GUILD-ALLY-NOT-ALLIED", (String) get("guild.ally.not-allied"));
        put("GUILD-ALLY-REMOVE-REMOVED-ALLY", (String) get("guild.ally.remove.removed-ally"));
        put("GUILD-ALLY-REMOVE-BROADCAST-ENABLE", (boolean) get("guild.ally.remove.broadcast.enable"));
        put("GUILD-ALLY-REMOVE-BROADCAST-MESSAGE", (String) get("guild.ally.remove.broadcast.message"));
        put("GUILD-INFO-TAG", (String) get("guild-info.tag"));
        put("GUILD-INFO-NAME", (String) get("guild-info.name"));
        put("GUILD-INFO-LEADER", (String) get("guild-info.leader"));
        put("GUILD-INFO-MEMBERS-LIST", (String) get("guild-info.members-list"));
        put("GUILD-INFO-ALLIANCES-LIST", (String) get("guild-info.alliances-list"));
        put("GUILD-INFO-MEMBERS", (String) get("guild-info.members"));
        put("GUILD-INFO-ALLIANCES", (String) get("guild-info.alliances"));
        put("GUILD-INFO-POINTS", (String) get("guild-info.points"));
    }

    protected void put(final String key, final Object value) {
        messages.put(key, value);
    }

    protected Object get(final String get) {
        return lang.get(get);
    }

    public Object getMessage(final String get) {
        return messages.get(get);
    }

}
