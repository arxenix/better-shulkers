package dev.arxenix.bettershulkers

import net.minecraft.block.Block
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.tag.BlockTags
import net.minecraft.util.collection.DefaultedList

val SHULKER_ITEMS = mutableSetOf<Item>(
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

fun isShulker(itemStack: ItemStack): Boolean {
    return isShulker(itemStack.item)
}

fun isShulker(item: Item): Boolean {
    return SHULKER_ITEMS.contains(item)
}

fun isShulker(block: Block): Boolean {
    return block.isIn(BlockTags.SHULKER_BOXES)
}

fun getShulkerInv(itemStack: ItemStack): DefaultedList<ItemStack> {
    // TODO - changeme when adding support for different shulker sizes
    var size = 27
    val tag = itemStack.getSubTag("BlockEntityTag")
    if (tag != null) {
        if (tag.contains("Size", 99)) {
            size = tag.getInt("Size")
        }

        val inv = DefaultedList.ofSize(size, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            Inventories.fromTag(tag, inv)
        }
        return inv
    }
    else return DefaultedList.ofSize(size, ItemStack.EMPTY)
}

fun setShulkerInv(itemStack: ItemStack, inv: DefaultedList<ItemStack>) {
    val tag = itemStack.getOrCreateSubTag("BlockEntityTag")
    Inventories.toTag(tag, inv)
}

fun hasShulkers(inv: Inventory): Boolean {
    return inv.containsAny(SHULKER_ITEMS)
}

fun getShulkers(inv: Inventory): List<Pair<Int, ItemStack>> {
    val shulkerIndexList = mutableListOf<Pair<Int, ItemStack>>()
    for (i in 0..inv.size()) {
        val stack = inv.getStack(i)
        if (isShulker(stack))
            shulkerIndexList.add(Pair(i, stack))
    }
    return shulkerIndexList
}

fun DefaultedList<ItemStack>.removeStack(slot: Int, amount: Int): ItemStack {
    return Inventories.splitStack(this, slot, amount)
}

fun canTransfer(from: ItemStack, to: ItemStack): Boolean {
    return if (to.item !== from.item) {
        false
    } /*else if (stack2.count + stack1.count > stack2.maxCount) {
        false
    } */else if (to.hasTag() xor from.hasTag()) {
        false
    } else {
        !to.hasTag() || to.tag == from.tag
    }
}

fun processItemConsume(player: PlayerEntity, stack: ItemStack, slot: Int) {
    // TODO - adapt for consuming multiple of the item at once?
    // TODO - make it work when the stack size is zero
    if (stack.isEmpty) return
    val shulkers = getShulkers(player.inventory)
    val restockers = shulkers.filter { EnchantmentHelper.getLevel(RESTOCK_ENCHANT, it.second) > 0 }
    for (restocker in restockers) {
        val shulker = restocker.second
        val stacks = getShulkerInv(shulker)
        for (shulkerStack in stacks) {
            if (!shulkerStack.isEmpty) {
                if (canTransfer(shulkerStack, stack)) {
                    shulkerStack.decrement(1)
                    stack.increment(1)
                    setShulkerInv(shulker, stacks)
                    return
                }
            }
        }
    }
}