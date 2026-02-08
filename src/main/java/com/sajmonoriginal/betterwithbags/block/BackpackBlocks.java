package com.sajmonoriginal.betterwithbags.block;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntityDispatcher;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.BlockBuilder;

public class BackpackBlocks {

    public static Block<?> BACKPACK_BLOCK;

    public void initializeBlocks() {
        BACKPACK_BLOCK = new BlockBuilder(BetterWithBagsMod.MOD_ID)
            .setHardness(0.6f)
            .setResistance(1.0f)
            .setBlockSound(BlockSounds.CLOTH)
            .setTileEntity(TileEntityBackpack::new)
            .setUseInternalLight()
            .setLightOpacity(0)
            .build(
                "backpack_block",
                BetterWithBagsMod.blockIdStart,
                BlockLogicBackpack::new
            );

        TileEntityDispatcher.addMapping(
            TileEntityBackpack.class,
            NamespaceID.getPermanent(BetterWithBagsMod.MOD_ID, "backpack")
        );
    }
}
