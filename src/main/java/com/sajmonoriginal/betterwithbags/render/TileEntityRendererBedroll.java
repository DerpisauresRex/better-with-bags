package com.sajmonoriginal.betterwithbags.render;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.tileentity.TileEntityRenderer;

@Environment(EnvType.CLIENT)
public class TileEntityRendererBedroll extends TileEntityRenderer<TileEntityBedroll> {

    private static final String TEX_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/block/bedroll/";

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
    public void doRender(TessellatorGeneral tessellator, TileEntityBedroll tileEntity, double x, double y, double z, float partialTick) {
        if (tileEntity.isFoot()) {
            return;
        }

        int direction = tileEntity.getDirection();

        float footDx, footDz;
        switch (direction) {
            case 0: footDx =  0; footDz = -1; break;
            case 1: footDx =  1; footDz =  0; break;
            case 2: footDx =  0; footDz =  1; break;
            case 3: footDx = -1; footDz =  0; break;
            default: footDx = 0; footDz = 0; break;
        }

        GLRenderer.pushFrame();

        GLRenderer.modelM4f().translate((float) x + 0.5f + footDx, (float) y, (float) z + 0.5f + footDz);
        GLRenderer.modelM4f().rotateY((float) Math.toRadians(getRotation(direction)));
        GLRenderer.modelM4f().translate(-0.5f, 0.0f, -0.5f);

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.setColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GLRenderer.disableState(State.CULL_FACE);

        renderHeadBlock(tessellator);
        renderFootBlock(tessellator);

        GLRenderer.enableState(State.CULL_FACE);
        GLRenderer.disableState(State.BLEND);
        GLRenderer.popFrame();
    }

    private void renderHeadBlock(TessellatorGeneral t) {
        bindTexture(HEAD_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 0, 0, 0);
        t.addVertexWithUV(0, H, 1, 1, 0);
        t.addVertexWithUV(1, H, 1, 1, 1);
        t.addVertexWithUV(1, H, 0, 0, 1);
        t.draw();

        bindTexture(HEAD_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 0, 0, 1);
        t.addVertexWithUV(1, 0, 1, 1, 1);
        t.addVertexWithUV(0, 0, 1, 1, 0);
        t.addVertexWithUV(0, 0, 0, 0, 0);
        t.draw();

        bindTexture(HEAD_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(1, H, 0, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 0, 1, SIDE_V1);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.draw();

        bindTexture(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 0, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 1, 1, SIDE_V0);
        t.draw();

        bindTexture(HEAD_SIDE);
        t.startDrawingQuads();
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(1, H, 1, 1, SIDE_V0);
        t.addVertexWithUV(1, 0, 1, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 0, 0, SIDE_V1);
        t.addVertexWithUV(1, H, 0, 0, SIDE_V0);
        t.draw();
    }

    private void renderFootBlock(TessellatorGeneral t) {
        bindTexture(FOOT_TOP);
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(0, H, 1, 0, 0);
        t.addVertexWithUV(0, H, 2, 1, 0);
        t.addVertexWithUV(1, H, 2, 1, 1);
        t.addVertexWithUV(1, H, 1, 0, 1);
        t.draw();

        bindTexture(FOOT_BOTTOM);
        t.startDrawingQuads();
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(1, 0, 1, 0, 1);
        t.addVertexWithUV(1, 0, 2, 1, 1);
        t.addVertexWithUV(0, 0, 2, 1, 0);
        t.addVertexWithUV(0, 0, 1, 0, 0);
        t.draw();

        bindTexture(FOOT_FRONT);
        t.startDrawingQuads();
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(1, H, 2, 0, SIDE_V0);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(1, 0, 2, 0, SIDE_V1);
        t.draw();

        bindTexture(FOOT_SIDE);
        t.startDrawingQuads();
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(0, H, 1, 0, SIDE_V0);
        t.addVertexWithUV(0, 0, 1, 0, SIDE_V1);
        t.addVertexWithUV(0, 0, 2, 1, SIDE_V1);
        t.addVertexWithUV(0, H, 2, 1, SIDE_V0);
        t.draw();

        bindTexture(FOOT_SIDE);
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
