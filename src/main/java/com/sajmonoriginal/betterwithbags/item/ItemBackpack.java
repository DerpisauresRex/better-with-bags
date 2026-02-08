package com.sajmonoriginal.betterwithbags.item;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ItemBackpack extends Item implements IArmorItem {

    private final BackpackTier tier;

    public ItemBackpack(String name, String textureKey, int id, BackpackTier tier) {
        super(name, textureKey, id);
        this.tier = tier;
        this.maxStackSize = 1;
    }

    public BackpackTier getTier() {
        return tier;
    }

    public int getCapacity() {
        return tier.getSlots();
    }

    public int getRows() {
        return tier.getRows();
    }

    @Override
    public ItemStack onUseItem(ItemStack itemStack, World world, Player player) {
        if (!player.isSneaking()) {
            openBackpackScreen(player, itemStack);
        }
        return itemStack;
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int x, int y, int z, Side side, double xOffset, double yOffset) {
        if (!player.isSneaking()) {
            openBackpackScreen(player, itemstack);
            return true;
        }

        int placeX = x + side.getOffsetX();
        int placeY = y + side.getOffsetY();
        int placeZ = z + side.getOffsetZ();

        if (!world.isAirBlock(placeX, placeY, placeZ)) {
            return false;
        }

        if (world.isClientSide) {
            return true;
        }

        int blockId = BackpackBlocks.BACKPACK_BLOCK.id();
        Direction facing = player.getHorizontalPlacementDirection(side).getOpposite();
        world.setBlockAndMetadataWithNotify(placeX, placeY, placeZ, blockId, facing.getId());

        TileEntity te = world.getTileEntity(placeX, placeY, placeZ);
        if (te instanceof TileEntityBackpack) {
            TileEntityBackpack backpackTE = (TileEntityBackpack) te;
            backpackTE.setTier(this.tier);

            ItemStack[] contents = getBackpackContents(itemstack);
            if (contents != null) {
                for (int i = 0; i < contents.length; i++) {
                    backpackTE.setItem(i, contents[i]);
                }
            }
        }

        itemstack.stackSize--;
        return true;
    }

    @net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
    private void openBackpackScreen(Player player, ItemStack backpackStack) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        mc.displayScreen(new com.sajmonoriginal.betterwithbags.gui.ScreenBackpack(player.inventory, backpackStack));
    }

    @Override
    public ArmorMaterial getArmorMaterial() {
        return BackpackArmorMaterial.BACKPACK;
    }

    @Override
    public int armorPieceProtection() {
        return 0;
    }

    @Override
    public int getArmorPiece() {
        return 2;
    }

    public static boolean isWearingBackpack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        return chestSlot != null && chestSlot.getItem() instanceof ItemBackpack;
    }

    public static ItemBackpack getWornBackpack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return (ItemBackpack) chestSlot.getItem();
        }
        return null;
    }

    public static ItemStack getWornBackpackStack(Player player) {
        ItemStack chestSlot = player.inventory.armorInventory[2];
        if (chestSlot != null && chestSlot.getItem() instanceof ItemBackpack) {
            return chestSlot;
        }
        return null;
    }

    public static BackpackTier getWornTier(Player player) {
        ItemBackpack backpack = getWornBackpack(player);
        return backpack != null ? backpack.getTier() : null;
    }

    public static ItemStack[] getBackpackContents(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return null;
        }

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        int capacity = backpack.getCapacity();
        ItemStack[] contents = new ItemStack[capacity];

        CompoundTag tag = backpackStack.getData();
        if (tag.containsKey("BackpackInventory")) {
            ListTag itemList = tag.getList("BackpackInventory");
            for (int i = 0; i < itemList.tagCount(); i++) {
                CompoundTag itemTag = (CompoundTag) itemList.tagAt(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot < capacity) {
                    contents[slot] = ItemStack.readItemStackFromNbt(itemTag);
                }
            }
        }

        return contents;
    }

    public static void setBackpackContents(ItemStack backpackStack, ItemStack[] contents) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return;
        }

        CompoundTag tag = backpackStack.getData();

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
    }

    public static void setBackpackSlot(ItemStack backpackStack, int slot, ItemStack item) {
        ItemStack[] contents = getBackpackContents(backpackStack);
        if (contents != null && slot >= 0 && slot < contents.length) {
            contents[slot] = item;
            setBackpackContents(backpackStack, contents);
        }
    }

    public static boolean hasItems(ItemStack backpackStack) {
        ItemStack[] contents = getBackpackContents(backpackStack);
        if (contents == null) return false;
        for (ItemStack item : contents) {
            if (item != null) return true;
        }
        return false;
    }

    public static int getOccupiedSlotCount(ItemStack backpackStack) {
        if (backpackStack == null || !(backpackStack.getItem() instanceof ItemBackpack)) {
            return 0;
        }
        CompoundTag tag = backpackStack.getData();
        if (tag.containsKey("BackpackInventory")) {
            return tag.getList("BackpackInventory").tagCount();
        }
        return 0;
    }
}
