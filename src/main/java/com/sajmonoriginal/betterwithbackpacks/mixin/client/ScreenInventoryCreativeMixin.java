package com.sajmonoriginal.betterwithbackpacks.mixin.client;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import com.sajmonoriginal.betterwithbackpacks.gui.ContainerBackpack;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventoryCreative.class, remap = false)
public abstract class ScreenInventoryCreativeMixin extends ScreenInventory {

    @Unique
    private static final String EXTENSION_TEXTURE_BASE = "/assets/" + BetterWithBackpacksMod.MOD_ID + "/textures/gui/backpack_extension_";
    @Unique
    private static final int STANDARD_HEIGHT = 166;
    @Unique
    private static final int ROW_HEIGHT = 18;
    @Unique
    private static final int EXTENSION_WIDTH = 176;
    @Unique
    private static final int EXTENSION_TITLE_HEIGHT = 14;
    @Unique
    private static final int EXTENSION_BOTTOM_HEIGHT = 7;
    @Unique
    private static final int INV_BOTTOM_BORDER_START = 162;
    @Unique
    private static final int INV_BORDER_COLOR = 0xFF000000;
    @Unique
    private static final int INV_BODY_COLOR = 0xFFC6C6C6;
    @Unique
    private static final int INV_HIGHLIGHT_COLOR = 0xFFFFFFFF;
    @Unique
    private static final int INV_SHADOW_COLOR = 0xFF555555;

    protected ScreenInventoryCreativeMixin(net.minecraft.core.entity.player.Player player) {
        super(player);
    }

    @Unique
    private BackpackTier bwb$creative$getCurrentTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        ItemBackpack backpack = ItemBackpack.getWornBackpack(this.mc.thePlayer);
        return backpack != null ? backpack.getTier() : null;
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void bwb$creative$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        BackpackTier tier = bwb$creative$getCurrentTier();
        if (tier == null) return;

        int rows = tier.getRows();
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int flatY = guiTop + INV_BOTTOM_BORDER_START;
        int flatEnd = guiTop + STANDARD_HEIGHT;
        this.drawRect(guiLeft, flatY, guiLeft + EXTENSION_WIDTH, flatEnd, INV_BODY_COLOR);
        this.drawRect(guiLeft, flatY, guiLeft + 1, flatEnd, INV_BORDER_COLOR);
        this.drawRect(guiLeft + 1, flatY, guiLeft + 3, flatEnd - 2, INV_HIGHLIGHT_COLOR);
        this.drawRect(guiLeft + 173, flatY, guiLeft + 175, flatEnd - 2, INV_SHADOW_COLOR);
        this.drawRect(guiLeft + 175, flatY, guiLeft + EXTENSION_WIDTH, flatEnd, INV_BORDER_COLOR);

        int panelY = guiTop + STANDARD_HEIGHT;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(EXTENSION_TEXTURE_BASE + tier.getName() + ".png").bind();

        int panelHeight = EXTENSION_TITLE_HEIGHT + rows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (tier.hasMeshPocket()) {
            int meshRowY = guiTop + STANDARD_HEIGHT + 15 + (rows - 2) * ROW_HEIGHT;
            this.drawTexturedModalRect(guiLeft + ContainerBackpack.MESH_LEFT_X - 1, meshRowY - 1, 176, 0, 18, 18);
            this.drawTexturedModalRect(guiLeft + ContainerBackpack.MESH_RIGHT_X - 1, meshRowY - 1, 176, 0, 18, 18);
        }
    }
}
