package com.sajmonoriginal.betterwithbags.block;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.BlockLogicBed;
import net.minecraft.core.block.entity.TileEntity;

public class TileEntityBedroll extends TileEntity {

    public boolean isFoot() {
        return !BlockLogicBed.IS_HEAD.bool(getBlockMeta());
    }

    public int getDirection() {
        return BlockLogicBed.DIRECTION.get(getBlockMeta());
    }

    @Override
    public void readAdditionalData(CompoundTag tag) {
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
    }
}
