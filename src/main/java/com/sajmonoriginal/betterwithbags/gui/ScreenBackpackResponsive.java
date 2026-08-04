package com.sajmonoriginal.betterwithbags.gui;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.player.inventory.menu.MenuAbstract;

public abstract class ScreenBackpackResponsive extends ScreenContainerAbstract implements ResponsiveBackpackScreen {
    private static final int SCREEN_MARGIN = 8;
    private boolean renderingResponsive;

    protected ScreenBackpackResponsive(MenuAbstract menu) {
        super(menu);
    }

    private float getGuiScale() {
        float horizontalScale = (this.width - SCREEN_MARGIN * 2) / (float) this.xSize;
        float verticalScale = (this.height - SCREEN_MARGIN * 2) / (float) this.ySize;
        return Math.min(1.0F, Math.min(horizontalScale, verticalScale));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        float scale = getGuiScale();
        if (scale >= 1.0F) {
            super.render(mouseX, mouseY, partialTicks);
            return;
        }

        this.renderBackground();
        GLRenderer.pushFrame();
        GLRenderer.modelM4f()
            .translate(this.width / 2.0F, this.height / 2.0F, 0.0F)
            .scale(scale, scale, 1.0F)
            .translate(-this.width / 2.0F, -this.height / 2.0F, 0.0F);
        this.renderingResponsive = true;
        try {
            super.render(betterWithBags$transformMouseX(mouseX), betterWithBags$transformMouseY(mouseY), partialTicks);
        } finally {
            this.renderingResponsive = false;
            GLRenderer.popFrame();
        }
    }

    @Override
    public boolean betterWithBags$hasResponsiveLayout() {
        return getGuiScale() < 1.0F;
    }

    @Override
    public boolean betterWithBags$isRenderingResponsive() {
        return this.renderingResponsive;
    }

    @Override
    public int betterWithBags$transformMouseX(int mouseX) {
        float scale = getGuiScale();
        return Math.round(this.width / 2.0F + (mouseX - this.width / 2.0F) / scale);
    }

    @Override
    public int betterWithBags$transformMouseY(int mouseY) {
        float scale = getGuiScale();
        return Math.round(this.height / 2.0F + (mouseY - this.height / 2.0F) / scale);
    }
}
