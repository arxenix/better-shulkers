package dev.arxenix.bettershulkers.enchantments

import dev.arxenix.bettershulkers.isShulker
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack


class Enlarge(weight: Rarity, type: EnchantmentTarget, slotTypes: Array<EquipmentSlot>) :
    Enchantment(weight, type, slotTypes) {
    override fun getMinPower(level: Int): Int {
        return 0
    }

    override fun getMaxPower(level: Int): Int {
        return 100000
    }

    override fun getMaxLevel(): Int {
        return 3
    }

    override fun canAccept(other: Enchantment): Boolean {
        return super.canAccept(other)
    }

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return isShulker(stack)
    }
}