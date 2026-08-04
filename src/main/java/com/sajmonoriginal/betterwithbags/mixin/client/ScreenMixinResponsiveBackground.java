package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.gui.ResponsiveBackpackScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = Screen.class, remap = false)
public abstract class ScreenMixinResponsiveBackground {
    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void bwb$skipDuplicateResponsiveBackground(CallbackInfo ci) {
        if (ResponsiveBackpackScreen.RENDERING_RESPONSIVE.get() ||
            (Object) this instanceof ResponsiveBackpackScreen &&
                ((ResponsiveBackpackScreen) (Object) this).betterWithBags$isRenderingResponsive()) {
            ci.cancel();
        }
    }
}
