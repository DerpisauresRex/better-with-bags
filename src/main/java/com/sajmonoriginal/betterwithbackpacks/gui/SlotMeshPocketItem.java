package com.sajmonoriginal.betterwithbackpacks.gui;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotMeshPocketItem extends Slot {
    private final ItemStack backpackStack;
    private final int side;

    public SlotMeshPocketItem(ItemStack backpackStack, int x, int y, int side) {
        super(null, 0, x, y);
        this.backpackStack = backpackStack;
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    @Override
    public ItemStack getItemStack() {
        return ItemBackpack.getMeshPocketItem(backpackStack, side);
    }

    @Override
    public void set(ItemStack stack) {
        ItemBackpack.setMeshPocketItem(backpackStack, stack, side);
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
                ItemBackpack.setMeshPocketItem(backpackStack, current, side);
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
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        return stack.getItem() instanceof ItemBlock;
    }
}
