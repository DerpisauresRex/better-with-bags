package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.config.BackpackPositionConfig;
import com.sajmonoriginal.betterwithbags.config.BackpackPositionConfig.TierTransform;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {

    private static final String TEXTURE_PATH = "/assets/betterwithbags/textures/entity/";
    private static final String GEOMETRY_ID = "geometry.humanoid.armor.bag";

    @Shadow
    private ModelBiped modelBipedMain;

    @Unique
    private static StaticEntityModel bwb$model;

    protected MobRendererPlayerMixin(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Unique
    private StaticEntityModel bwb$getModel() {
        if (bwb$model == null) {
            bwb$model = EntityGeometryMojangData.Cache.getModel(GEOMETRY_ID, 0);
        }
        return bwb$model;
    }

    @Unique
    private String bwb$getTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_" + tier.getName() + ".png";
    }

    @Inject(method = "renderSpecials(Lnet/minecraft/core/entity/player/Player;F)V", at = @At("TAIL"))
    private void renderBackpackOnBack(Player player, float partialTick, CallbackInfo ci) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        if (chestSlot == null || !(chestSlot.getItem() instanceof ItemBackpack)) {
            return;
        }

        ItemBackpack backpackItem = (ItemBackpack) chestSlot.getItem();
        BackpackTier tier = backpackItem.getTier();
        StaticEntityModel model = bwb$getModel();

        GL11.glPushMatrix();

        this.renderDispatcher.textureManager.loadTexture(bwb$getTexture(tier)).bind();

        BackpackPositionConfig.tick();
        TierTransform t = BackpackPositionConfig.get(tier);

        float bodyYaw = modelBipedMain.body.yRot;
        float bodyPitch = modelBipedMain.body.xRot;
        GL11.glRotatef(bodyYaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bodyPitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        float s = t.scale;
        GL11.glScalef(s, s, s);
        GL11.glTranslatef(t.translateX, t.translateY, t.translateZ);
        GL11.glRotatef(t.rotateX, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(t.rotateY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(t.rotateZ, 0.0F, 0.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        model.render(Tessellator.instance);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
