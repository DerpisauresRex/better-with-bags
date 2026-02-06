package com.sajmonoriginal.betterwithbackpacks.block;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;

public class TileEntityBackpack extends TileEntity implements Container {

    private ItemStack[] contents;
    private BackpackTier tier;
    private ItemStack meshPocketLeft;
    private ItemStack meshPocketRight;

    public TileEntityBackpack() {
        this.tier = BackpackTier.SMALL;
        this.contents = new ItemStack[tier.getSlots()];
    }

    public ItemStack getMeshPocketItem() {
        return meshPocketLeft;
    }

    public ItemStack getMeshPocketItem(int side) {
        return side == 0 ? meshPocketLeft : meshPocketRight;
    }

    public void setMeshPocketItem(ItemStack item) {
        this.meshPocketLeft = item;
        setChanged();
    }

    public void setMeshPocketItem(ItemStack item, int side) {
        if (side == 0) {
            this.meshPocketLeft = item;
        } else {
            this.meshPocketRight = item;
        }
        setChanged();
    }

    public BackpackTier getTier() {
        return tier;
    }

    public void setTier(BackpackTier tier) {
        this.tier = tier;
        ItemStack[] oldContents = this.contents;
        this.contents = new ItemStack[tier.getSlots()];
        if (oldContents != null) {
            System.arraycopy(oldContents, 0, this.contents, 0, Math.min(oldContents.length, this.contents.length));
        }
    }

    @Override
    public int getContainerSize() {
        return contents.length;
    }

    @Override
    public ItemStack getItem(int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (contents[slot] != null) {
            if (contents[slot].stackSize <= amount) {
                ItemStack stack = contents[slot];
                contents[slot] = null;
                setChanged();
                return stack;
            }
            ItemStack split = contents[slot].splitStack(amount);
            if (contents[slot].stackSize == 0) {
                contents[slot] = null;
            }
            setChanged();
            return split;
        }
        return null;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        contents[slot] = stack;
        if (stack != null && stack.stackSize > getMaxStackSize()) {
            stack.stackSize = getMaxStackSize();
        }
        setChanged();
    }

    @Override
    public String getNameTranslationKey() {
        return "container.backpack." + tier.getName();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (worldObj.getTileEntity(x, y, z) != this) {
            return false;
        }
        return player.distanceTo(x + 0.5, y + 0.5, z + 0.5) <= 64.0;
    }

    @Override
    public void sortContainer() {
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);

        if (tag.containsKey("BackpackTier")) {
            String tierName = tag.getString("BackpackTier");
            try {
                this.tier = BackpackTier.valueOf(tierName);
            } catch (IllegalArgumentException e) {
                this.tier = BackpackTier.SMALL;
            }
        }

        this.contents = new ItemStack[tier.getSlots()];

        if (tag.containsKey("BackpackInventory")) {
            ListTag itemList = tag.getList("BackpackInventory");
            for (int i = 0; i < itemList.tagCount(); i++) {
                CompoundTag itemTag = (CompoundTag) itemList.tagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < contents.length) {
                    contents[slot] = ItemStack.readItemStackFromNbt(itemTag);
                }
            }
        }

        if (tag.containsKey("MeshPocketLeft")) {
            CompoundTag meshTag = tag.getCompound("MeshPocketLeft");
            if (meshTag.containsKey("id")) {
                this.meshPocketLeft = ItemStack.readItemStackFromNbt(meshTag);
            }
        } else if (tag.containsKey("MeshPocketItem")) {
            CompoundTag meshTag = tag.getCompound("MeshPocketItem");
            if (meshTag.containsKey("id")) {
                this.meshPocketLeft = ItemStack.readItemStackFromNbt(meshTag);
            }
        }
        if (tag.containsKey("MeshPocketRight")) {
            CompoundTag meshTag = tag.getCompound("MeshPocketRight");
            if (meshTag.containsKey("id")) {
                this.meshPocketRight = ItemStack.readItemStackFromNbt(meshTag);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag tag) {
        super.writeToNBT(tag);

        tag.putString("BackpackTier", tier.name());

        ListTag itemList = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                contents[i].writeToNBT(itemTag);
                itemList.addTag(itemTag);
            }
        }
        tag.put("BackpackInventory", itemList);

        CompoundTag meshLeftTag = new CompoundTag();
        if (meshPocketLeft != null) {
            meshPocketLeft.writeToNBT(meshLeftTag);
        }
        tag.putCompound("MeshPocketLeft", meshLeftTag);

        CompoundTag meshRightTag = new CompoundTag();
        if (meshPocketRight != null) {
            meshPocketRight.writeToNBT(meshRightTag);
        }
        tag.putCompound("MeshPocketRight", meshRightTag);
    }

    @Override
    public void dropContents(World world, int x, int y, int z) {
        // Contents are preserved in the dropped item, not scattered
        // So we intentionally do nothing here
    }

    @Override
    public boolean canBeCarried(World world, net.minecraft.core.entity.Entity entity) {
        return false;
    }
}
