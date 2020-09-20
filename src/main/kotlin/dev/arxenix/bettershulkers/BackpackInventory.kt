package dev.arxenix.bettershulkers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag

class BackpackInventory(var items: ListTag, val rows: Int) : Inventory {
    override fun markDirty() {}

    override fun clear() {
        items.clear()
    }

    override fun setStack(slot: Int, stack: ItemStack) {
        for (item in items.iterator()) {
            if ((item as? CompoundTag)?.getInt("Slot") == slot) {
                stack.toTag(item)
                return
            }
        }
        val item = getInventorySlot(items, slot)
        if (item != null) stack.toTag(item) else {
            // If no item already there
            val newTag = CompoundTag()
            newTag.putInt("Slot", slot)
            stack.toTag(newTag)
            items.add(newTag)
        }
    }

    override fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack {
        val item = getInventorySlot(items, slot)!!
        item.putInt("Count", item.getInt("Count") - amount)
        if (item.getInt("Count") <= 0) items.remove(item)
        return ItemStack.fromTag(item)
    }

    override fun removeStack(slot: Int): ItemStack {
        val item = getInventorySlot(items, slot)!!
        items.remove(item)
        return ItemStack.fromTag(item)
    }

    override fun getStack(slot: Int): ItemStack {
        return ItemStack.fromTag(getInventorySlot(items, slot))
    }

    override fun canPlayerUse(player: PlayerEntity?): Boolean {
        return true
    }

    override fun size(): Int {
        return rows*9
    }

    override fun isValid(slot: Int, stack: ItemStack): Boolean {
        return !isShulker(stack)
    }
}