package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    // sometimes the enchanted boxes are not created through an enchanting table
    // e.g. anvils
    // this injection updates the BET for these cases
    @Inject(
            method = "set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private static void set(Map<Enchantment, Integer> enchantments, ItemStack stack, CallbackInfo ci,
                            ListTag tag) {
        if (ShulkerUtilsKt.isShulker(stack)) {
            CompoundTag bet = stack.getOrCreateSubTag("BlockEntityTag");
            bet.put("Enchantments", tag);
        }
    }
}
