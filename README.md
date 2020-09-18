# Simple Sound Muffler

This is a Minecraft mod for the Fabric Modloader, currently compatible with 1.16.2 and 1.16.3

The mod adds a single item and block to the game - the Sound Muffler. When placed, this block mutes all noises that originate within 3 blocks. 

![screenshot](https://i.imgur.com/KOVbVkZ.png)

The recipe is one note block surrounded by 8 wool (any color)

![recipe](https://i.imgur.com/VExX1rl.png)

The block can also be right-clicked to open up a UI where you can configure it further. 

![gui](https://i.imgur.com/QMEcgwM.png)


## Settings

**DENY mode** - do not muffle any sounds except for the selected ones

**ALLOW mode** - muffle all sounds except for the selected ones

**Muffle %** - how much the sounds should be muffled by. 100% = muted, 0% = no effect

**Radius** - radius to muffle sounds in

## Dependencies
 
- [fabric modloader](https://fabricmc.net/use/)
- [fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [fabric-language-kotlin](https://github.com/FabricMC/fabric-language-kotlin) >=1.3.60, <=1.4.0


## Demo Video

https://www.youtube.com/watch?v=mWYyhK7ld-s

## TODO

- [ ] improve gui
- [ ] optimize more
- [ ] add sound by manually entering ID
