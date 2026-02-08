package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.model.ItemModelBackpack;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBackpack;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelEmpty;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class BackpackModels implements ModelEntrypoint {

    private static final String MOD_ID = BetterWithBagsMod.MOD_ID;

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        dispatcher.addDispatch(BackpackBlocks.BACKPACK_BLOCK, new BlockModelEmpty<>(BackpackBlocks.BACKPACK_BLOCK));
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_SMALL, null)
            .setIcon(MOD_ID + ":item/backpack_small"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_MEDIUM, null)
            .setIcon(MOD_ID + ":item/backpack_medium"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_LARGE, null)
            .setIcon(MOD_ID + ":item/backpack_large"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_REINFORCED, null)
            .setIcon(MOD_ID + ":item/backpack_reinforced"));
        dispatcher.addDispatch(new ItemModelBackpack(BackpackItems.BACKPACK_NETHER, null)
            .setIcon(MOD_ID + ":item/backpack_nether"));
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        ModelHelper.setTileEntityModel(TileEntityBackpack.class, TileEntityRendererBackpack::new);
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
