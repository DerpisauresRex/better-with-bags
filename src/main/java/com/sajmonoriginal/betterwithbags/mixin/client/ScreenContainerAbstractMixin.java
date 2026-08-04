package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbags.slot.SlotBackpackAttachment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenContainerAbstract.class, remap = false)
public abstract class ScreenContainerAbstractMixin {

    @ModifyVariable(method = {"mouseClicked", "mouseReleased"}, at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int bwb$transformMouseX(int mouseX) {
        if (ResponsiveBackpackScreen.MOUSE_ALREADY_TRANSFORMED.get()) {
            return mouseX;
        }
        if ((Object) this instanceof ResponsiveBackpackScreen) {
            ResponsiveBackpackScreen screen = (ResponsiveBackpackScreen) (Object) this;
            if (screen.betterWithBags$hasResponsiveLayout()) {
                return screen.betterWithBags$transformMouseX(mouseX);
            }
        }
        return mouseX;
    }

    @ModifyVariable(method = {"mouseClicked", "mouseReleased"}, at = @At("HEAD"), argsOnly = true, ordinal = 1)
    private int bwb$transformMouseY(int mouseY) {
        if (ResponsiveBackpackScreen.MOUSE_ALREADY_TRANSFORMED.get()) {
            return mouseY;
        }
        if ((Object) this instanceof ResponsiveBackpackScreen) {
            ResponsiveBackpackScreen screen = (ResponsiveBackpackScreen) (Object) this;
            if (screen.betterWithBags$hasResponsiveLayout()) {
                return screen.betterWithBags$transformMouseY(mouseY);
            }
        }
        return mouseY;
    }

    @Shadow
    public abstract Slot getSlotAtPosition(int mouseX, int mouseY);

    @Inject(method = "getSlotId", at = @At("RETURN"), cancellable = true)
    private void bwb$allowBackpackSlotClicks(int mouseX, int mouseY, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() == -999) {
            Slot slot = getSlotAtPosition(mouseX, mouseY);
            if (slot instanceof SlotBackpack || slot instanceof SlotBackpackAttachment) {
                cir.setReturnValue(slot.index);
            }
        }
    }
}
