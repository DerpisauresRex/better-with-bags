package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.block.TileEntityBackpack;
import com.sajmonoriginal.betterwithbags.block.TileEntityBedroll;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import com.sajmonoriginal.betterwithbags.model.ItemModelBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBackpack;
import com.sajmonoriginal.betterwithbags.model.ItemModelPlacedBedroll;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBackpack;
import com.sajmonoriginal.betterwithbags.render.TileEntityRendererBedroll;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.block.Block;
import turniplabs.halplibe.util.ModelEntrypoint;

import java.io.InputStream;

public class BackpackModels implements ModelEntrypoint {

    private static final String MOD_ID = BetterWithBagsMod.MOD_ID;

    private static void loadGeo(String path) {
        try (InputStream in = BackpackModels.class.getResourceAsStream(path)) {
            if (in != null) {
                EntityGeometryMojangData.Cache.loadModelsFromResource(in);
            } else {
                BetterWithBagsMod.LOGGER.error("Geo resource not found: {}", path);
            }
        } catch (Exception e) {
            BetterWithBagsMod.LOGGER.error("Failed to load geo: {}", path, e);
        }
    }

    public static void loadBwbGeoModels() {
        loadGeo("/assets/betterwithbags/models/entity/backpack.geo.json");
        loadGeo("/assets/betterwithbags/models/entity/bedroll_attachment.geo.json");
    }

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
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

        dispatcher.addDispatch(new ItemModelStandard(BackpackItems.BEDROLL, null)
            .setIcon(MOD_ID + ":item/bedroll"));

        for (BackpackTier tier : BackpackTier.values()) {
            Block<?> block = BackpackBlocks.getBlockForTier(tier);
            dispatcher.addDispatch(new ItemModelPlacedBackpack(block.asItem(), tier));
        }

        dispatcher.addDispatch(new ItemModelPlacedBedroll(BackpackBlocks.BEDROLL_BLOCK.asItem()));
    }

    @Override
    public void initEntityModels(EntityRendererDispatcher dispatcher) {
        loadBwbGeoModels();
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        dispatcher.assignRenderer(TileEntityBackpack.class, new TileEntityRendererBackpack());
        dispatcher.assignRenderer(TileEntityBedroll.class, new TileEntityRendererBedroll());
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
