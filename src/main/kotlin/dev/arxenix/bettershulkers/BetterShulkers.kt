package dev.arxenix.bettershulkers

import com.chocohead.mm.api.ClassTinkerers
import dev.arxenix.bettershulkers.enchantments.Feeder
import net.fabricmc.api.ModInitializer
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.data.server.BlockLootTableGenerator
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.loot.ConstantLootTableRange
import net.minecraft.loot.LootPool
import net.minecraft.loot.LootTable
import net.minecraft.loot.entry.DynamicEntry
import net.minecraft.loot.entry.ItemEntry
import net.minecraft.loot.function.CopyNameLootFunction
import net.minecraft.loot.function.CopyNbtLootFunction
import net.minecraft.loot.function.SetContentsLootFunction
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
        //LOGGER.info("Can enchant shulker? " + target.isAcceptableItem(Items.SHULKER_BOX));
        FEEDER_ENCHANT = Registry.register(
            Registry.ENCHANTMENT,
            Identifier(MODID, "feeder"),
            Feeder(Enchantment.Rarity.COMMON, SHULKER_ENCHANTMENT_TARGET!!, arrayOf())
        )
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





