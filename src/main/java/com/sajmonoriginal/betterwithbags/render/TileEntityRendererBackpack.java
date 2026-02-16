package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.util.helper.Direction;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBackpack extends TileEntityRenderer<TileEntityBackpack> {

    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";
    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";
    private static final String BEDROLL_GEOMETRY_ID = "geometry.bag.bedroll";
    private static final String BEDROLL_TEXTURE = TEXTURE_PATH + "bedroll_attachment.png";

    private static StaticEntityModel placedModel;
    private static StaticEntityModel bedrollModel;

    private static boolean bedrollTextureChecked = false;
    private static boolean bedrollTextureValid = false;
    private StaticEntityModel getModel() {
        if (placedModel == null) {
            placedModel = EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0);
        }
        return placedModel;
    }

    private StaticEntityModel getBedrollModel() {
        if (bedrollModel == null) {
            bedrollModel = EntityGeometryMojangData.Cache.getModel(BEDROLL_GEOMETRY_ID, 0);
        }
        return bedrollModel;
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

    private BackpackTier getDisplayTier(TileEntityBackpack tileEntity) {
        if (tileEntity.worldObj != null) {
            int blockId = tileEntity.worldObj.getBlockId(tileEntity.x, tileEntity.y, tileEntity.z);
            BackpackTier blockTier = BackpackBlocks.getTierForBlockId(blockId);
            if (blockTier != null) return blockTier;
        }
        return tileEntity.getTier();
    }

    @Override
    public void doRender(Tessellator tessellator, TileEntityBackpack tileEntity, double x, double y, double z, float partialTick) {
        boolean hasBedroll = tileEntity.hasAttachment();
        StaticEntityModel backpackModel = getModel();
        if (backpackModel == null) {
            return;
        }

        BackpackTier tier = getDisplayTier(tileEntity);
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

        if (hasBedroll) {
            if (!bedrollTextureChecked) {
                bedrollTextureChecked = true;
                try {
                    InputStream stream = TileEntityRendererBackpack.class.getResourceAsStream(BEDROLL_TEXTURE);
                    if (stream != null) {
                        BufferedImage testImg = ImageIO.read(stream);
                        bedrollTextureValid = (testImg != null);
                        stream.close();
                    }
                } catch (Exception e) {
                    bedrollTextureValid = false;
                }
            }
            if (bedrollTextureValid) {
                StaticEntityModel bedroll = getBedrollModel();
                if (bedroll != null) {
                    loadTexture(BEDROLL_TEXTURE);
                    bedroll.render(tessellator);
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }
}
