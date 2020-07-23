# SPGT-1.16_HyperWarp
Minecraft 1.16 Spigot plugin. An advanced warp plugin which allows you to set warps between worlds with the ability to assign permissions for each individual warp.

Install by dropping jar file into plugins folder of your server.

## Commands:

Arguments in <triangle brackets> are required, [square brackets] are optional, and without brackets are explicitly that text.

#### `/setwarp <warp name> [x] [y] [z] [pitch] [yaw] [world] [permissionNode] [description... ]`

Set a new warp destination at (x,y,z) rotated (pitch,yaw) in world.
Players without access to permissionNode cannot warp to a given location.
(write as 'none' to let anyone visit). 

Aliases: `/sw`

#### `/delwarp <warp name>`

Remove the specified warp.

Aliases: `/dw`

#### `/warp <warp name>`

Warp to a specific location if the player has the permission.

Aliases: `/w`

#### `/listwarps`

Get a list of all the warps.

Aliases: `/lw`, `/warps`

#### `/getwarp <warp name>`

Get specific information about a warp.

Aliases: `/gw`, `/warpinfo`, `/wi`

#### `/editwarp <warp name> <change type> <new value>`

Change various info after a warp has been created.

Sub commands:
```
/editwarp <warp name> rename <new name>
/editwarp <warp name> position <x> <y> <z>
/editwarp <warp name> rotation <pitch> <yaw>
/editwarp <warp name> world <world>
/editwarp <warp name> permission <permission>
/editwarp <warp name> description <description>
```

Aliases: `/ew`
