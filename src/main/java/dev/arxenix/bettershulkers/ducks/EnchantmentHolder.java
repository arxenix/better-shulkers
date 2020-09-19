package dev.arxenix.bettershulkers.ducks;

import net.minecraft.nbt.ListTag;

public interface EnchantmentHolder {
    ListTag getEnchantments();
    void setEnchantments(ListTag tag);
}
