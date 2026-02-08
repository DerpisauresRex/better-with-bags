package com.sajmonoriginal.betterwithbags.slot;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotBackpack extends Slot {
    private final ContainerInventory playerInventory;
    private final int backpackSlotIndex;

    public SlotBackpack(ContainerInventory inventory, int backpackSlot, int x, int y) {
        super(inventory, 0, x, y);
        this.playerInventory = inventory;
        this.backpackSlotIndex = backpackSlot;
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

        ItemStack[] contents = ItemBackpack.getBackpackContents(backpackStack);
        if (contents != null && backpackSlotIndex >= 0 && backpackSlotIndex < contents.length) {
            return contents[backpackSlotIndex];
        }
        return null;
    }

    @Override
    public void set(ItemStack stack) {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return;

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
            result = current;
            set(null);
        } else {
            result = current.splitStack(amount);
            if (current.stackSize == 0) {
                set(null);
            } else {
                ItemStack backpackStack = getBackpackStack();
                if (backpackStack != null) {
                    ItemBackpack.setBackpackSlot(backpackStack, backpackSlotIndex, current);
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
        if (stack.getItem() instanceof ItemBackpack) return false;
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return false;
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        return backpackSlotIndex < backpack.getCapacity();
    }

    public boolean isActive() {
        ItemStack backpackStack = getBackpackStack();
        if (backpackStack == null) return false;
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        return backpackSlotIndex < backpack.getCapacity();
    }
}
