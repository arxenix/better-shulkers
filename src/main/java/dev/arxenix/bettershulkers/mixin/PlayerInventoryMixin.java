package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

    @Inject(
            at = @At("HEAD"),
            method = "insertStack(Lnet/minecraft/item/ItemStack;)Z",
            cancellable = true
    )
    public void insertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean didVacuum = ShulkerUtilsKt.processItemGet(player, stack);
        if (didVacuum) {
            cir.setReturnValue(true);
        }
    }
}
