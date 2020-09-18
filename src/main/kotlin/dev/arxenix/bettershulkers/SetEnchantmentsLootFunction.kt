package dev.arxenix.bettershulkers

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootChoice
import net.minecraft.loot.LootTable
import net.minecraft.loot.LootTableReporter
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.entry.LootPoolEntry
import net.minecraft.loot.function.ConditionalLootFunction
import net.minecraft.loot.function.LootFunction
import net.minecraft.loot.function.LootFunctionType
import net.minecraft.loot.function.LootFunctionTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList
import java.util.*
import java.util.function.Consumer


class SetEnchantmentsLootFunction private constructor(conditions: Array<LootCondition>, entries: List<LootPoolEntry>) :
    ConditionalLootFunction(conditions) {
    private val entries: List<LootPoolEntry>
    override fun getType(): LootFunctionType {
        return LootFunctionTypes.SET_NBT
    }

    public override fun process(stack: ItemStack, context: LootContext): ItemStack {
        return if (stack.isEmpty) {
            stack
        } else {
            val defaultedList = DefaultedList.of<ItemStack>()
            entries.forEach(Consumer { entry: LootPoolEntry ->
                entry.expand(
                    context
                ) { choice: LootChoice ->
                    defaultedList.javaClass
                    choice.generateLoot(LootTable.processStacks { e: ItemStack ->
                        defaultedList.add(
                            e
                        )
                    }, context)
                }
            })
            val compoundTag = CompoundTag()
            Inventories.toTag(compoundTag, defaultedList)
            val compoundTag2 = stack.orCreateTag
            compoundTag2.put("BlockEntityTag", compoundTag.copyFrom(compoundTag2.getCompound("BlockEntityTag")))
            stack
        }
    }

    override fun validate(reporter: LootTableReporter) {
        super.validate(reporter)
        for (i in entries.indices) {
            entries[i].validate(reporter.makeChild(".entry[$i]"))
        }
    }

    class Serializer : ConditionalLootFunction.Serializer<SetEnchantmentsLootFunction>() {
        override fun toJson(
            jsonObject: JsonObject,
            setEnchantmentsLootFunction: SetEnchantmentsLootFunction,
            jsonSerializationContext: JsonSerializationContext
        ) {
            super.toJson(jsonObject, setEnchantmentsLootFunction, jsonSerializationContext)
            jsonObject.add("entries", jsonSerializationContext.serialize(setEnchantmentsLootFunction.entries))
        }

        override fun fromJson(
            jsonObject: JsonObject,
            jsonDeserializationContext: JsonDeserializationContext,
            lootConditions: Array<LootCondition>
        ): SetEnchantmentsLootFunction {
            val lootPoolEntrys = JsonHelper.deserialize(
                jsonObject, "entries", jsonDeserializationContext,
                Array<LootPoolEntry>::class.java
            ) as Array<LootPoolEntry>
            return SetEnchantmentsLootFunction(lootConditions, Arrays.asList(*lootPoolEntrys))
        }
    }

    class Builer : Builder<Builer>() {
        private val entries: MutableList<LootPoolEntry> = Lists.newArrayList()
        override fun getThisBuilder(): Builer {
            return this
        }

        fun withEntry(entryBuilder: LootPoolEntry.Builder<*>): Builer {
            entries.add(entryBuilder.build())
            return this
        }

        override fun build(): LootFunction {
            return SetEnchantmentsLootFunction(this.conditions, entries)
        }
    }

    companion object {
        fun builder(): Builer {
            return Builer()
        }
    }

    init {
        this.entries = ImmutableList.copyOf(entries)
    }
}