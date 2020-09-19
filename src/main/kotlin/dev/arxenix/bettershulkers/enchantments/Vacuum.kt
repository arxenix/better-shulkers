package dev.arxenix.bettershulkers.enchantments

import dev.arxenix.bettershulkers.RESTOCK_ENCHANT
import dev.arxenix.bettershulkers.isShulker
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack


class Vacuum(weight: Rarity, type: EnchantmentTarget, slotTypes: Array<EquipmentSlot>) :
    Enchantment(weight, type, slotTypes) {
    override fun getMinPower(level: Int): Int {
        return 25
    }

    override fun getMaxPower(level: Int): Int {
        return 50
    }

    override fun getMaxLevel(): Int {
        return 1
    }

    override fun canAccept(other: Enchantment): Boolean {
        return super.canAccept(other) && other != RESTOCK_ENCHANT
    }

    override fun isAcceptableItem(stack: ItemStack): Boolean {
        return isShulker(stack)
    }
}