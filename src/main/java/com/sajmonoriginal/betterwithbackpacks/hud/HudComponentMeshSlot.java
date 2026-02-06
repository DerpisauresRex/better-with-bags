package com.sajmonoriginal.betterwithbackpacks.hud;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.HudComponentMovable;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class HudComponentMeshSlot extends HudComponentMovable {

    private static final int SLOT_SIZE = 22;
    private static final int HOTBAR_SELECTOR_OFFSET = 5;
    private static final String HUD_TEXTURE_BASE = "/assets/betterwithbackpacks/textures/gui/hud_mesh_";

    private final int slotIndex;
    private ItemElement itemElement;

    public HudComponentMeshSlot(String key, int slotIndex, Layout layout) {
        super(key, SLOT_SIZE, SLOT_SIZE, layout);
        this.slotIndex = slotIndex;
    }

    @Override
    public int getAnchorX(net.minecraft.client.gui.hud.component.ComponentAnchor anchor) {
        int base = super.getAnchorX(anchor);
        if (slotIndex == 0) {
            base += HOTBAR_SELECTOR_OFFSET;
        }
        return base;
    }

    @Override
    public boolean isVisible(Minecraft mc) {
        if (mc.thePlayer == null || !mc.gameSettings.immersiveMode.drawHotbar()) {
            return false;
        }
        ItemStack chestSlot = mc.thePlayer.inventory.armorInventory[2];
        if (chestSlot == null || !(chestSlot.getItem() instanceof ItemBackpack)) {
            return false;
        }
        return ((ItemBackpack) chestSlot.getItem()).getTier().hasMeshPocket();
    }

    @Override
    public void render(Minecraft mc, HudIngame hud, int screenWidth, int screenHeight, float partialTicks) {
        ItemStack chestSlot = mc.thePlayer.inventory.armorInventory[2];
        ItemBackpack backpack = (ItemBackpack) chestSlot.getItem();

        ItemStack meshItem = ItemBackpack.getMeshPocketItem(chestSlot, slotIndex);

        int x = getLayout().getComponentX(mc, this, screenWidth);
        int y = getLayout().getComponentY(mc, this, screenHeight);

        if (this.itemElement == null) {
            this.itemElement = new ItemElement(mc);
        }

        int meshSelected = ((MeshHotbarCapable) mc.thePlayer.inventory).betterWithBackpacks$getMeshSelected();
        String hudTexture = HUD_TEXTURE_BASE + backpack.getTier().getName() + ".png";

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.textureManager.loadTexture(hudTexture).bind();
        hud.drawTexturedModalRect(x, y, 0, 0, SLOT_SIZE, SLOT_SIZE);

        int expectedSelected = slotIndex + 1;
        if (meshSelected == expectedSelected) {
            mc.textureManager.loadTexture(hudTexture).bind();
            hud.drawTexturedModalRect(x - 1, y - 1, 0, 22, 24, 24);
        }

        if (meshItem != null) {
            this.itemElement.render(meshItem, x + 3, y + 3);
        }
    }

    @Override
    public void renderPreview(Minecraft mc, Gui gui, Layout layout, int screenWidth, int screenHeight) {
        int x = layout.getComponentX(mc, this, screenWidth);
        int y = layout.getComponentY(mc, this, screenHeight);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.textureManager.loadTexture(HUD_TEXTURE_BASE + "reinforced.png").bind();
        gui.drawTexturedModalRect(x, y, 0, 0, SLOT_SIZE, SLOT_SIZE);

        GL11.glPopAttrib();
    }
}
