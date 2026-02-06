package com.sajmonoriginal.betterwithbackpacks;

import com.sajmonoriginal.betterwithbackpacks.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbackpacks.item.BackpackItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;

import java.util.Properties;

public class BetterWithBackpacksMod implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "betterwithbackpacks";
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
        LOGGER.info("Better With Backpacks initialized!");
    }

    @Override
    public void beforeGameStart() {
        new BackpackItems().initializeItems();
        new BackpackBlocks().initializeBlocks();
    }

    @Override
    public void afterGameStart() {
    }
}
