package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.recipe.RecipeEntryBackpackUpgrade;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.guidebook.crafting.RecipePageCrafting;
import net.minecraft.client.gui.guidebook.crafting.displays.DisplayAdapterShaped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = RecipePageCrafting.class, remap = false)
public abstract class RecipePageCraftingMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void bwb$showBackpackUpgradesInGuidebook(CallbackInfo ci) {
        RecipePageCrafting.recipeToDisplayAdapterMap.put(RecipeEntryBackpackUpgrade.class, new DisplayAdapterShaped());
    }
}
