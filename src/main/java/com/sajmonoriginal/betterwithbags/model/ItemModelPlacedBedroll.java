package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBedroll extends ItemModelStandard {

    private static final String TEX_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/block/bedroll/";

    // Head and foot textures are SWAPPED (same as TileEntityRendererBedroll)
    private static final String HEAD_TOP = TEX_BASE + "foot_top.png";
    private static final String HEAD_BOTTOM = TEX_BASE + "foot_bottom.png";
    private static final String HEAD_SIDE = TEX_BASE + "foot_side.png";
    private static final String HEAD_FRONT = TEX_BASE + "foot_front.png";

    private static final String FOOT_TOP = TEX_BASE + "head_top.png";
    private static final String FOOT_BOTTOM = TEX_BASE + "head_bottom.png";
    private static final String FOOT_SIDE = TEX_BASE + "head_side.png";
    private static final String FOOT_FRONT = TEX_BASE + "head_front.png";

    private static final float H = 4f / 16f;
    private static final float SIDE_V0 = 12f / 16f;
    private static final float SIDE_V1 = 1f;

    public ItemModelPlacedBedroll(Item blockItem) {
        super(blockItem, null);
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/bedroll");
    }

    @Override
    public void renderItemIntoGui(Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        GL11.glTranslatef(x - 2, y + 3, -3.0f);
        GL11.glScalef(10.0f, 10.0f, 10.0f);
        GL11.glTranslatef(1.0f, 0.5f, 1.0f);
        GL11.glScalef(1.0f, 1.0f, -1.0f);
        GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glColor4f(brightness, brightness, brightness, alpha);
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);

        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glTranslatef(0.5f, 0.0f, 0.0f);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_CULL_FACE);

        renderHeadBlock(tessellator, textureManager);
        renderFootBlock(tessellator, textureManager);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderHeadBlock(Tessellator t, TextureManager textureManager) {
        textureManager.loadTexture(HEAD_TOP).bind();
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 0, 0, 0);
        t.addVertexWithUV(0, H, 1, 1, 0);
        t.addVertexWithUV(1, H, 1, 1, 1);
        t.addVertexWithUV(1, H, 0, 0, 1);
        t.draw();

        textureManager.loadTexture(HEAD_BOTTOM).bind();
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 0, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(0, 0, 1, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        textureManager.loadTexture(HEAD_FRONT).bind();
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(1, H, 0, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 0, 1, SIDE_V1);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.draw();

        textureManager.loadTexture(HEAD_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 1, 1, SIDE_V0);
        t.draw();

        textureManager.loadTexture(HEAD_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 1, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 0, 0, SIDE_V0);
        t.draw();
    }

    private void renderFootBlock(Tessellator t, TextureManager textureManager) {
        textureManager.loadTexture(FOOT_TOP).bind();
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 1, 0, 0);
        t.addVertexWithUV(0, H, 2, 1, 0);
        t.addVertexWithUV(1, H, 2, 1, 1);
        t.addVertexWithUV(1, H, 1, 0, 1);
        t.draw();

        textureManager.loadTexture(FOOT_BOTTOM).bind();
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 2, 1, 1);
        t.addVertexWithUV(0, 0, 2, 1, 0);
        t.addVertexWithUV(0, 0, 1, 0, 0);
        t.draw();

        textureManager.loadTexture(FOOT_FRONT).bind();
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(1, H, 2, 0, SIDE_V0);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 2, 0, SIDE_V1);
        t.draw();

        textureManager.loadTexture(FOOT_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 1, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.draw();

        textureManager.loadTexture(FOOT_SIDE).bind();
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 1, 0, SIDE_V0);
        t.draw();
    }
}
