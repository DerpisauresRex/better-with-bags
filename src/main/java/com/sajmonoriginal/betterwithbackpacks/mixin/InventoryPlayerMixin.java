package com.sajmonoriginal.betterwithbackpacks.mixin;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class InventoryPlayerMixin {
    @Shadow
    public Player player;

    @Shadow
    public ItemStack[] armorInventory;

    @Shadow
    protected int currentItem;

    @Shadow
    protected int hotbarOffset;

    @Shadow
    public abstract boolean currentItemLocked();

    @Unique
    private ItemStack bwb$getBackpackStack() {
        ItemStack chestSlot = armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return chestSlot;
        }
        return null;
    }

    @Inject(method = "dropAllItems", at = @At("HEAD"))
    private void dropBackpackContentsOnDeath(CallbackInfo ci) {
        ItemStack chestSlot = armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            ItemStack[] contents = ItemBackpack.getBackpackContents(chestSlot);
            if (contents != null) {
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null) {
                        this.player.dropPlayerItem(contents[i]);
                        contents[i] = null;
                    }
                }
                ItemBackpack.setBackpackContents(chestSlot, contents);
            }
        }
    }
}
