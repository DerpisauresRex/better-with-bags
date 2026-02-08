package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class ScreenBackpack extends ScreenContainerAbstract {
    private static final String GUI_TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_";

    private final ContainerBackpack container;
    private final int rows;
    private final String guiTexture;

    public ScreenBackpack(ContainerInventory playerInventory, ItemStack backpackStack) {
        super(new ContainerBackpack(playerInventory, backpackStack));
        this.container = (ContainerBackpack) this.inventorySlots;
        this.rows = container.getRows();
        this.ySize = 113 + rows * 18;
        BackpackTier tier = container.getTier();
        this.guiTexture = GUI_TEXTURE_BASE + tier.getName() + ".png";
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(guiTexture).bind();

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, rows * 18 + 17);
        this.drawTexturedModalRect(guiLeft, guiTop + rows * 18 + 17, 0, 126, this.xSize, 96);
    }

    @Override
    public void drawGuiContainerForegroundLayer() {
        String tierName = container.getTier().name().substring(0, 1) + container.getTier().name().substring(1).toLowerCase();
        this.font.drawString(tierName + " Backpack", 8, 6, 0xFFFFFF);
        this.font.drawString("Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }
}
