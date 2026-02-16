package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {

    private static final String TEXTURE_PATH = "/assets/betterwithbags/textures/entity/";
    private static final String GEOMETRY_ID = "geometry.humanoid.armor.bag";
    private static final String BEDROLL_GEOMETRY_ID = "geometry.bag.bedroll";
    private static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";

    @Shadow
    private ModelBiped modelBipedMain;

    @Unique
    private static StaticEntityModel bwb$model;

    @Unique
    private static StaticEntityModel bwb$modelBedroll;

    @Unique
    private static boolean bwb$bedrollTextureChecked = false;

    @Unique
    private static boolean bwb$bedrollTextureValid = false;

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
    private StaticEntityModel bwb$getBedrollModel() {
        if (bwb$modelBedroll == null) {
            bwb$modelBedroll = EntityGeometryMojangData.Cache.getModel(BEDROLL_GEOMETRY_ID, 0);
        }
        return bwb$modelBedroll;
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
        boolean hasBedroll = ItemBackpack.hasAttachment(chestSlot);

        GL11.glPushMatrix();

        float bodyYaw = modelBipedMain.body.yRot;
        float bodyPitch = modelBipedMain.body.xRot;
        GL11.glRotatef(bodyYaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bodyPitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

        float s = 0.0625F;
        GL11.glScalef(s, s, s);
        GL11.glTranslatef(0.0F, 24.6F, 0.0F);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.renderDispatcher.textureManager.loadTexture(bwb$getTexture(tier)).bind();
        bwb$getModel().render(Tessellator.instance);

        if (hasBedroll) {
            if (!bwb$bedrollTextureChecked) {
                bwb$bedrollTextureChecked = true;
                try {
                    InputStream stream = MobRendererPlayerMixin.class.getResourceAsStream(BEDROLL_TEXTURE);
                    if (stream != null) {
                        BufferedImage testImg = ImageIO.read(stream);
                        bwb$bedrollTextureValid = (testImg != null);
                        stream.close();
                    }
                } catch (Exception e) {
                    bwb$bedrollTextureValid = false;
                }
            }
            if (bwb$bedrollTextureValid) {
                try {
                    StaticEntityModel bedroll = bwb$getBedrollModel();
                    if (bedroll != null) {
                        this.renderDispatcher.textureManager.loadTexture(BEDROLL_TEXTURE).bind();
                        bedroll.render(Tessellator.instance);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
