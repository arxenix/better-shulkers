package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.BetterShulkers;
import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract public class BlockItemMixin extends Item {
    public BlockItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        if (ShulkerUtilsKt.isShulker(this))
            return true;
        else return super.isEnchantable(stack);
    }

    @Override
    public int getEnchantability() {
        if (ShulkerUtilsKt.isShulker(this))
            return 1;
        else
            return 0;
    }


    @Inject(
            at=@At("RETURN"),
            method= "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;"
    )
    private void place(ItemPlacementContext ctx, CallbackInfoReturnable<ActionResult> cir) {
        if (cir.getReturnValue().isAccepted()) {
            if (ctx.getPlayer() != null) {
                int slot = 40; //offhand
                if (ctx.getPlayer().getActiveHand() == Hand.MAIN_HAND) {
                    slot = ctx.getPlayer().inventory.selectedSlot;
                }
                ShulkerUtilsKt.processItemConsume(ctx.getPlayer(), ctx.getStack(), slot);
            }
        }
    }
}
