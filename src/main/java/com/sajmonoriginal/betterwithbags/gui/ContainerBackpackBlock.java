package com.sajmonoriginal.betterwithbags.gui;

import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.Collections;
import java.util.List;

public class ContainerBackpackBlock extends MenuAbstract {
    private final TileEntityBackpack tileEntity;
    private final BackpackTier tier;
    private final int rows;
    private final int capacity;

    public ContainerBackpackBlock(ContainerInventory playerInventory, TileEntityBackpack tileEntity) {
        this.tileEntity = tileEntity;
        this.tier = tileEntity.getTier();
        this.rows = tier.getRows();
        this.capacity = tier.getSlots();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = row * 9 + col;
                if (slotIndex < capacity) {
                    this.addSlot(new SlotBackpackItem(tileEntity, slotIndex, 8 + col * 18, 18 + row * 18));
                }
            }
        }

        boolean supportsAttachment = tier == BackpackTier.LARGE || 
                                      tier == BackpackTier.REINFORCED || 
                                      tier == BackpackTier.NETHER;
        if (supportsAttachment) {
            this.addSlot(new SlotAttachmentBlock(tileEntity, 178, 18));
        }

        int playerInvY = 17 + rows * 18 + 22;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, playerInvY + row * 18));
            }
        }

        int hotbarY = playerInvY + 54 + 4;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, hotbarY));
        }
    }

    public int getRows() {
        return rows;
    }

    public BackpackTier getTier() {
        return tier;
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        int attachmentSlot = capacity;
        int playerStart = capacity + 1;
        if (slot.index < capacity) {
            return this.getSlots(slot.index, 1, false);
        }
        if (slot.index == attachmentSlot) {
            return this.getSlots(attachmentSlot, 1, false);
        }
        if (action == InventoryAction.MOVE_ALL) {
            if (slot.index >= playerStart && slot.index < playerStart + 27) {
                return this.getSlots(playerStart, 27, false);
            }
            if (slot.index >= playerStart + 27) {
                return this.getSlots(playerStart + 27, 9, false);
            }
        }
        return this.getSlots(slot.index, 1, false);
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
        int attachmentSlot = capacity;
        int playerStart = capacity + 1;
        if (slot.index < capacity) {
            return this.getSlots(playerStart, 36, false);
        }
        if (slot.index == attachmentSlot) {
            return this.getSlots(playerStart, 36, false);
        }
        if (slot.index >= playerStart) {
            ItemStack stack = slot.getItemStack();
            if (stack != null && stack.getItem() instanceof ItemBackpack) {
                return Collections.emptyList();
            }
            if (stack != null && stack.getItem() instanceof com.sajmonoriginal.betterwithbags.item.ItemBedroll) {
                Slot attSlot = this.getSlot(attachmentSlot);
                if (attSlot != null && attSlot.mayPlace(stack) && !attSlot.hasItem()) {
                    return this.getSlots(attachmentSlot, 1, false);
                }
            }
            return this.getSlots(0, capacity, false);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean stillValid(Player player) {
        return tileEntity.stillValid(player);
    }
}
