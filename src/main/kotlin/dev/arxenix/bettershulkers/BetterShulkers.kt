package dev.arxenix.bettershulkers

import com.chocohead.mm.api.ClassTinkerers
import dev.arxenix.bettershulkers.enchantments.Backpack
import dev.arxenix.bettershulkers.enchantments.Enlarge
import dev.arxenix.bettershulkers.enchantments.Restock
import dev.arxenix.bettershulkers.enchantments.Vacuum
import dev.arxenix.bettershulkers.mixin.ItemAccessor
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.event.Event
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val LOGGER: Logger = LogManager.getLogger()
const val MODID = "bettershulkers"

var SHULKER_ENCHANTMENT_TARGET: EnchantmentTarget? = null
var RESTOCK_ENCHANT: Restock? = null
var ENLARGE_ENCHANT: Enlarge? = null
var VACUUM_ENCHANT: Vacuum? = null
var BACKPACK_ENCHANT: Backpack? = null
var SHULKER_ITEM_GROUP: ItemGroup? = null

class BetterShulkers: ModInitializer {
    override fun onInitialize() {
        LOGGER.info("BetterShulkers - fabric mod initialized")

        SHULKER_ENCHANTMENT_TARGET = ClassTinkerers.getEnum(EnchantmentTarget::class.java, "SHULKER_BOX")
        //val target = ClassTinkerers.getEnum(EnchantmentTarget::class.java, "SHULKER_BOX")
        //LOGGER.info("Can enchant shulker? " + target.isAcceptableItem(Items.SHULKER_BOX)),
        RESTOCK_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "restock"),
            Restock(Enchantment.Rarity.VERY_RARE, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )

        ENLARGE_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "enlarge"),
            Enlarge(Enchantment.Rarity.RARE, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )

        VACUUM_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "vacuum"),
            Vacuum(Enchantment.Rarity.VERY_RARE, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )

        BACKPACK_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "backpack"),
            Backpack(Enchantment.Rarity.VERY_RARE, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )

        SHULKER_ITEM_GROUP = FabricItemGroupBuilder.create(
            Identifier(MODID, "item_group")
        )
            .icon { ItemStack(Items.SHULKER_BOX) }
            //.appendItems {
            //    shulkerItems.map { ItemStack(it) }
            //}
            .build()
        SHULKER_ITEM_GROUP!!.setEnchantments(SHULKER_ENCHANTMENT_TARGET)

        for (item in SHULKER_ITEMS) {
            (item as ItemAccessor).setGroup(SHULKER_ITEM_GROUP)
        }

        // Backpack player tick event
        // TODO: is this START_CLIENT_TICK, or END?
        ClientTickEvents.START_CLIENT_TICK.register(checkBackpackTick)
    }
}




