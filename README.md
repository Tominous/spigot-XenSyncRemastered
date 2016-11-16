# XenSync Remastered
XenSync Remastered is a Spigot plugin to sync a rank with XenForo.

## Installation
**This plugin requires [Vault](https://dev.bukkit.org/bukkit-plugins/vault/) to be installed on your server.**

Just drop the jar into your `plugins` folder, reload, edit configuration, reload, go ham.

## Configuration
### MySQL
Just put the database link (IP) with the database name in the `uri` portion and the username and password in the corresponding sections.

### Player Identifier
`field`: The custom user field that contains the user's Minecraft username. [Click here to see how to create custom user fields in Xenforo](https://xenforo.com/help/custom-user-fields/)

`name-system`: Use UUIDs or the player name. `true` for names, `false` for UUID.

`use-profile-fields`: Whether or not to use profile fields, leave `true`.

`database-table`: The default table to use for checking against. Leave default unless you're using your own database.

`table-xenid`: The user id field to grab the user's id.

`table-playerid`: The field to get the Minecraft info from.

### Member Feature
`require-valid-email`: Require the user to be verified before syncing.

### Other
`sync-task-delay`: The amount of minutes to wait before syncing all online players again.
`debug`: Just outputs some more info to the console.

## Data Files
### groupconversions
The file will be blank. It should be like `minecraft_group:XenForo_group`, so it could look something like...
```
donator:Donator
donatorplus:DonatorPlus
subscribed:Premium
lord:Lord
mod:Moderator
```

### groupexceptions
A list of Minecraft groups to not sync up. Could look like this...
```
administration
banned
```

### playerexceptions
A list of Minecraft usernames to not sync up. Example usage...
```
notch
yourmomma
```

## Donate
[![Donate](https://az743702.vo.msecnd.net/cdn/kofi1.png?v=f)](https://ko-fi.com/636QU7F12V5F)

## Development Video
Here you hungry hippos you. :neckbeard:

[![Coding Off](https://img.youtube.com/vi/cnWvT6ErCnA/0.jpg)](https://www.youtube.com/watch?v=cnWvT6ErCnA)
