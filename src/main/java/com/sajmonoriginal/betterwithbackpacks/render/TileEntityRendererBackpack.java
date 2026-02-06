package com.sajmonoriginal.betterwithbackpacks.render;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import com.sajmonoriginal.betterwithbackpacks.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Direction;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBackpack extends TileEntityRenderer<TileEntityBackpack> {

    private static final String TEXTURE_PATH = "/assets/" + BetterWithBackpacksMod.MOD_ID + "/textures/entity/";
    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";

    private static StaticEntityModel placedModel;

    private StaticEntityModel getModel() {
        if (placedModel == null) {
            placedModel = EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0);
        }
        return placedModel;
    }

    private String getTexture(BackpackTier tier) {
        return TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
    }

    private float getRotationFromMeta(int meta) {
        Direction dir = Direction.getDirectionById(meta);
        if (dir == Direction.NORTH) return 180f;
        if (dir == Direction.SOUTH) return 0f;
        if (dir == Direction.WEST) return 90f;
        if (dir == Direction.EAST) return -90f;
        return 0f;
    }

    @Override
    public void doRender(Tessellator tessellator, TileEntityBackpack tileEntity, double x, double y, double z, float partialTick) {
        StaticEntityModel backpackModel = getModel();
        if (backpackModel == null) {
            return;
        }

        BackpackTier tier = tileEntity.getTier();
        int meta = tileEntity.getBlockMeta();
        float rotation = getRotationFromMeta(meta);

        GL11.glPushMatrix();

        float scale = 1.0f / 16.0f;
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(0.0f, -13.5f, 0.0f);
        GL11.glRotatef(rotation, 0.0f, 1.0f, 0.0f);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        loadTexture(getTexture(tier));
        backpackModel.render(tessellator);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();

        if (tier.hasMeshPocket()) {
            ItemStack meshLeft = tileEntity.getMeshPocketItem(0);
            ItemStack meshRight = tileEntity.getMeshPocketItem(1);
            if (meshLeft != null && meshLeft.getItem() instanceof ItemBlock) {
                renderMeshBlock(tessellator, meshLeft, x, y, z, rotation, 1);
            }
            if (meshRight != null && meshRight.getItem() instanceof ItemBlock) {
                renderMeshBlock(tessellator, meshRight, x, y, z, rotation, -1);
            }
        }
    }

    private void renderMeshBlock(Tessellator tessellator, ItemStack meshItem, double x, double y, double z, float rotation, int side) {
        Block<?> block = Blocks.blocksList[meshItem.itemID];
        if (block == null) return;
        BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(block);
        if (blockModel == null) return;

        float meshCenterX = -side * 5.5f;
        float meshCenterY = 16.25f;
        float meshCenterZ = 4.5f;
        float pixelScale = 1.0f / 16.0f;
        float blockScale = 2.0f;

        TextureRegistry.blockAtlas.bind();

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GL11.glScalef(pixelScale, pixelScale, pixelScale);
        GL11.glTranslatef(0.0f, -13.5f, 0.0f);
        GL11.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(meshCenterX, meshCenterY, meshCenterZ);
        GL11.glScalef(blockScale, blockScale, blockScale);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        blockModel.renderBlockOnInventory(tessellator, meshItem.getMetadata(), 1.0f, null);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
