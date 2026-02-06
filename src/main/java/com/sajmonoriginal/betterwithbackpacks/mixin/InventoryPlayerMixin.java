package com.sajmonoriginal.betterwithbackpacks.mixin;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ContainerInventory.class, remap = false)
public abstract class InventoryPlayerMixin implements MeshHotbarCapable {
    @Shadow
    public Player player;

    @Shadow
    public ItemStack[] armorInventory;

    @Shadow
    protected int currentItem;

    @Shadow
    protected int hotbarOffset;

    @Shadow
    public abstract boolean currentItemLocked();

    @Unique
    private int bwb$meshSelected = 0;

    @Override
    public int betterWithBackpacks$getMeshSelected() {
        return bwb$meshSelected;
    }

    @Override
    public void betterWithBackpacks$setMeshSelected(int side) {
        bwb$meshSelected = side;
    }

    @Unique
    private boolean bwb$hasMeshPocket() {
        ItemStack chestSlot = armorInventory[2];
        if (chestSlot == null || !(chestSlot.getItem() instanceof ItemBackpack)) return false;
        return ((ItemBackpack) chestSlot.getItem()).getTier().hasMeshPocket();
    }

    @Unique
    private ItemStack bwb$getBackpackStack() {
        ItemStack chestSlot = armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return chestSlot;
        }
        return null;
    }

    @Unique
    private ItemStack bwb$trackedMeshItem = null;

    @Unique
    private int bwb$trackedMeshSide = -1;

    @Unique
    private int bwb$trackedOriginalSize = 0;

    @Unique
    private ItemStack bwb$getLiveMeshItem() {
        if (bwb$meshSelected <= 0) return null;
        int side = bwb$meshSelected - 1;

        if (bwb$trackedMeshItem != null && bwb$trackedMeshSide == side) {
            return bwb$trackedMeshItem;
        }

        ItemStack backpack = bwb$getBackpackStack();
        if (backpack == null) return null;

        bwb$trackedMeshItem = ItemBackpack.getMeshPocketItem(backpack, side);
        bwb$trackedMeshSide = side;
        bwb$trackedOriginalSize = bwb$trackedMeshItem != null ? bwb$trackedMeshItem.stackSize : 0;
        return bwb$trackedMeshItem;
    }

    @Override
    public void betterWithBackpacks$syncMeshItem() {
        if (bwb$trackedMeshItem == null || bwb$trackedMeshSide < 0) return;

        if (bwb$trackedMeshItem.stackSize == bwb$trackedOriginalSize) {
            bwb$trackedMeshItem = null;
            bwb$trackedMeshSide = -1;
            return;
        }

        ItemStack backpack = bwb$getBackpackStack();
        if (backpack == null) {
            bwb$trackedMeshItem = null;
            bwb$trackedMeshSide = -1;
            return;
        }

        if (bwb$trackedMeshItem.stackSize <= 0) {
            ItemBackpack.setMeshPocketItem(backpack, null, bwb$trackedMeshSide);
        } else {
            ItemBackpack.setMeshPocketItem(backpack, bwb$trackedMeshItem, bwb$trackedMeshSide);
        }

        bwb$trackedMeshItem = null;
        bwb$trackedMeshSide = -1;
    }

    @Override
    public ItemStack betterWithBackpacks$removeMeshItem(int maxCount) {
        if (bwb$meshSelected <= 0) return null;
        int side = bwb$meshSelected - 1;
        ItemStack backpack = bwb$getBackpackStack();
        if (backpack == null) return null;

        ItemStack meshItem = ItemBackpack.getMeshPocketItem(backpack, side);
        if (meshItem == null) return null;

        ItemStack dropped;
        if (meshItem.stackSize <= maxCount) {
            dropped = meshItem;
            ItemBackpack.setMeshPocketItem(backpack, null, side);
        } else {
            dropped = meshItem.splitStack(maxCount);
            ItemBackpack.setMeshPocketItem(backpack, meshItem, side);
        }

        bwb$trackedMeshItem = null;
        bwb$trackedMeshSide = -1;
        return dropped;
    }

    @Override
    public void betterWithBackpacks$destroyMeshItem() {
        if (bwb$meshSelected <= 0) return;
        int side = bwb$meshSelected - 1;
        ItemStack backpack = bwb$getBackpackStack();
        if (backpack == null) return;

        ItemBackpack.setMeshPocketItem(backpack, null, side);
        bwb$trackedMeshItem = null;
        bwb$trackedMeshSide = -1;
    }

    @Inject(method = "changeCurrentItem", at = @At("HEAD"), cancellable = true)
    private void bwb$extendHotbarScroll(int direction, CallbackInfo ci) {
        if (currentItemLocked()) return;
        if (!bwb$hasMeshPocket()) {
            if (bwb$meshSelected != 0) {
                bwb$meshSelected = 0;
                bwb$trackedMeshItem = null;
                bwb$trackedMeshSide = -1;
            }
            return;
        }

        int dir = direction > 0 ? 1 : (direction < 0 ? -1 : 0);
        if (dir == 0) return;

        boolean forward = dir < 0;
        int hotbarStart = hotbarOffset;
        int hotbarEnd = hotbarOffset + 8;

        if (bwb$meshSelected == 0) {
            int next = currentItem + (forward ? 1 : -1);
            if (forward && next > hotbarEnd) {
                bwb$meshSelected = 2;
                bwb$trackedMeshItem = null;
                bwb$trackedMeshSide = -1;
                ci.cancel();
                return;
            }
            if (!forward && next < hotbarStart) {
                bwb$meshSelected = 1;
                bwb$trackedMeshItem = null;
                bwb$trackedMeshSide = -1;
                ci.cancel();
                return;
            }
            return;
        }

        ci.cancel();

        if (bwb$meshSelected == 2) {
            if (forward) {
                bwb$meshSelected = 1;
            } else {
                bwb$meshSelected = 0;
                currentItem = hotbarEnd;
            }
        } else if (bwb$meshSelected == 1) {
            if (forward) {
                bwb$meshSelected = 0;
                currentItem = hotbarStart;
            } else {
                bwb$meshSelected = 2;
            }
        }

        bwb$trackedMeshItem = null;
        bwb$trackedMeshSide = -1;
    }

    @Inject(method = "getCurrentItem", at = @At("HEAD"), cancellable = true)
    private void bwb$getMeshCurrentItem(CallbackInfoReturnable<ItemStack> cir) {
        if (bwb$meshSelected > 0) {
            ItemStack live = bwb$getLiveMeshItem();
            cir.setReturnValue(live);
            return;
        }
    }

    @Inject(method = "dropAllItems", at = @At("HEAD"))
    private void dropBackpackContentsOnDeath(CallbackInfo ci) {
        ItemStack chestSlot = armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            ItemStack[] contents = ItemBackpack.getBackpackContents(chestSlot);
            if (contents != null) {
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null) {
                        this.player.dropPlayerItem(contents[i]);
                        contents[i] = null;
                    }
                }
                ItemBackpack.setBackpackContents(chestSlot, contents);
            }

            for (int side = 0; side < 2; side++) {
                ItemStack meshItem = ItemBackpack.getMeshPocketItem(chestSlot, side);
                if (meshItem != null) {
                    this.player.dropPlayerItem(meshItem);
                    ItemBackpack.setMeshPocketItem(chestSlot, null, side);
                }
            }
        }
    }
}
