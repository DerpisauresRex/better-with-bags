package com.sajmonoriginal.betterwithbackpacks.slot;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotMeshPocket extends Slot {
    private final ContainerInventory playerInventory;
    private final int side;

    public SlotMeshPocket(ContainerInventory inventory, int x, int y, int side) {
        super(inventory, 0, x, y);
        this.playerInventory = inventory;
        this.side = side;
    }

    public int getSide() {
        return side;
    }

    private ItemStack getBackpackStack() {
        Player player = playerInventory.player;
        if (player == null) return null;
        return ItemBackpack.getWornBackpackStack(player);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return null;
        return ItemBackpack.getMeshPocketItem(backpackStack, side);
    }

    @Override
    public void set(ItemStack stack) {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return;
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
            result = current;
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                ItemStack backpackStack = getBackpackStack();
                if (backpackStack != null) {
                    ItemBackpack.setMeshPocketItem(backpackStack, current, side);
                }
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
        if (getBackpackStack() == null) return false;
        return stack.getItem() instanceof ItemBlock;
    }

    public boolean isActive() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return false;
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        return backpack.getTier().hasMeshPocket();
    }
}
