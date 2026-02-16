package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.Properties;

public class BetterWithBagsMod implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "betterwithbags";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int itemIdStart;
    public static int blockIdStart;

    static {
        Properties prop = new Properties();
        prop.setProperty("starting_item_id", "29000");
        prop.setProperty("starting_block_id", "4000");
        ConfigHandler config = new ConfigHandler(MOD_ID, prop);
        itemIdStart = config.getInt("starting_item_id");
        blockIdStart = config.getInt("starting_block_id");
        config.updateConfig();
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Better With Bags initialized!");
    }

    public static final int BACKPACK_SCREEN_ID = 20;
    public static final int BACKPACK_BLOCK_SCREEN_ID = 21;

    @Override
    public void beforeGameStart() {
        LOGGER.info("beforeGameStart() called - registering items and blocks");
        new BackpackItems().initializeItems();
        new BackpackBlocks().initializeBlocks();
        LOGGER.info("Registered items starting at ID {} and blocks starting at ID {}", itemIdStart, blockIdStart);
    }

    @Override
    public void afterGameStart() {
    }
}
