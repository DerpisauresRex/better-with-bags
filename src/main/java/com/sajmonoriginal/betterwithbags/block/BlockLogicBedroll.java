package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.util.BedrollSleepTracker;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.EnumSleepStatus;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicBedroll extends BlockLogicBed {

    public BlockLogicBedroll(Block<?> block) {
        super(block);
        this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);
    }

    @Override
    public boolean onInteracted(World world, TilePosc pos, Player player, Side side, double hitX, double hitY) {
        if (world.isClientSide) return true;
        TilePos tilePos = new TilePos(pos);
        BedrollSleepTracker.markSleepingInBedroll(player);
        EnumSleepStatus status = player.sleepInBedAt(tilePos.x, tilePos.y, tilePos.z);
        if (status != null) {
            BedrollSleepTracker.clearSleepingInBedroll(player);
        }
        return true;
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
        return new ItemStack[]{new ItemStack(BackpackItems.BEDROLL)};
    }
}
