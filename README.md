# PropertyGuilds
## Guild system for Minecraft 1.13.2 server
![PropertyGuilds](https://i.imgur.com/QWu1DYg.png)
#
***Dependencies***: [NameManager (https://www.spigotmc.org/resources/namemanager.5147/)]
#
**Permissions**:
* propertyguilds.default - default plugin access
* propertyguilds.reload - access to plugin reload
#
**Commands**:
* */guilds* - Guilds help
* */guilds items* - Opens gui with items needed to create guild
* */guilds create [tag] [name]* - Creates guild
* */guilds invite [nick]* - Invites player to guild
* */guilds accept [tag]* - Accepts guild invitation
* */guilds decline [tag]* - Declines guild invitation
* */guilds kick [nick]* - Kicks player from guild
* */guilds ally invite [tag]* - Sends ally invitation to guild
* */guilds ally accept [tag]* - Accepts ally invitation
* */guilds ally decline [tag]* - Declines ally invitation
* */guilds ally remove [tag]* - Removes guild's ally
* */guilds leave* - Leaves guild
* */guilds delete [tag]* - Deletes guild
* */guilds info [tag]* - Guild's info ([tag] isn't necessarily)
* */guilds base* - Teleports to guild's base
* */guilds chat* - Toggles between global and guild chat
* */guilds top* - Shows top 10 guilds
* */guilds reload* - Reloads plugin
#
**Configuration file (config.yml)**
```
# PropertyGuilds plugin
# version: 0.1
# core: 1.13.2
# © krzysek (deew)
# Permissions: propertyguilds.default, propertyguilds.reload
# MySQL configuration
#  host - MySQL server address
#  port - MySQL server port
#  user - MySQL server user
#  password - MySQL server password
#  database - the database must be create before plugin installation
mysql:
  host: 'localhost'
  port: '3306'
  user: 'root'
  password: '123'
  database: 'PropertyGuilds_plugin'
#
chat:
  # Enable chat formatting (color messages - propertyguilds.chatcolor)
  enable: true
  # Chat format
  # %guild-tag%, %guild-name%, %guild-points%, %name%, %kills%, %deaths%, %points%, %world%, %message%
  format: '&8[&c&f%guild-tag%&8] &8[&6%points%&8] &r%name% &6> &r%message%'
guild-chat:
  # Guild chat format
  # %guild-tag%, %guild-name%, %guild-points%, %name%, %kills%, %deaths%, %points%, %world%, %message%
  format: '&c&l%guild-tag% &r%name% &6> &r&o%message%'
create-require-items:
  # Need items for creating guild
  enable: true
  # Gui size
  gui-size: 9
  # Items needed for creating guild (creating-require-items: true)
  list:
    - '16@DIAMOND'
    - '32@OBSIDIAN'
guild:
  # Tag lengths
  tag:
    # Min tag length
    min: 3
    # Max tag length
    max: 6
  # Name lengths
  name:
    # Min name length
    min: 3
    # Max name length
    max: 16
  # Property
  property:
    # Property radius
    radius: 10
    # World's spawn protection radius
    spawn-radius: 500
  # Points
  points:
    # Amount of guild points that guild starts with
    default: 0
  # Actions after successfully creating guild
  create:
    # Broadcast opts
    broadcast:
      # Send broadcast message
      enable: true
    # Private opts
    private:
      # Send private message
      enable: false
  # Actions after successfully deleting guild
  delete:
    # Broadcast opts
    broadcast:
      # Send broadcast message
      enable: true
    # Private opts
    private:
      # Send private message
      enable: false
  # Blocks options
  blocks:
    # Chests
    chests:
      # Allow chests opening
      allow-open: false
  # Cooldowns
  cooldowns:
    # Base teleportation cooldown
    base-teleport: 5
  # Damage
  damage:
    # Allow to deal damage between guild members
    friendly-fire:
      enable: false
    # Allow to deal damage between alliances
    alliances:
      enable: false
# Points
points:
  # Points for kill
  kill:
    # Points for killing player without guild
    player-without-guild: 5
    # Points for killing player with guild
    player-with-guild: 10
    # Points for kill that goes to player's guild [is he has]
    to-guild: 5
  # Points for death
  death:
    # Points for death (player without guild)
    player-without-guild: -5
    # Points for death (player with guild)
    player-with-guild: -10
    # Points for death that goes to player's guild [if he has]
    to-guild: -5
```
#
**Configuration file (lang.yml)**
```
# PropertyGuilds plugin
# version: 0.1
# core: 1.13.2
# © krzysek (deew)
# Permissions: propertyguilds.default, propertyguilds.reload
console-execute: 'You cannot execute this command from console state'
no-permission: '&cYou do not have access to this command'
wrong-syntax: '&cWrong syntax ! &f/guilds'
plugin-reload: '&aPlugin has been reloaded'
guilds-help:
  - '&e/guilds &7- &fGuilds help'
  - '&e/guilds items &7- &fItems needed for creating guild'
  - '&e/guilds create [tag] [name] &7- &fCreate guild'
  - '&e/guilds invite [nick] &7- &fInvite player to guild'
  - '&e/guilds accept [tag] &7- &fAccept guild invite'
  - '&e/guilds decline [tag] &7- &fDecline guild invite'
  - '&e/guilds ally invite [tag] &7- &fSent ally invite to guild'
  - '&e/guilds ally accept [tag] &7- &fAccept guild ally invite'
  - '&e/guilds ally decline [tag] &7- &fDecline guild ally invite'
  - '&e/guilds ally remove [tag] &7- &fRemove guild ally'
  - '&e/guilds leave &7- &fLeave guild'
  - '&e/guilds kick [nick] &7- &fKick guild member'
  - '&e/guilds delete [tag] &7- &fDelete guild'
  - '&e/guilds info [tag] &7- &fGuild info'
  - '&e/guilds base &7- &fTeleport to guild base'
  - '&e/guilds chat &7- &fSwitch between global and guild chat'
  - '&e/guilds top &7- &fTop 10 guilds'
  - '&e/guilds reload &7- &fReload plugin'
none-create-require-items: '&cYou do not need any items for creating guild'
create-require-items-gui-name: '&a&lRequired items'
has-guild: '&cYou are already in guild'
has-not-guild: '&cYou do not belong to any guild'
guild-not-exists: '&cGuild %tag% does not exists'
cannot-break: '&cThis block belongs to guild property'
guild:
  tag:
    length: '&cTag length must be &f>= %min% &cand &f<= %max%'
    exists: '&cGiven tag is already in-use'
  name:
    length: '&cName length must be &f>= %min% &cand &f<= %max%'
    exists: '&cGiven name is already in-use'
  property:
    exists: '&cYou can not create guild on property of another one'
    too-close: '&cYou are too close to property of another guild. You have to be at least &f%distance% &cblocks away from there'
    too-close-spawn: '&cYou are too close to spawn. You have to be at least &f%distance% &cblocks away from spawn'
    entered: '&cYou entered &7%tag% &cguild property'
    left: '&aYou left &7%tag% &aguild property'
  items:
    not-enough: '&cYou dont have enough items to create guild &f/guilds items'
  create:
    broadcast:
      message: '&8[&f%tag%&8] &f%name% &aguild has been created by &f%leader% &a!'
    private:
      enable: true
      message: '&8[&f%tag%&8] &f%name% &aguild has been created by &f%leader% &a!'
  delete:
    not-leader: '&cYou are not leader of this guild'
    broadcast:
      message: '&8[&f%tag%&8] &f%name% &cguild has been deleted by &f%leader% &c!'
    private:
      enable: true
      message: '&8[&f%tag%&8] &f%name% &cguild has been deleted by &f%leader% &c!'
  blocks:
    chests:
      cannot-open: '&cYou can not open chests that belongs to other guilds'
  teleport:
    base:
      remaining: '&aYou will be teleported to guild base in &f%time% seconds&a. Do not move'
      abort: '&cBase teleportation has been aborted'
      done: '&aYou have been teleported to your guild base'
  chat:
    global-toggle: '&aToggled to global chat'
    guild-toggle: '&aToggled to guild chat'
  invite:
    not-leader: '&cYou are not leader of your guild'
    offline: '&cThere is no online player with this nickname'
    has-guild: '&cThis player already has guild'
    already: '&cThis player is already invited'
    sent: '&aYou have invited &f%invited% &ato guild'
    recieved: '&aYou have been invited to &f%guild% &aguild'
    already-member: '&cYou are already in guild'
    guild-not-exists: '&cGuild with this tag does not exists'
    not-invited: '&cYou have not been invited to this guild'
    accepted: '&f%invited% &aaccepted invitation to guild'
    success-accepted: '&aYou have accepted &f%guild% &aguild invite'
    declined: '&f%invited% &cdeclined invitation to guild'
    success-declined: '&cYou have declined &f%guild% &cguild invite'
  leave:
    leader: '&cYou can not leave a guild while being its leader'
    left: '&cYou have left &f%guild% &cguild'
    broadcast:
      enable: true
      left: '&f%player% &cleft &f%guild% &cguild'
  kick:
    leader: '&cYou are not guild leader'
    has-not-guild: '&cThis player has not guild'
    not-belong: '&cThis player do not belong to your guild'
    leader-given: '&cYou can not kick leader'
    kicked: '&f%player% &chas been kicked from your guild'
    kick-recieve: '&cYou have been kicked from &f%guild% &cguild'
  damage:
    friendly-fire:
      message: '&cYou can not hurt guild members'
    alliances:
      message: '&cYou can not hurt alliances of your guild'
  ally:
    leader: '&cYou are not guild leader'
    guild-not-exists: '&cGuild with this tag does not exists'
    already-allied: '&cYour guild is already allied with this guild'
    already-sent: '&cYour guild have already sent ally invite to this guild'
    leader-not-online: '&cLeader of &f%guild% &cguild is not online'
    own-guild: '&cYou can not sent ally invite to your own guild'
    sent: '&aAlly invitation has been sent to &f%guild% &aguild'
    ally-recieve: '&f%guild% &ahas sent your guild ally invitation'
    not-recieved: '&cYour guild has not recieved ally invitation from this guild'
    declined: '&cYour guild has declined ally invitation from &f%guild% &cguild'
    accepted: '&aYour guild has accepted ally invitation form &f%guild% &aguild'
    accepted-recieve: '&f%guild% &aguild has accepted your guild ally invitation'
    not-allied: '&cYour guild has not allied with &f%guild% &cguild'
    remove:
      removed-ally: '&cYour guild has removed guild ally &f%guild%'
      broadcast:
        enable: true
        message: '&f%remover% &cguild has removed guild ally with &f%ally% &cguild'
guild-info:
  tag: 'Tag'
  name: 'Name'
  leader: 'Leader'
  members-list: 'Members list'
  alliances-list: 'Alliances list'
  members: 'Members'
  alliances: 'Alliances'
  points: 'Points'
```
#
***Have any new ideas ? Share it at https://github.com/loonypl/PropertyGuilds/issues***
