package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBedroll extends TileEntityRenderer<TileEntityBedroll> {

    private static final String TEX_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/block/bedroll/";

    // IMPORTANT: Head and foot are SWAPPED here to fix orientation
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

    @Override
    public void doRender(Tessellator tessellator, TileEntityBedroll tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity.isFoot()) {
            return;
        }

        int direction = tileEntity.getDirection();

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GL11.glRotatef(getRotation(direction), 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.5f, 0.0f, -0.5f);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(GL11.GL_CULL_FACE);

        renderHeadBlock(tessellator);
        renderFootBlock(tessellator);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private void renderHeadBlock(Tessellator t) {
        loadTexture(HEAD_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 0, 0, 0);
        t.addVertexWithUV(0, H, 1, 1, 0);
        t.addVertexWithUV(1, H, 1, 1, 1);
        t.addVertexWithUV(1, H, 0, 0, 1);
        t.draw();

        loadTexture(HEAD_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 0, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(0, 0, 1, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        loadTexture(HEAD_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(1, H, 0, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 0, 1, SIDE_V1);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.draw();

        loadTexture(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 1, 1, SIDE_V0);
        t.draw();

        loadTexture(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 1, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 0, 0, SIDE_V0);
        t.draw();
    }

    private void renderFootBlock(Tessellator t) {
        loadTexture(FOOT_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 1, 0, 0);
        t.addVertexWithUV(0, H, 2, 1, 0);
        t.addVertexWithUV(1, H, 2, 1, 1);
        t.addVertexWithUV(1, H, 1, 0, 1);
        t.draw();

        loadTexture(FOOT_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 2, 1, 1);
        t.addVertexWithUV(0, 0, 2, 1, 0);
        t.addVertexWithUV(0, 0, 1, 0, 0);
        t.draw();

        loadTexture(FOOT_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(1, H, 2, 0, SIDE_V0);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 2, 0, SIDE_V1);
        t.draw();

        loadTexture(FOOT_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 1, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.draw();

        loadTexture(FOOT_SIDE);
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 1, 0, SIDE_V0);
        t.draw();
    }

    private float getRotation(int direction) {
        switch (direction) {
            case 0: return 0f;
            case 1: return 270f;
            case 2: return 180f;
            case 3: return 90f;
            default: return 0f;
        }
    }
}
