package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.hud.component.HudComponentArmorBar;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(value = HudComponentArmorBar.class, remap = false)
public abstract class HudComponentArmorBarMixin {
    @Final
    @Shadow
    private HumanArmorShape targetShape;

    @Redirect(
        method = "render",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I")
    )
    private int bwb$keepBackpackIconStable(Random random, int bound) {
        Minecraft mc = Minecraft.getMinecraft();
        if (this.targetShape == HumanArmorShape.CHEST && mc.thePlayer != null) {
            ItemStack chestStack = mc.thePlayer.getItemInArmorSlot(HumanArmorShape.CHEST);
            if (chestStack != null && chestStack.getItem() instanceof ItemBackpack) {
                return 0;
            }
        }
        return random.nextInt(bound);
    }
}
