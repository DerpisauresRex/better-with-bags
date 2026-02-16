package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class ItemBedroll extends Item {

    public ItemBedroll(String name, String textureKey, int id) {
        super(name, textureKey, id);
        this.maxStackSize = 1;
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if (!world.canPlaceInsideBlock(blockX, blockY, blockZ)) {
            blockX += side.getOffsetX();
            blockY += side.getOffsetY();
            blockZ += side.getOffsetZ();
        }

        int direction = player.getHorizontalPlacementDirection(null).getOpposite().getHorizontalIndex();

        int offsetX = 0;
        int offsetZ = 0;
        if (direction == 0) offsetZ = 1;
        if (direction == 1) offsetX = -1;
        if (direction == 2) offsetZ = -1;
        if (direction == 3) offsetX = 1;

        if (world.isAirBlock(blockX, blockY, blockZ)
            && world.isAirBlock(blockX + offsetX, blockY, blockZ + offsetZ)
            && world.canPlaceOnSurfaceOfBlock(blockX, blockY - 1, blockZ)
            && world.canPlaceOnSurfaceOfBlock(blockX + offsetX, blockY - 1, blockZ + offsetZ)
            && itemstack.consumeItem(player)) {

            world.playBlockSoundEffect(player, (float) blockX + 0.5f, (float) blockY + 0.5f, (float) blockZ + 0.5f,
                BackpackBlocks.BEDROLL_BLOCK, EnumBlockSoundEffectType.PLACE);

            world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ,
                BackpackBlocks.BEDROLL_BLOCK.id(), direction);
            world.setBlockAndMetadataWithNotify(blockX + offsetX, blockY, blockZ + offsetZ,
                BackpackBlocks.BEDROLL_BLOCK.id(), direction + 8);

            return true;
        }
        return false;
    }
}
