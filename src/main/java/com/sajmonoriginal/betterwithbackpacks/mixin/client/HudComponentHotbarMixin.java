package com.sajmonoriginal.betterwithbackpacks.mixin.client;

import com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.HudComponentHotbar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = HudComponentHotbar.class, remap = false)
public abstract class HudComponentHotbarMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/container/ContainerInventory;getCurrentItemIndex()I"), cancellable = true)
    private void bwb$hideSelectionWhenMeshSelected(Minecraft mc, HudIngame hud, int screenWidth, int screenHeight, float partialTicks, CallbackInfo ci) {
        if (mc.thePlayer != null) {
            MeshHotbarCapable cap = (MeshHotbarCapable) mc.thePlayer.inventory;
            if (cap.betterWithBackpacks$getMeshSelected() > 0) {
                ci.cancel();
            }
        }
    }
}
