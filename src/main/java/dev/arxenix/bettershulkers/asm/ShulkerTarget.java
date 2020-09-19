package dev.arxenix.bettershulkers.asm;

import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import dev.arxenix.bettershulkers.mixin.EnchantmentTargetMixin;
import net.minecraft.item.Item;

public class ShulkerTarget extends EnchantmentTargetMixin {
    @Override
    public boolean isAcceptableItem(Item item) {
        //System.out.println("checking item for ShulkerTarget");
        //return item == Items.SHULKER_BOX;
        return ShulkerUtilsKt.isShulker(item);
    }
}