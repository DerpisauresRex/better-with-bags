package com.sajmonoriginal.betterwithbackpacks.gui;

import com.sajmonoriginal.betterwithbackpacks.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.Collections;
import java.util.List;

public class ContainerBackpackBlock extends MenuAbstract {
    public static final int MESH_LEFT_X = -10;
    public static final int MESH_RIGHT_X = 170;

    private final TileEntityBackpack tileEntity;
    private final BackpackTier tier;
    private final int rows;
    private final int capacity;
    private final boolean hasMesh;
    private int meshLeftSlotIndex = -1;
    private int meshRightSlotIndex = -1;

    public ContainerBackpackBlock(ContainerInventory playerInventory, TileEntityBackpack tileEntity) {
        this.tileEntity = tileEntity;
        this.tier = tileEntity.getTier();
        this.rows = tier.getRows();
        this.capacity = tier.getSlots();
        this.hasMesh = tier.hasMeshPocket();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = row * 9 + col;
                if (slotIndex < capacity) {
                    this.addSlot(new Slot(tileEntity, slotIndex, 8 + col * 18, 18 + row * 18));
                }
            }
        }

        if (hasMesh) {
            int meshY = 18 + (rows - 2) * 18;
            this.meshLeftSlotIndex = this.slots.size();
            this.addSlot(new SlotMeshPocketBlock(tileEntity, MESH_LEFT_X, meshY, 0));
            this.meshRightSlotIndex = this.slots.size();
            this.addSlot(new SlotMeshPocketBlock(tileEntity, MESH_RIGHT_X, meshY, 1));
        }

        int playerInvY = 17 + rows * 18 + 14;
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

    public boolean hasMeshPocket() {
        return hasMesh;
    }

    public int getMeshLeftSlotIndex() {
        return meshLeftSlotIndex;
    }

    public int getMeshRightSlotIndex() {
        return meshRightSlotIndex;
    }

    private boolean isMeshSlot(int index) {
        return hasMesh && (index == meshLeftSlotIndex || index == meshRightSlotIndex);
    }

    @Override
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, Player player) {
        int meshCount = hasMesh ? 2 : 0;
        int playerStart = capacity + meshCount;
        if (slot.index < capacity || isMeshSlot(slot.index)) {
            return this.getSlots(slot.index, 1, false);
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
        int meshCount = hasMesh ? 2 : 0;
        int playerStart = capacity + meshCount;
        if (slot.index < capacity || isMeshSlot(slot.index)) {
            return this.getSlots(playerStart, 36, false);
        }
        if (slot.index >= playerStart) {
            ItemStack stack = slot.getItemStack();
            if (stack != null && stack.getItem() instanceof ItemBackpack) {
                return Collections.emptyList();
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
