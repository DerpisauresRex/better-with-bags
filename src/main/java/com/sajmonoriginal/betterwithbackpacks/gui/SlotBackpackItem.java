package com.sajmonoriginal.betterwithbackpacks.gui;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotBackpackItem extends Slot {
    private final ItemStack backpackStack;
    private final int backpackSlotIndex;

    public SlotBackpackItem(ItemStack backpackStack, int slotIndex, int x, int y) {
        super(null, slotIndex, x, y);
        this.backpackStack = backpackStack;
        this.backpackSlotIndex = slotIndex;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack[] contents = ItemBackpack.getBackpackContents(backpackStack);
        if (contents != null && backpackSlotIndex >= 0 && backpackSlotIndex < contents.length) {
            return contents[backpackSlotIndex];
        }
        return null;
    }

    @Override
    public void set(ItemStack stack) {
        ItemBackpack.setBackpackSlot(backpackStack, backpackSlotIndex, stack);
        this.setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        ItemStack current = getItemStack();
        if (current == null) {
            return null;
        }

        ItemStack result;
        if (current.stackSize <= amount) {
            result = current.copy();
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                ItemBackpack.setBackpackSlot(backpackStack, backpackSlotIndex, current);
            }
        }
        this.setChanged();
        return result;
    }

    @Override
    public boolean hasItem() {
        return getItemStack() != null;
    }

    @Override
    public void setChanged() {
        // No-op: backpack data is stored in ItemStack NBT, not a Container
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        return !(stack.getItem() instanceof ItemBackpack);
    }
}
