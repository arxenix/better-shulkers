package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ducks.EnchantmentHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements EnchantmentHolder {
    @Unique
    private ListTag enchantmentData = new ListTag();

    @Override
    public ListTag getEnchantments() {
        return enchantmentData;
    }

    @Inject(
            method= "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V",
            at=@At("TAIL")
    )
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        System.out.println("fromTag called");
        System.out.println(tag.toString());
        if (tag.contains("Enchantments", 9)) {
            System.out.println("has Enchantments!!");
            this.enchantmentData = tag.getList("Enchantments", 10);
        }
    }

    @Inject(
            method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            at =@At("TAIL"),
            cancellable = true
    )
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        System.out.println("toTag called!");
        System.out.println("Intended tag:");
        System.out.println(tag.toString());

        System.out.println("Setting Enchantments key!");
        tag.put("Enchantments", enchantmentData);
    }


}
