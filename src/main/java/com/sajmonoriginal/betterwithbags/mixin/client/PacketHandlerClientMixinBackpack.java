package com.sajmonoriginal.betterwithbags.mixin.client;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerOpen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PacketHandlerClient.class, remap = false)
public abstract class PacketHandlerClientMixinBackpack {
    @Final
    @Shadow
    private Minecraft mc;

    @Inject(method = "handleOpenWindow", at = @At("TAIL"))
    private void bwb$handleBackpackScreens(PacketContainerOpen packet, CallbackInfo ci) {
        PlayerLocal playerLocal = this.mc.thePlayer;
        BagScreens playerScreen = (BagScreens) playerLocal;

        if (packet.inventoryType == BetterWithBagsMod.BACKPACK_SCREEN_ID) {
            ItemStack heldItem = playerLocal.getCurrentEquippedItem();
            if (heldItem != null && heldItem.getItem() instanceof ItemBackpack) {
                playerScreen.betterWithBags$displayBackpackScreen(heldItem);
                playerLocal.craftingInventory.containerId = packet.windowId;
            }
        }

        if (packet.inventoryType == BetterWithBagsMod.BACKPACK_BLOCK_SCREEN_ID) {
            BackpackTier tier = BackpackTier.SMALL;
            BackpackTier[] tiers = BackpackTier.values();
            if (packet.slotsCount >= 0 && packet.slotsCount < tiers.length) {
                tier = tiers[packet.slotsCount];
            }
            TileEntityBackpack dummy = new TileEntityBackpack();
            dummy.setTier(tier);
            playerScreen.betterWithBags$displayBackpackBlockScreen(dummy);
            playerLocal.craftingInventory.containerId = packet.windowId;
        }
    }
}
