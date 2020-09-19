package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import dev.arxenix.bettershulkers.ducks.EnchantmentHolder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShulkerBoxBlock.class)
abstract class ShulkerBoxBlockMixin extends Block {
    public ShulkerBoxBlockMixin(Settings settings) {
        super(settings);
    }

    /*@Inject(
            method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/loot/context/LootContext$Builder;)Ljava/util/List;",
            at = @At(
                    "TAIL"
            ),
            locals = LocalCapture.PRINT
    )
    public void getDroppedStacks(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> ci) {
        System.out.println("dropping stacks");
    }

     */

    /**
     * @author arxenix
     * @reason just cuz
     */
    @Overwrite
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof ShulkerBoxBlockEntity) {
            ShulkerBoxBlockEntity sbe = (ShulkerBoxBlockEntity)blockEntity;
            ItemStack is = ShulkerUtilsKt.itemStackFromBlockEntity(sbe);
            ArrayList<ItemStack> ret = new ArrayList<>();
            ret.add(is);
            return ret;
        }
        return super.getDroppedStacks(state, builder);
    }

    /**
     * @author arxenix
     * @reason I need to overwrite the entire behavior
     */
    @Overwrite
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ShulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative()) {
                //System.out.println("is a shulkerboxblockentity!");
                ShulkerBoxBlockEntity sbe = (ShulkerBoxBlockEntity) be;

                ItemStack itemStack = ShulkerUtilsKt.itemStackFromBlockEntity(sbe);

                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }

        }
        super.onBreak(world, pos, state, player);
    }

    /*
    @Inject(
        method= "onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)V",
        at = @At(
                "HEAD"
        ),
        cancellable = true
    )
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {

        ci.cancel();
    }
    */
}
