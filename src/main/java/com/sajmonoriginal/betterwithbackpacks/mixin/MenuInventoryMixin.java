package com.sajmonoriginal.betterwithbackpacks.mixin;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import com.sajmonoriginal.betterwithbackpacks.gui.ContainerBackpack;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.mixin.accessor.MenuAbstractAccessor;
import com.sajmonoriginal.betterwithbackpacks.slot.SlotBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.BackpackMenuCapable;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = MenuInventory.class, remap = false)
public abstract class MenuInventoryMixin implements BackpackMenuCapable {
    @Shadow
    public ContainerInventory inventory;

    @Unique
    private static final int STANDARD_HEIGHT = 166;
    @Unique
    private static final int ROW_HEIGHT = 18;
    @Unique
    private static final int HIDDEN_Y = -9999;

    @Unique
    private boolean bwb$hasBackpackSlots = false;

    @Unique
    private int bwb$backpackSlotStartIndex = -1;

    @Unique
    private int bwb$backpackRows = 0;

    @Unique
    private List<SlotBackpack> bwb$backpackSlots = new ArrayList<>();

    @Unique
    private boolean bwb$slotsCreated = false;

    @Override
    public boolean betterWithBackpacks$hasBackpackSlots() {
        return this.bwb$hasBackpackSlots;
    }

    @Override
    public int betterWithBackpacks$getBackpackSlotStartIndex() {
        return this.bwb$backpackSlotStartIndex;
    }

    @Override
    public int betterWithBackpacks$getBackpackRows() {
        return this.bwb$backpackRows;
    }

    @Override
    public void betterWithBackpacks$refreshBackpackSlots() {
        if (inventory == null || inventory.player == null) {
            hideBackpackSlots();
            return;
        }

        ItemStack chestSlot = inventory.player.inventory.armorInventory[2];
        boolean hasBackpack = chestSlot != null && chestSlot.getItem() instanceof ItemBackpack;

        if (!hasBackpack) {
            hideBackpackSlots();
            return;
        }

        ItemBackpack backpack = (ItemBackpack) chestSlot.getItem();
        BackpackTier tier = backpack.getTier();
        int capacity = tier.getSlots();
        int rows = tier.getRows();

        if (!bwb$slotsCreated) {
            createBackpackSlots();
        }

        this.bwb$hasBackpackSlots = true;
        this.bwb$backpackRows = rows;

        for (int i = 0; i < bwb$backpackSlots.size(); i++) {
            SlotBackpack slot = bwb$backpackSlots.get(i);
            if (i < capacity) {
                int row = i / 9;
                int col = i % 9;
                slot.x = 8 + col * 18;
                slot.y = STANDARD_HEIGHT + 15 + row * ROW_HEIGHT;
            } else {
                slot.x = HIDDEN_Y;
                slot.y = HIDDEN_Y;
            }
        }
    }

    @Unique
    private void createBackpackSlots() {
        MenuInventory self = (MenuInventory) (Object) this;
        this.bwb$backpackSlotStartIndex = self.slots.size();

        int maxCapacity = BackpackTier.REINFORCED.getSlots();
        for (int i = 0; i < maxCapacity; i++) {
            SlotBackpack slot = new SlotBackpack(inventory, i, HIDDEN_Y, HIDDEN_Y);
            bwb$backpackSlots.add(slot);
            ((MenuAbstractAccessor) this).invokeAddSlot(slot);
        }

        bwb$slotsCreated = true;
    }

    @Unique
    private void hideBackpackSlots() {
        this.bwb$hasBackpackSlots = false;
        this.bwb$backpackRows = 0;

        for (SlotBackpack slot : bwb$backpackSlots) {
            slot.x = HIDDEN_Y;
            slot.y = HIDDEN_Y;
        }
    }

    @Inject(method = "getTargetSlots", at = @At("HEAD"), cancellable = true)
    private void bwb$addBackpackTargets(InventoryAction action, Slot slot, int button, Player player,
                                         CallbackInfoReturnable<List<Integer>> cir) {
        if (!bwb$hasBackpackSlots || bwb$backpackSlotStartIndex < 0) return;

        int capacity = bwb$backpackRows * 9;
        int slotIndex = slot.index;
        MenuInventory self = (MenuInventory) (Object) this;

        if (slotIndex >= 9 && slotIndex <= 44 && button != 1) {
            if (button == 2 && slot.getItemStack() != null
                && slot.getItemStack().getItem() instanceof IArmorItem) {
                return;
            }
            List<Integer> targets = new ArrayList<>();
            for (int i = 0; i < capacity; i++) {
                targets.add(bwb$backpackSlotStartIndex + i);
            }
            if (slotIndex < 36) {
                targets.addAll(self.getSlots(36, 9, false));
            } else {
                targets.addAll(self.getSlots(9, 27, false));
            }
            cir.setReturnValue(targets);
            return;
        }

        if (slotIndex >= bwb$backpackSlotStartIndex
            && slotIndex < bwb$backpackSlotStartIndex + capacity) {
            cir.setReturnValue(self.getSlots(9, 36, false));
        }
    }
}
