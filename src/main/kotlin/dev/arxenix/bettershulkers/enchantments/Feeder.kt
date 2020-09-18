package dev.arxenix.bettershulkers.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.tag.BlockTags


class Feeder(weight: Rarity, type: EnchantmentTarget, slotTypes: Array<EquipmentSlot>) :
    Enchantment(weight, type, slotTypes) {
    override fun getMinPower(level: Int): Int {
        return 0
    }

    override fun getMaxPower(level: Int): Int {
        return 100000
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun canAccept(other: Enchantment): Boolean {
        return super.canAccept(other)
    }

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        println("checking item for feeder enchant")
        // TODO - make sure the shulkers are not stacked for compatibility with other mods
        val item = stack.item
        return item is BlockItem && item.block.isIn(BlockTags.SHULKER_BOXES)
    }
}