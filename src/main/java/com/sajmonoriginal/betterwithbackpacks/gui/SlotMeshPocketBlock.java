package com.sajmonoriginal.betterwithbackpacks.gui;

import com.sajmonoriginal.betterwithbackpacks.block.TileEntityBackpack;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotMeshPocketBlock extends Slot {
    private final TileEntityBackpack tileEntity;
    private final int side;

    public SlotMeshPocketBlock(TileEntityBackpack tileEntity, int x, int y, int side) {
        super(tileEntity, 0, x, y);
        this.tileEntity = tileEntity;
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    @Override
    public ItemStack getItemStack() {
        return tileEntity.getMeshPocketItem(side);
    }

    @Override
    public void set(ItemStack stack) {
        tileEntity.setMeshPocketItem(stack, side);
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
            result = current;
            tileEntity.setMeshPocketItem(null, side);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                tileEntity.setMeshPocketItem(null, side);
            } else {
                tileEntity.setMeshPocketItem(current, side);
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
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack == null) return false;
        return stack.getItem() instanceof ItemBlock;
    }
}
