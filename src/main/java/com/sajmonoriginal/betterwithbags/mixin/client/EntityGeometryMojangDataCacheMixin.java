package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BackpackModels;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.texturepack.TexturePackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(targets = "org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData$Cache", remap = false)
public class EntityGeometryMojangDataCacheMixin {

    @Inject(method = "reload", at = @At("RETURN"))
    private static void bwb$reloadGeo(TexturePackList texturePacks, CallbackInfo ci) {
        BackpackModels.loadBwbGeoModels();
    }
}
