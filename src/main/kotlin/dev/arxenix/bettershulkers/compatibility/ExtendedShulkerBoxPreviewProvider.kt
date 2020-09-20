package dev.arxenix.bettershulkers.compatibility

import com.misterpemodder.shulkerboxtooltip.api.PreviewContext
import com.misterpemodder.shulkerboxtooltip.api.provider.BlockEntityPreviewProvider
import dev.arxenix.bettershulkers.getShulkerSizeFromBlockEntityTag
import dev.arxenix.bettershulkers.isShulker
import net.minecraft.block.Block
import net.minecraft.block.ShulkerBoxBlock
import net.minecraft.item.BlockItem
import net.minecraft.util.DyeColor

class ExtendedShulkerBoxPreviewProvider: BlockEntityPreviewProvider(27, true) {
    private val SHULKER_BOX_COLOR: FloatArray = floatArrayOf(0.592f, 0.403f, 0.592f)

    override fun showTooltipHints(context: PreviewContext): Boolean {
        return true;
    }

    override fun getWindowColor(context: PreviewContext): FloatArray {
        val dye = (Block.getBlockFromItem(context.getStack().getItem()) as ShulkerBoxBlock).getColor()

        if (dye != null) {
            val components = dye.getColorComponents()

            return floatArrayOf(Math.max(0.15f, components[0]), Math.max(0.15f, components[1]),
                Math.max(0.15f, components[2]))
        } else {
          return SHULKER_BOX_COLOR
        }
    }

    override fun getPriority(): Int {
        // Set priority higher than ShulkerBoxPreviewProvider
        return 2000
    }

    override fun getInventoryMaxSize(context: PreviewContext): Int {
        val stack = context.getStack()

        if (isShulker(stack)) {
            val tag = stack.getSubTag("BlockEntityTag")

            if (tag != null)
                return getShulkerSizeFromBlockEntityTag(tag)
        }
        return this.maxInvSize
    }
}