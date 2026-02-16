package com.sajmonoriginal.betterwithbags.block;

import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.block.entity.TileEntity;

public class TileEntityBedroll extends TileEntity {

    public boolean isFoot() {
        return BlockLogicBed.isBlockFootOfBed(getBlockMeta());
    }

    public int getDirection() {
        return BlockLogicBed.getDirection(getBlockMeta());
    }
}
