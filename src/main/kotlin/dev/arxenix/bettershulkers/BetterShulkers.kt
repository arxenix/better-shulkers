package dev.arxenix.bettershulkers

import com.chocohead.mm.api.ClassTinkerers
import dev.arxenix.bettershulkers.enchantments.Feeder
import dev.arxenix.bettershulkers.mixin.ItemAccessor
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tag.BlockTags
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class BetterShulkers: ModInitializer {
    companion object {
        val LOGGER: Logger = LogManager.getLogger()
        val MODID = "bettershulkers"

        var SHULKER_ENCHANTMENT_TARGET: EnchantmentTarget? = null
        var FEEDER_ENCHANT: Feeder? = null


        fun isShulker(item: Item): Boolean {
            return item is BlockItem && item.block.isIn(BlockTags.SHULKER_BOXES)
        }
    }

    override fun onInitialize() {
        LOGGER.info("BetterShulkers - fabric mod initialized")

        SHULKER_ENCHANTMENT_TARGET = ClassTinkerers.getEnum(EnchantmentTarget::class.java, "SHULKER_BOX")
        //val target = ClassTinkerers.getEnum(EnchantmentTarget::class.java, "SHULKER_BOX")
        //LOGGER.info("Can enchant shulker? " + target.isAcceptableItem(Items.SHULKER_BOX)),
        FEEDER_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "feeder"),
            Feeder(Enchantment.Rarity.COMMON, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )

        val SHULKER_ITEM_GROUP = FabricItemGroupBuilder.create(
            Identifier(MODID, "item_group")
        )
            .icon { ItemStack(Items.SHULKER_BOX) }
            //.appendItems {
            //    shulkerItems.map { ItemStack(it) }
            //}
            .build()
        //SHULKER_ITEM_GROUP.texture = "shulkers.png"
        SHULKER_ITEM_GROUP.setEnchantments(SHULKER_ENCHANTMENT_TARGET)


        val shulkerItems = listOf(
            Items.SHULKER_BOX,
            Items.WHITE_SHULKER_BOX,
            Items.ORANGE_SHULKER_BOX,
            Items.MAGENTA_SHULKER_BOX,
            Items.LIGHT_BLUE_SHULKER_BOX,
            Items.YELLOW_SHULKER_BOX,
            Items.LIME_SHULKER_BOX,
            Items.PINK_SHULKER_BOX,
            Items.GRAY_SHULKER_BOX,
            Items.LIGHT_GRAY_SHULKER_BOX,
            Items.CYAN_SHULKER_BOX,
            Items.PURPLE_SHULKER_BOX,
            Items.BLUE_SHULKER_BOX,
            Items.BROWN_SHULKER_BOX,
            Items.GREEN_SHULKER_BOX,
            Items.RED_SHULKER_BOX,
            Items.BLACK_SHULKER_BOX
        )

        for (item in shulkerItems) {
            (item as ItemAccessor).setGroup(SHULKER_ITEM_GROUP)
        }

        /*
        val newShulkerLT = LootTable.builder().pool(
            BlockLootTableGenerator.addSurvivesExplosionCondition(
                drop,
                LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(
                    ItemEntry.builder(drop)
                        .apply(
                            CopyNameLootFunction.builder(CopyNameLootFunction.Source.BLOCK_ENTITY)
                        )
                        .apply(
                            CopyNbtLootFunction.builder(CopyNbtLootFunction.Source.BLOCK_ENTITY)
                                .withOperation("Lock", "BlockEntityTag.Lock")
                                .withOperation("LootTable", "BlockEntityTag.LootTable")
                                .withOperation("LootTableSeed", "BlockEntityTag.LootTableSeed")
                        )
                        .apply(
                            SetContentsLootFunction.builder().withEntry(DynamicEntry.builder(ShulkerBoxBlock.CONTENTS))
                        )
                )
            )
        )
        Registry.register(
            Registry.LOOT_FUNCTION_TYPE,
            Identifier(MODID, "copy_enchantments")
        )
         */
    }
}





