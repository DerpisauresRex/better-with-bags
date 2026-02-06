package com.sajmonoriginal.betterwithbackpacks.mixin.client;

import com.sajmonoriginal.betterwithbackpacks.config.BackpackPositionConfig;
import com.sajmonoriginal.betterwithbackpacks.config.BackpackPositionConfig.TierTransform;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.MeshBackpackRenderState;
import com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable;
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

    private static final String TEXTURE_PATH = "/assets/betterwithbackpacks/textures/entity/";
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

    @Inject(method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/entity/player/Player;DDDFF)V", at = @At("HEAD"))
    private void bwb$setMeshBackpackState(Tessellator tessellator, Player player, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        boolean hasMesh = chestSlot != null 
            && chestSlot.getItem() instanceof ItemBackpack 
            && ((ItemBackpack) chestSlot.getItem()).getTier().hasMeshPocket();
        MeshBackpackRenderState.setHasMeshBackpack(hasMesh);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/core/entity/player/Player;DDDFF)V", at = @At("RETURN"))
    private void bwb$clearMeshBackpackState(Tessellator tessellator, Player player, double x, double y, double z, float yaw, float partialTick, CallbackInfo ci) {
        MeshBackpackRenderState.setHasMeshBackpack(false);
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

        if (tier.hasMeshPocket()) {
            int meshSelected = ((com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable) player.inventory).betterWithBackpacks$getMeshSelected();
            ItemStack meshLeft = ItemBackpack.getMeshPocketItem(chestSlot, 0);
            ItemStack meshRight = ItemBackpack.getMeshPocketItem(chestSlot, 1);
            if (meshLeft != null && meshLeft.getItem() instanceof ItemBlock && meshSelected != 1) {
                bwb$renderMeshBlock(meshLeft, t, 1);
            }
            if (meshRight != null && meshRight.getItem() instanceof ItemBlock && meshSelected != 2) {
                bwb$renderMeshBlock(meshRight, t, -1);
            }
        }
    }

    @Unique
    private void bwb$renderMeshBlock(ItemStack meshItem, TierTransform t, int side) {
        Block<?> block = Blocks.blocksList[meshItem.itemID];
        if (block == null) return;
        BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(block);
        if (blockModel == null) return;

        float bodyYaw = modelBipedMain.body.yRot;
        float bodyPitch = modelBipedMain.body.xRot;

        float meshCenterX = side * 5.5f;
        float meshCenterY = 16.25f;
        float meshCenterZ = 4.5f;
        float blockScale = 2.0f / 16f / t.scale;

        TextureRegistry.blockAtlas.bind();

        GL11.glPushMatrix();

        GL11.glRotatef(bodyYaw * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(bodyPitch * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
        GL11.glScalef(t.scale, t.scale, t.scale);
        GL11.glTranslatef(t.translateX, t.translateY, t.translateZ);
        GL11.glRotatef(t.rotateX, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(t.rotateY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(t.rotateZ, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(meshCenterX, meshCenterY, meshCenterZ);
        GL11.glScalef(blockScale, blockScale, blockScale);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        blockModel.renderBlockOnInventory(Tessellator.instance, meshItem.getMetadata(), 1.0f, null);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
