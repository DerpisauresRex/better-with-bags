package com.sajmonoriginal.betterwithbags.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelEmpty;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.Side;

@Environment(EnvType.CLIENT)
public class BlockModelBag<T extends BlockLogic> extends BlockModelEmpty<T> {
    public BlockModelBag(Block<T> block) { super(block); }

    @Override
    public IconCoordinate getParticleTexture(Side side, int meta) {
        return null;
    }
}
