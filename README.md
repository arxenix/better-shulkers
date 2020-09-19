# Better Shulkers

This is a Minecraft mod for the Fabric Modloader, currently compatible with 1.16.2 and 1.16.3

It adds several new features and enchantments to shulkerboxes to make them even more useful.

Ideas from Xisumavoid's video https://www.youtube.com/watch?v=FMu8T8KriQY

# Current Features

3 added enchantments


### Enlarge

3 levels (I, II, III). Each level increases the size of the shulker box by 1 row.


### Restock

If you place a block, it will attempt to give you back that block from a Restock shulker in your inventory.


### Vacuum

If a shulker with the vacuum enchant is in your inventory, then whenever you pick up an item, it will attempt to put it into the shulker if there is a stack of the same type in the shulker.


# Planned Features / Improvements

- Make Restock work with consumable items (e.g. foods, minecarts, boats) 
- the default shulker tooltip only shows items in the first 3 rows, so fix that
- possibly incorporate nice tooltips similar to ShulkerBoxTooltip, or find a way to make it compatible
- add Backpack enchant
- add Ender Power enchant
- customize shulker box GUI to show enchants


# Known Bugs
- If you make use of Loot Tables for shulker boxes, they will not work, as the mod currently completely overwrites the Shulker box drop function
- Probably incompatible with any other mod that touches shulker boxes. Let me know which ones and I'll attempt to make it compatible
- Pick block in creative mode does not copy the shulker box enchantments


## Dependencies
 
- [fabric modloader](https://fabricmc.net/use/)
- [fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
- [fabric-language-kotlin](https://github.com/FabricMC/fabric-language-kotlin) >=1.3.60, <=1.4.0


## TODO (dev stuff)

- [ ] don't override the shulker box loot table - make it compatible by adding a custom LootFunction
- [ ] make the enchantment glow less intense, so you can see the shulker colors better
