# ParkourRegion


This plugin provides parkour regions with start, checkpoints, fail detection, blacklisted blocks, finish commands/messages/sounds, and admin tools to manage them.


## Build
1. `mvn clean package`
2. Put `target/ParkourRegion-1.0.0.jar` into `plugins/`.


## Commands
- `/por pos1` - set position 1 (player location)
- `/por pos2` - set position 2
- `/por create <region>` - create region from pos1+pos2
- `/por remove <region>` - remove region
- `/por <region> setstart` - set start to current location
- `/por <region> setfinish` - set finish to current location
- `/por <region> addcheckpoint <x> <y> <z>` - add checkpoint
- `/por <region> listcheckpoints` - list
- `/por <region> removecheckpoint <index>` - remove by index
- `/por <region> clearcheckpoints` - clear
- `/por <region> addfinishcommand <command...>` - add finish command
- `/por <region> listfinishcommands` - list finish commands
- `/por <region> removefinishcommand <index>` - remove finish command
- `/por <region> editfinishcommand <index> <new...>` - edit
- `/por <region> clearfinishcommands` - clear finish commands
- `/por <region> setfaillimit <y>` - set fail Y
- `/por <region> removefaillimit` - remove fail Y
- `/por <region> blacklist add <MATERIAL>` - add blacklisted block
- `/por <region> blacklist remove <MATERIAL>` - remove blacklisted block
- `/por list` - list regions
- `/por <region> info` - show region info
- `/por <region> tp` - teleport to region start/test
- `/por <region> show` - show particle preview
- `/por <region> setcooldown <time>` - set finish cooldown (e.g. 10s, 5m)


## Permissions
- `parkourregion.admin` - full admin access (default OP)
