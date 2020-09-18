package dev.arxenix.bettershulkers.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {


    @Inject(
            method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
            at = @At(
                    "TAIL"
            ),
            locals = LocalCapture.PRINT
    )
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
        System.out.println("dropping stacks");
    }

    @Inject(
        method= "onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",
        at = @At(
                "HEAD"
        )
    )
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        // TODO - make it so creative mode players can get the enchanted blocks back
        System.out.println("injection onBreak test");
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ShulkerBoxBlockEntity) {
            System.out.println("shulker");
            System.out.println(((LootableContainerBlockEntityAccessor) be).getLootTableId());
            System.out.println(((LootableContainerBlockEntityAccessor) be).getLootTableSeed());
            CompoundTag tag = new CompoundTag();
            ((LootableContainerBlockEntityAccessor) be).callSerializeLootTable(tag);
            System.out.println(tag.toText().asString());
            ((ShulkerBoxBlockEntity) be).checkLootInteraction(player);
            ((ShulkerBoxBlockEntity) be).checkLootInteraction(player);
            ((ShulkerBoxBlockEntity) be).checkLootInteraction(player);
        }
    }
}
