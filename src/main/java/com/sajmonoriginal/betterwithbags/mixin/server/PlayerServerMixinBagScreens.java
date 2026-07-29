package com.sajmonoriginal.betterwithbags.mixin.server;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.gui.BagScreens;
import com.sajmonoriginal.betterwithbags.gui.ContainerBackpack;
import com.sajmonoriginal.betterwithbags.gui.ContainerBackpackBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketContainerOpen;
import net.minecraft.server.entity.player.PlayerServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.SERVER)
@Mixin(value = PlayerServer.class, remap = false)
public abstract class PlayerServerMixinBagScreens implements BagScreens {
    @Shadow
    private int currentWindowId;

    @Shadow
    protected abstract void getNextWindowId();

    @Override
    public void betterWithBags$displayBackpackScreen(ItemStack backpackStack) {
        this.getNextWindowId();
        PlayerServer playerServer = (PlayerServer) (Object) this;
        playerServer.playerNetServerHandler.sendPacket(
            new PacketContainerOpen(this.currentWindowId,
                BetterWithBagsMod.BACKPACK_SCREEN_ID,
                "container.backpack",
                0)
        );
        playerServer.containerMenu.onCraftGuiClosed(playerServer);
        playerServer.containerMenu = new ContainerBackpack(playerServer.inventory, backpackStack);
        playerServer.containerMenu.containerId = this.currentWindowId;
        playerServer.containerMenu.addSlotListener(playerServer);
    }

    @Override
    public void betterWithBags$displayBackpackBlockScreen(TileEntityBackpack tileEntity) {
        this.getNextWindowId();
        PlayerServer playerServer = (PlayerServer) (Object) this;
        playerServer.playerNetServerHandler.sendPacket(
            new PacketContainerOpen(this.currentWindowId,
                BetterWithBagsMod.BACKPACK_BLOCK_SCREEN_ID,
                tileEntity.getNameTranslationKey(),
                tileEntity.getTier().ordinal())
        );
        playerServer.containerMenu.onCraftGuiClosed(playerServer);
        playerServer.containerMenu = new ContainerBackpackBlock(playerServer.inventory, tileEntity);
        playerServer.containerMenu.containerId = this.currentWindowId;
        playerServer.containerMenu.addSlotListener(playerServer);
    }
}
