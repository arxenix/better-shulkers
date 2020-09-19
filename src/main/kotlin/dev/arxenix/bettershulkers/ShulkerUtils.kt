package dev.arxenix.bettershulkers

import dev.arxenix.bettershulkers.ducks.EnchantmentHolder
import dev.arxenix.bettershulkers.mixin.ItemStackAccessor
import net.minecraft.block.Block
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.ShulkerBoxBlockEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.tag.BlockTags
import net.minecraft.util.collection.DefaultedList
import kotlin.math.min

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

fun isShulker(blockEntity: BlockEntity): Boolean {
    return blockEntity is ShulkerBoxBlockEntity
}

fun isShulker(itemStack: ItemStack): Boolean {
    return isShulker(itemStack.item)
}

fun isShulker(item: Item): Boolean {
    return SHULKER_ITEMS.contains(item)
}

fun isShulker(block: Block): Boolean {
    return block.isIn(BlockTags.SHULKER_BOXES)
}

fun getShulkerSizeFromEnchantmentsTag(tag: ListTag): Int {
    return getShulkerSizeFromEnchantmentsMap(EnchantmentHelper.fromTag(tag))
}

fun getShulkerSizeFromEnchantmentsMap(enchants: Map<Enchantment, Int>): Int {
    return 9 * (min(3, enchants.getOrDefault(ENLARGE_ENCHANT as Enchantment, 0)) + 3)
}

fun getShulkerSizeFromBlockEntityTag(tag: CompoundTag): Int {
    return if (tag.contains("Enchantments", 9)) {
        getShulkerSizeFromEnchantmentsTag(tag.getList("Enchantments", 10))
    }
    else 27
}

fun getShulkerInv(itemStack: ItemStack): DefaultedList<ItemStack> {
    // TODO - changeme when adding support for different shulker sizes
    val tag = itemStack.getSubTag("BlockEntityTag")
    if (tag != null) {
        val size = getShulkerSizeFromBlockEntityTag(tag)
        val inv = DefaultedList.ofSize(size, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            Inventories.fromTag(tag, inv)
        }
        return inv
    }
    else return DefaultedList.ofSize(27, ItemStack.EMPTY)
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

fun itemStackFromBlockEntity(sbe: ShulkerBoxBlockEntity): ItemStack {
    val itemStack = ShulkerBoxBlock.getItemStack(sbe.color)
    val compoundTag = sbe.serializeInventory(CompoundTag())
    val enchantmentData = (sbe as EnchantmentHolder).enchantments
    if (enchantmentData != null) {
        //System.out.println("we have enchantment data!");
        itemStack.putSubTag("Enchantments", enchantmentData)
        compoundTag.put("Enchantments", enchantmentData)
    }
    if (!compoundTag.isEmpty) {
        itemStack.putSubTag("BlockEntityTag", compoundTag)
    }
    if (sbe.hasCustomName()) {
        itemStack.setCustomName(sbe.customName)
    }
    return itemStack
}

fun canTransfer(from: ItemStack, to: ItemStack): Boolean {
    val toRealTag = to.tag
    val toHasTag = toRealTag != null && !toRealTag.isEmpty
    return if ((to as ItemStackAccessor).realItem !== from.item) {
        false
    } /*else if (stack2.count + stack1.count > stack2.maxCount) {
        false
    } */else if (toHasTag xor from.hasTag()) {
        false
    } else {
        !toHasTag || to.tag == from.tag
    }
}

fun processItemConsume(player: PlayerEntity, stack: ItemStack, slot: Int) {
    // TODO - adapt for consuming multiple of the item at once?
    // TODO - make it work when the stack size is zero
    //if (stack.isEmpty) return
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

fun canMerge(from: ItemStack, to: ItemStack): Boolean {
    return if (from.item !== to.item) {
        false
    } else if (to.count >= to.maxCount) {
        false
    } else if (to.hasTag() xor from.hasTag()) {
        false
    } else {
        !from.hasTag() || from.tag == to.tag
    }
}

fun tryMerge(from: ItemStack, to: ItemStack): Boolean {
    if (canMerge(from, to)) {
        // merge
        val maxTransferable = min(min(to.maxCount, 64) - to.count, from.count)
        from.decrement(maxTransferable)
        to.increment(maxTransferable)
        return true
    }
    return false
}

fun processItemGet(player: PlayerEntity, stack: ItemStack): Boolean {
    //println("got item $stack ${stack.tag.toString()}")
    val shulkers = getShulkers(player.inventory)
    val vacuums = shulkers.filter { EnchantmentHelper.getLevel(VACUUM_ENCHANT, it.second) > 0 }
    //println("have vacuums: ${vacuums.size}")
    var didVacuum = false
    for (vacuum in vacuums) {
        if (stack.isEmpty) return didVacuum
        val shulker = vacuum.second
        val stacks = getShulkerInv(shulker)

        var updatedShulker = false
        for (shulkerStack in stacks) {
            if (!shulkerStack.isEmpty) {
                if (tryMerge(stack, shulkerStack)) {
                    //println("merge success!")
                    updatedShulker = true
                    if (stack.isEmpty)
                        break
                }
            }
        }
        if (updatedShulker) {
            //println("updated shulker!")
            didVacuum = true
            setShulkerInv(shulker, stacks)
        }
    }
    return didVacuum
}