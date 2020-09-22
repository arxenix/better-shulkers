package dev.arxenix.bettershulkers.compatibility

import com.misterpemodder.shulkerboxtooltip.api.ShulkerBoxTooltipApi
import com.misterpemodder.shulkerboxtooltip.api.provider.PreviewProvider
import dev.arxenix.bettershulkers.MODID
import dev.arxenix.bettershulkers.SHULKER_ITEMS
import net.minecraft.item.Item

class ShulkerBoxTooltipHook : ShulkerBoxTooltipApi {
    override fun getModId(): String {
        return MODID
    }

    override fun registerProviders(previewProviders: MutableMap<PreviewProvider, List<Item>>) {
        previewProviders.put(ExtendedShulkerBoxPreviewProvider(), SHULKER_ITEMS.toList())
    }
}
