package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.ducks.EnchantmentHolder;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.SpriteTexturedVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.ShulkerBoxBlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ShulkerBoxBlockEntityRenderer.class)
public class ShulkerBoxBlockEntityRendererMixin {

    // give shulker boxes glint
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/util/SpriteIdentifier.getVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Ljava/util/function/Function;)Lnet/minecraft/client/render/VertexConsumer;"
            )
    )
    private VertexConsumer getVertexConsumer(SpriteIdentifier spriteIdentifier, VertexConsumerProvider vertexConsumerProvider, Function<Identifier, RenderLayer> layerFactory,
                                             ShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider2, int i, int j) {
        return new SpriteTexturedVertexConsumer(
                ItemRenderer.getDirectGlintVertexConsumer(
                        vertexConsumerProvider,
                        spriteIdentifier.getRenderLayer(layerFactory),
                        false,
                        !((EnchantmentHolder)shulkerBoxBlockEntity).getEnchantments().isEmpty()
                ),
                spriteIdentifier.getSprite()
        );
    }
}
