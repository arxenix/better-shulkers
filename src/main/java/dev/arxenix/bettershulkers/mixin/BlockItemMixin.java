package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.BetterShulkers;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockItem.class)
abstract public class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        if (BetterShulkers.Companion.isShulker(this))
            return true;
        else return super.isEnchantable(stack);
    }

    @Override
    public int getEnchantability() {
        if (BetterShulkers.Companion.isShulker(this))
            return 1;
        else
            return 0;
    }
}
