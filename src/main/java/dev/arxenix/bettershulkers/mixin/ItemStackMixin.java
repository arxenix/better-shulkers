package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.BetterShulkers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    // nullable
    @Shadow public abstract CompoundTag getSubTag(String key);

    // nullable
    @Shadow public abstract CompoundTag getTag();

    @Inject(
            method= "addEnchantment(Lnet/minecraft/enchantment/Enchantment;I)V",
            at=@At("TAIL")
    )
    public void addEnchantment(Enchantment enchantment, int level, CallbackInfo ci) {
        System.out.println("addEnchantment called");
        if (BetterShulkers.Companion.isShulker(getItem())) {
            System.out.println("adding BET to item");
            CompoundTag beTag = getSubTag("BlockEntityTag");
            if (beTag == null) beTag = new CompoundTag();
            beTag.put("Enchantments", this.getTag().get("Enchantments"));
        }
    }
}
