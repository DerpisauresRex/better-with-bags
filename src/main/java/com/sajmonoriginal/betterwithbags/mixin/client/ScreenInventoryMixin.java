package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventory.class, remap = false)
public abstract class ScreenInventoryMixin extends ScreenContainerAbstract {

    @Unique
    private static final String EXTENSION_TEXTURE_BASE = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/gui/backpack_extension_";
    @Unique
    private static final int STANDARD_HEIGHT = 166;
    @Unique
    private static final int ROW_HEIGHT = 18;
    @Unique
    private static final int EXTENSION_WIDTH = 176;
    @Unique
    private static final int EXTENSION_OVERLAP = 3;
    @Unique
    private static final int EXTENSION_TITLE_HEIGHT = 14;
    @Unique
    private static final int EXTENSION_BOTTOM_HEIGHT = 7;

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
        backpackMenu.betterWithBags$refreshBackpackSlots();

        if (!bwb$hasBackpack()) {
            this.bwb$backpackRows = 0;
            this.bwb$currentTier = null;
        } else {
            this.bwb$backpackRows = backpackMenu.betterWithBags$getBackpackRows();
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

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        if (this.bwb$backpackRows <= 0 || this.bwb$currentTier == null) {
            return;
        }

        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int panelY = guiTop + STANDARD_HEIGHT - EXTENSION_OVERLAP;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        String extTexture = EXTENSION_TEXTURE_BASE + this.bwb$currentTier.getName() + ".png";
        this.mc.textureManager.loadTexture(extTexture).bind();

        int panelHeight = EXTENSION_OVERLAP + EXTENSION_TITLE_HEIGHT + this.bwb$backpackRows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (this.bwb$currentTier == BackpackTier.LARGE ||
            this.bwb$currentTier == BackpackTier.REINFORCED ||
            this.bwb$currentTier == BackpackTier.NETHER) {
            this.drawTexturedModalRect(guiLeft + 174, panelY + 13, 174, 13, 36, 26);
        }

        String tierName = this.bwb$currentTier.getName();
        tierName = tierName.substring(0, 1).toUpperCase() + tierName.substring(1);
        this.fontRenderer.render(tierName + " Backpack", guiLeft + 8, panelY + EXTENSION_OVERLAP + 4).setColor(0xFFFFFF).call();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
