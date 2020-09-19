package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.BetterShulkersKt;
import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import dev.arxenix.bettershulkers.ducks.EnchantmentHolder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements EnchantmentHolder {
    //@Shadow private DefaultedList<ItemStack> inventory;
    @Unique
    private ListTag enchantmentData = new ListTag();
    @Unique
    private Map<Enchantment, Integer> enchantmentMap = new HashMap<>();

    @Override
    public ListTag getEnchantments() {
        return enchantmentData;
    }

    @Override
    public void setEnchantments(ListTag tag) {
        this.enchantmentData = tag;
        this.enchantmentMap = EnchantmentHelper.fromTag(enchantmentData);
    }

    /*
    @Inject(
            method = "<init>(Lnet/minecraft/util/DyeColor;)V",
            at=@At("TAIL")
    )
    public void init(DyeColor color, CallbackInfo ci) {
        this.inventory = DefaultedList.ofSize(54, ItemStack.EMPTY);
    }
     */

    @Inject(
            method= "fromTag(Lnet/minecraft/block/BlockState;Lnet/minecraft/nbt/CompoundTag;)V",
            at=@At(
                    value="INVOKE",
                    target="Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;deserializeInventory(Lnet/minecraft/nbt/CompoundTag;)V"
            )
    )
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        //System.out.println("fromTag called");
        //System.out.println(tag.toString());
        if (tag.contains("Enchantments", 9)) {
            //System.out.println("has Enchantments!!");
            setEnchantments(tag.getList("Enchantments", 10));
        }

        /* enable glint on be
        World world = ((ShulkerBoxBlockEntity)(Object)this).getWorld();
        if (world != null && !world.isClient) {
            sync();
        }
         */
    }

    /**
     * @author arxenix
     * @reason BetterShulkers
     */
    @Overwrite
    public int size() {
        return ShulkerUtilsKt.getShulkerSizeFromEnchantmentsMap(enchantmentMap);
    }

    @Inject(
            method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            at =@At("TAIL"),
            cancellable = true
    )
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        //System.out.println("toTag called!");
        //System.out.println(tag.toString());
        tag.put("Enchantments", enchantmentData);
    }

    /**
     * @author arxenix
     * @reason BetterShulkers
     */
    @Overwrite
    public int[] getAvailableSlots(Direction side) {
        return IntStream.range(0, ShulkerUtilsKt.getShulkerSizeFromEnchantmentsTag(enchantmentData)).toArray();
    }


    /**
     * @author arxenix
     * @reason BetterShulkers
     */
    @Overwrite
    public Text getContainerName() {
        TranslatableText name = new TranslatableText("container.shulkerBox");
        if (!enchantmentMap.isEmpty()) {
            Style style = Style.EMPTY.withFormatting(Formatting.AQUA);
            ItemStack myStack = ShulkerUtilsKt.itemStackFromBlockEntity((ShulkerBoxBlockEntity) (Object) this);
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(myStack)));
            name.setStyle(style);
        }
        return name;
    }

    /**
     * @author arxenix
     * @reason BetterShulkers
     */
    @Overwrite
    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        int extraRows = enchantmentMap.getOrDefault(BetterShulkersKt.getENLARGE_ENCHANT(), 0);
        int rows = 3 + Math.min(extraRows, 3);

        ScreenHandlerType type = ScreenHandlerType.GENERIC_9X3;
        switch(rows) {
            case 4:
                type = ScreenHandlerType.GENERIC_9X4;
                break;
            case 5:
                type = ScreenHandlerType.GENERIC_9X5;
                break;
            case 6:
                type = ScreenHandlerType.GENERIC_9X6;
                break;
        }

        ScreenHandler newHandler = new GenericContainerScreenHandler(type, syncId, playerInventory, (ShulkerBoxBlockEntity) (Object) this, rows);
        for (int i=0; i<9*rows; i++) {
            Slot oldSlot = newHandler.slots.get(i);
            Slot newSlot = new ShulkerBoxSlot(oldSlot.inventory, i, oldSlot.x, oldSlot.y);
            newSlot.id = oldSlot.id;
            newHandler.slots.set(i, newSlot);
        }
        return newHandler;
    }


    /* enable glint on blockentity
    @Override
    public void fromClientTag(CompoundTag tag) {
        this.setEnchantments(tag.getList("Enchantments", 10));
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag.put("Enchantments", enchantmentData);
        return tag;
    }
    */
}
