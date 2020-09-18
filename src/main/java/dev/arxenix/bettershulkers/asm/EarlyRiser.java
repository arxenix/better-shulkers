package dev.arxenix.bettershulkers.asm;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;

public class EarlyRiser implements Runnable{
        @Override
        public void run() {
                final String EnchantmentTarget = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "net.minecraft.class_1886");
                ClassTinkerers.enumBuilder(EnchantmentTarget, new Class[0]).addEnumSubclass("SHULKER_BOX", "dev.arxenix.bettershulkers.asm.ShulkerTarget").build();
        }
}