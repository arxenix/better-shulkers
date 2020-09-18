package dev.arxenix.bettershulkers

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
class BetterShulkersClient: ClientModInitializer {
    override fun onInitializeClient() {
        println("BetterShulkersClient - fabric mod initialized")
    }
}