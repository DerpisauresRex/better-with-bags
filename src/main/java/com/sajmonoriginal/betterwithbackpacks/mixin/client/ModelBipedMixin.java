package com.sajmonoriginal.betterwithbackpacks.mixin.client;

import com.sajmonoriginal.betterwithbackpacks.util.MeshBackpackRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelBiped;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ModelBiped.class, remap = false)
public class ModelBipedMixin {

    @Shadow
    public Cube armLeft;

    @Shadow
    public Cube armRight;

    @Shadow
    public boolean holdingLeftHand;

    @Shadow
    public boolean holdingRightHand;

    @Inject(method = "setupAnimation", at = @At("TAIL"))
    private void bwb$limitArmSwingForMeshBackpack(float limbSwing, float limbYaw, float ticksExisted, float headYaw, float headPitch, float scale, CallbackInfo ci) {
        if (!MeshBackpackRenderState.hasMeshBackpack()) {
            return;
        }

        if (!this.holdingRightHand) {
            this.armRight.xRot = this.armRight.xRot * 0.5f - 0.3141593f * limbYaw;
        }

        if (!this.holdingLeftHand) {
            this.armLeft.xRot = this.armLeft.xRot * 0.5f - 0.3141593f * limbYaw;
        }
    }
}
