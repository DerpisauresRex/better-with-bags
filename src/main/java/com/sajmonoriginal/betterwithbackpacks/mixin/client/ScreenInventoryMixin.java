package com.sajmonoriginal.betterwithbackpacks.mixin.client;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import com.sajmonoriginal.betterwithbackpacks.gui.ContainerBackpack;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventory.class, remap = false)
public abstract class ScreenInventoryMixin extends ScreenContainerAbstract {

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

    @Unique
    private int bwb$backpackRows = 0;
    @Unique
    private boolean bwb$lastHadBackpack = false;
    @Unique
    private BackpackTier bwb$lastTier = null;
    @Unique
    private BackpackTier bwb$currentTier = null;

    protected ScreenInventoryMixin(MenuAbstract menu) {
        super(menu);
    }

    @Unique
    private boolean bwb$hasBackpack() {
        if (this.mc == null || this.mc.thePlayer == null) return false;
        return ItemBackpack.isWearingBackpack(this.mc.thePlayer);
    }

    @Unique
    private BackpackTier bwb$getCurrentTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        ItemBackpack backpack = ItemBackpack.getWornBackpack(this.mc.thePlayer);
        return backpack != null ? backpack.getTier() : null;
    }

    @Unique
    private void bwb$refreshBackpack() {
        MenuInventory menu = (MenuInventory) this.inventorySlots;
        BackpackMenuCapable backpackMenu = (BackpackMenuCapable) menu;
        backpackMenu.betterWithBackpacks$refreshBackpackSlots();

        if (!bwb$hasBackpack()) {
            this.bwb$backpackRows = 0;
            this.bwb$currentTier = null;
        } else {
            this.bwb$backpackRows = backpackMenu.betterWithBackpacks$getBackpackRows();
            this.bwb$currentTier = bwb$getCurrentTier();
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwb$adjustHeight(CallbackInfo ci) {
        this.bwb$lastHadBackpack = bwb$hasBackpack();
        this.bwb$lastTier = bwb$getCurrentTier();
        bwb$refreshBackpack();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$checkBackpackChange(CallbackInfo ci) {
        boolean hasBackpackNow = bwb$hasBackpack();
        BackpackTier tierNow = bwb$getCurrentTier();
        if (hasBackpackNow != this.bwb$lastHadBackpack || tierNow != this.bwb$lastTier) {
            this.bwb$lastHadBackpack = hasBackpackNow;
            this.bwb$lastTier = tierNow;
            bwb$refreshBackpack();
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void bwb$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        if (this.bwb$backpackRows <= 0 || this.bwb$currentTier == null) {
            return;
        }

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
        String extTexture = EXTENSION_TEXTURE_BASE + this.bwb$currentTier.getName() + ".png";
        this.mc.textureManager.loadTexture(extTexture).bind();

        int panelHeight = EXTENSION_TITLE_HEIGHT + this.bwb$backpackRows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (this.bwb$currentTier.hasMeshPocket()) {
            int meshRowY = guiTop + STANDARD_HEIGHT + 15 + (this.bwb$backpackRows - 2) * ROW_HEIGHT;
            this.drawTexturedModalRect(guiLeft + ContainerBackpack.MESH_LEFT_X - 1, meshRowY - 1, 176, 0, 18, 18);
            this.drawTexturedModalRect(guiLeft + ContainerBackpack.MESH_RIGHT_X - 1, meshRowY - 1, 176, 0, 18, 18);
        }
    }

    @Inject(method = "drawGuiContainerForegroundLayer", at = @At("TAIL"))
    private void bwb$drawBackpackLabel(CallbackInfo ci) {
        if (this.bwb$backpackRows <= 0 || this.bwb$currentTier == null) {
            return;
        }

        String tierName = this.bwb$currentTier.name().substring(0, 1) + this.bwb$currentTier.name().substring(1).toLowerCase();
        this.font.drawString(tierName + " Backpack", 8, STANDARD_HEIGHT + 4, 0xFFFFFF);
    }
}
