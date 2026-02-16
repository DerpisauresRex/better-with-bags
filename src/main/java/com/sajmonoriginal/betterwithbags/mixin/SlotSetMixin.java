package com.sajmonoriginal.betterwithbags.mixin;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Slot.class, remap = false)
public abstract class SlotSetMixin {
    @Shadow
    @Final
    public Container container;

    @Shadow
    @Final
    public int slot;

    @Shadow
    public abstract void setChanged();

    /**
     * @author BetterWithBags
     * @reason Prevent NPE when ItemStack has unregistered item ID (removed mod items)
     */
    @Overwrite
    public void set(ItemStack itemstack) {
        this.container.setItem(this.slot, itemstack);
        this.setChanged();
        if (this.container instanceof ContainerInventory) {
            ContainerInventory inventory = (ContainerInventory) this.container;
            if (itemstack != null) {
                Item item = Item.itemsList[itemstack.itemID];
                if (item != null) {
                    inventory.player.addStat(item.getStat("stat_picked_up"), 1);
                }
            }
        }
    }
}
