package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.sajmonoriginal.betterwithbags.gui.MenuBackpack;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//** Config Logic*/
import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class ContainerInventoryMixin {
    @Shadow
    @Final
    public Player player;

    @ModifyReturnValue(method = "currentSlotLocked", at = @At("RETURN"))
    private boolean bwb$lockOpenBackpackSlot(boolean locked) {
        return locked || this.player != null && this.player.containerMenu instanceof MenuBackpack;
    }

    // Stops the drop/re-pickup loop: an ejected bag can't be picked straight back up. Ryan added this
    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void bwb$oneBackpackOnly(ItemStack stack, boolean flag, CallbackInfo ci) {
        if (BetterWithBagsMod.oneBagLimit //added logic to reference config settings
                && this.player != null
                && ItemBackpack.isBackpack(stack)
                && ItemBackpack.countCarriedBackpacks(this.player) > 0) {
            ci.cancel();
        }

    }
}
