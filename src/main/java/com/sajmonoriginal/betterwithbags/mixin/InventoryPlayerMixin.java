package com.sajmonoriginal.betterwithbags.mixin;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class InventoryPlayerMixin {
    @Shadow
    public Player player;

    @Shadow
    public ItemStack[] armorInventory;

    @Shadow
    private int currentSlot;

    @Shadow
    private int hotbarOffset;

    @Shadow
    public abstract boolean currentSlotLocked();
}
