package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;

public class BackpackInventoryContainer implements Container {
    private final ContainerInventory playerInventory;
    private final int slotIndex;
    private final int capacity;
    private final ItemStack[] contents;

    public BackpackInventoryContainer(ContainerInventory playerInventory, int slotIndex) {
        this.playerInventory = playerInventory;
        this.slotIndex = slotIndex;
        ItemStack backpackStack = playerInventory.mainInventory[slotIndex];
        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        this.capacity = backpack.getCapacity();

        ItemStack[] nbtContents = ItemBackpack.getBackpackContents(backpackStack);
        this.contents = nbtContents != null ? nbtContents : new ItemStack[capacity];
    }

    // Live lookup — server replaces the ItemStack via copyItemStack() in handlePlace after onUseItem returns
    private ItemStack getBackpackStack() {
        return playerInventory.mainInventory[slotIndex];
    }

    @Override
    public int getContainerSize() {
        return capacity;
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < capacity) {
            return contents[slot];
        }
        return null;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot < 0 || slot >= capacity || contents[slot] == null) {
            return null;
        }

        ItemStack result;
        if (contents[slot].stackSize <= amount) {
            result = contents[slot];
            contents[slot] = null;
        } else {
            result = contents[slot].splitStack(amount);
            if (contents[slot].stackSize == 0) {
                contents[slot] = null;
            }
        }
        setChanged();
        return result;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < capacity) {
            contents[slot] = stack;
            setChanged();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.backpack";
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
        ItemStack current = getBackpackStack();
        if (current != null && current.getItem() instanceof ItemBackpack) {
            ItemBackpack.setBackpackContents(current, contents);
        }
    }

    public void flush() {
        ItemStack current = getBackpackStack();
        if (current != null && current.getItem() instanceof ItemBackpack) {
            ItemBackpack.setBackpackContents(current, contents);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void sortContainer() {
    }
}
