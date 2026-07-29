package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.player.inventory.container.ContainerInventory;

@Environment(EnvType.CLIENT)
public class ScreenBackpackBlock extends ScreenContainerAbstract {
    private static final String GUI_TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_";

    private final ContainerBackpackBlock container;
    private final int rows;
    private final String guiTexture;

    public ScreenBackpackBlock(ContainerInventory playerInventory, TileEntityBackpack tileEntity) {
        super(new ContainerBackpackBlock(playerInventory, tileEntity));
        this.container = (ContainerBackpackBlock) this.inventorySlots;
        this.rows = container.getRows();
        BackpackTier tier = container.getTier();
        boolean hasAttachmentSlot = tier == BackpackTier.LARGE || tier == BackpackTier.REINFORCED || tier == BackpackTier.NETHER;
        this.xSize = hasAttachmentSlot ? 194 : 176;
        this.ySize = 121 + rows * 18;
        this.guiTexture = GUI_TEXTURE_BASE + tier.getName() + ".png";
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks) {
        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(guiTexture).bind();

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(guiLeft, guiTop + rows * 18 + 17, 0, 118, 176, 103);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, rows * 18 + 17 + 7);

        BackpackTier tier = container.getTier();
        if (tier == BackpackTier.LARGE || tier == BackpackTier.REINFORCED || tier == BackpackTier.NETHER) {
            this.drawTexturedModalRect(guiLeft + 174, guiTop + 13, 174, 13, 25, 26);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer() {
        BackpackTier tier = container.getTier();
        String tierName = tier.name().substring(0, 1) + tier.name().substring(1).toLowerCase();
        this.fontRenderer.render(tierName + " Backpack", 8, 6).setColor(0xFFFFFF).call();
        this.fontRenderer.render("Inventory", 8, this.ySize - 96 + 2).setColor(0x404040).call();
    }
}
