package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.dependency.Key;
import turniplabs.halplibe.util.toml.Toml;

//* Pulls in information from backpack tiers to configure their penalties via config*/
import com.sajmonoriginal.betterwithbags.item.BackpackTier;

public class BetterWithBagsMod implements ModInitializer {
    public static final String MOD_ID = HalpLibe.registerMod("betterwithbags");
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int itemIdStart;
    public static int blockIdStart;

    //* Toggle Penalties and Show Content*/
    public static boolean oneBagLimit;
    public static boolean carriedBagPenalty;
    public static boolean wornBagHasPenalty;
    public static boolean showContentsWhenCarried;


    //* Toggle Penalty Percentages */
    public static int smallPenaltyPercent;
    public static int mediumPenaltyPercent;
    public static int largePenaltyPercent;
    public static int reinforcedPenaltyPercent;
    public static int netherPenaltyPercent;


    public static float getMaxSpeedPenalty(BackpackTier tier) {
    int percent;
    switch (tier) {
        case SMALL: percent = smallPenaltyPercent; break;
        case MEDIUM: percent = mediumPenaltyPercent; break;
        case LARGE: percent = largePenaltyPercent; break;
        case REINFORCED: percent = reinforcedPenaltyPercent; break;
        case NETHER: percent = netherPenaltyPercent; break;
        default: percent = 0;
    }
    return Math.max(0, Math.min(100, percent)) / 100.0f;
}



    static {
        Toml defaults = new Toml("Better With Bags");
        defaults.addEntry("starting_item_id", "First item ID", 29000);
        defaults.addEntry("starting_block_id", "First block ID", 4000);
        //** Default Toggle Penalties and Show Content Written to Config*/
        defaults.addEntry("one_bag_limit", "Only one backpack may be carried at a time", true);
        defaults.addEntry("carried_bag_penalty", "A backpack in the inventory (not worn) slows you by how full it is", true);
        defaults.addEntry("worn_bag_has_penalty", "A worn backpack slows you by how full it is", true);
        defaults.addEntry("show_contents_when_carried", "Show an unworn backpack's contents in the inventory screen", false);

        //** Default Toggle Penalty Percentages Written to Config*/
        defaults.addEntry("small_bag_max_speed_penalty", "Slowdown in percent (0-100) when a small bag is completely full", 0);
        defaults.addEntry("medium_bag_max_speed_penalty", "Slowdown in percent (0-100) when a medium bag is completely full", 0);
        defaults.addEntry("large_bag_max_speed_penalty", "Slowdown in percent (0-100) when a large bag is completely full", 10);
        defaults.addEntry("reinforced_bag_max_speed_penalty", "Slowdown in percent (0-100) when a reinforced bag is completely full", 15);
        defaults.addEntry("nether_bag_max_speed_penalty", "Slowdown in percent (0-100) when a nether bag is completely full", 0);

        TomlConfigHandler config = new TomlConfigHandler(MOD_ID, defaults);
        itemIdStart = config.getInt("starting_item_id");
        blockIdStart = config.getInt("starting_block_id");

        //** Toggle Penalties and Show Content Written to Config*/
        oneBagLimit = config.getBoolean("one_bag_limit");
        carriedBagPenalty = config.getBoolean("carried_bag_penalty");
        wornBagHasPenalty = config.getBoolean("worn_bag_has_penalty");
        showContentsWhenCarried = config.getBoolean("show_contents_when_carried");

        //* Toggle Penalty Percentages */
        smallPenaltyPercent = config.getInt("small_bag_max_speed_penalty");
        mediumPenaltyPercent = config.getInt("medium_bag_max_speed_penalty");
        largePenaltyPercent = config.getInt("large_bag_max_speed_penalty");
        reinforcedPenaltyPercent = config.getInt("reinforced_bag_max_speed_penalty");
        netherPenaltyPercent = config.getInt("nether_bag_max_speed_penalty");



    }

    @Override
    public void onInitialize() {
        Key key = Key.of(MOD_ID);
        CommonEvents.BEFORE_GAME_START.listen(key, BetterWithBagsMod::registerContent);
        CommonEvents.RECIPES_NAMESPACE_INIT.listen(key, BackpackRecipes::initNamespaces);
        CommonEvents.RECIPES_READY.listen(key, BackpackRecipes::registerRecipes);
        LOGGER.info("Better With Bags initialized!");
    }

    private static void registerContent() {
        BackpackRecipes.registerRecipeTypes();
        new BackpackBlocks().initializeBlocks();
        new BackpackItems().initializeItems();
        LOGGER.info("Registered items starting at ID {} and blocks starting at ID {}", itemIdStart, blockIdStart);
    }
}
