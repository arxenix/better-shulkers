package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import dev.arxenix.bettershulkers.ducks.EnchantmentHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {
    // forward item enchantment data to the ShulkerBoxBlockEntityRenderer, so it knows whether to render glint or not
    @Redirect(
            method="render",
            at=@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;renderEntity(Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)Z"
            )
    )
    private boolean renderEntity(BlockEntityRenderDispatcher instance, BlockEntity blockEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j,
                                 ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack2, VertexConsumerProvider vertexConsumerProvider2, int i2, int j2) {
        if (ShulkerUtilsKt.isShulker(stack) && ShulkerUtilsKt.isShulker(blockEntity)) {
            ShulkerBoxBlockEntity sbe = (ShulkerBoxBlockEntity) blockEntity;
            ((EnchantmentHolder) sbe).setEnchantments(stack.getEnchantments());
        }
        return instance.renderEntity(blockEntity, matrixStack, vertexConsumerProvider, i, j);
    }
}
