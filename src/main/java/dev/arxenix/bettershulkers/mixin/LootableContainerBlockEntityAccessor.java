package dev.arxenix.bettershulkers.mixin;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootableContainerBlockEntity.class)
public interface LootableContainerBlockEntityAccessor {
    @Accessor
    Identifier getLootTableId();

    @Accessor
    long getLootTableSeed();

    @Invoker
    boolean callSerializeLootTable(CompoundTag compoundTag);
}
