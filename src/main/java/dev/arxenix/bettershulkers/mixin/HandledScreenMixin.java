package dev.arxenix.bettershulkers.mixin;

import dev.arxenix.bettershulkers.BetterShulkers;
import dev.arxenix.bettershulkers.BetterShulkersKt;
import dev.arxenix.bettershulkers.ShulkerUtilsKt;
import dev.arxenix.bettershulkers.ducks.BackpackHandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class HandledScreenMixin implements BackpackHandledScreen {
    public boolean backpackKeyPressed = false;
    @Inject(at = @At(value = "TAIL"), method = "keyPressed")
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        backpackKeyPressed =  BetterShulkersKt.getUSE_BACKPACK_KEY().matchesKey(keyCode, scanCode);
    }

    @Override
    public boolean getBackpackKeyPressed() {
        return backpackKeyPressed;
    }

    @Override
    public void resetBackpackKey() {
        backpackKeyPressed = false;
    }
}
