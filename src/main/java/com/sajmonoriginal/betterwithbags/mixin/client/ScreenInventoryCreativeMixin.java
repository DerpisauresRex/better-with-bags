package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import com.sajmonoriginal.betterwithbags.util.BackpackMenuCapable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.gui.container.ScreenInventoryCreative;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ScreenInventoryCreative.class, remap = false)
public abstract class ScreenInventoryCreativeMixin extends ScreenInventory {

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
    private boolean bwb$creative$lastHadBackpack = false;
    @Unique
    private BackpackTier bwb$creative$lastTier = null;

    protected ScreenInventoryCreativeMixin(net.minecraft.core.entity.player.Player player) {
        super(player);
    }

    @Unique
    private boolean bwb$creative$hasBackpack() {
        if (this.mc == null || this.mc.thePlayer == null) return false;
        return ItemBackpack.isWearingBackpack(this.mc.thePlayer);
    }

    @Unique
    private BackpackTier bwb$creative$getCurrentTier() {
        if (this.mc == null || this.mc.thePlayer == null) return null;
        ItemBackpack backpack = ItemBackpack.getWornBackpack(this.mc.thePlayer);
        return backpack != null ? backpack.getTier() : null;
    }

    @Unique
    private void bwb$creative$refreshBackpack() {
        if (this.inventorySlots instanceof MenuInventory) {
            MenuInventory menu = (MenuInventory) this.inventorySlots;
            BackpackMenuCapable backpackMenu = (BackpackMenuCapable) menu;
            backpackMenu.betterWithBags$refreshBackpackSlots();
        }
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void bwb$creative$onInit(CallbackInfo ci) {
        this.bwb$creative$lastHadBackpack = bwb$creative$hasBackpack();
        this.bwb$creative$lastTier = bwb$creative$getCurrentTier();
        bwb$creative$refreshBackpack();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void bwb$creative$onTick(CallbackInfo ci) {
        boolean hasBackpackNow = bwb$creative$hasBackpack();
        BackpackTier tierNow = bwb$creative$getCurrentTier();
        if (hasBackpackNow != this.bwb$creative$lastHadBackpack || tierNow != this.bwb$creative$lastTier) {
            this.bwb$creative$lastHadBackpack = hasBackpackNow;
            this.bwb$creative$lastTier = tierNow;
            bwb$creative$refreshBackpack();
        }
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("HEAD"))
    private void bwb$creative$drawBackpackPanel(float partialTicks, CallbackInfo ci) {
        BackpackTier tier = bwb$creative$getCurrentTier();
        if (tier == null) return;

        int rows = tier.getRows();
        int guiLeft = (this.width - this.xSize) / 2;
        int guiTop = (this.height - this.ySize) / 2;

        int panelY = guiTop + STANDARD_HEIGHT - EXTENSION_OVERLAP;

        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.loadTexture(EXTENSION_TEXTURE_BASE + tier.getName() + ".png").bind();

        int panelHeight = EXTENSION_OVERLAP + EXTENSION_TITLE_HEIGHT + rows * ROW_HEIGHT + EXTENSION_BOTTOM_HEIGHT;
        this.drawTexturedModalRect(guiLeft, panelY, 0, 0, EXTENSION_WIDTH, panelHeight);

        if (tier == BackpackTier.LARGE || tier == BackpackTier.REINFORCED || tier == BackpackTier.NETHER) {
            this.drawTexturedModalRect(guiLeft + 174, panelY + 13, 174, 13, 36, 26);
        }

        String tierName = tier.getName();
        tierName = tierName.substring(0, 1).toUpperCase() + tierName.substring(1);
        this.fontRenderer.render(tierName + " Backpack", guiLeft + 8, panelY + EXTENSION_OVERLAP + 4).setColor(0xFFFFFF).call();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
