package com.sajmonoriginal.betterwithbackpacks.item;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public class BackpackItems {
    private static int currentId = BetterWithBackpacksMod.itemIdStart;
    private static boolean initialized = false;

    public static Item BACKPACK_SMALL;
    public static Item BACKPACK_MEDIUM;
    public static Item BACKPACK_LARGE;
    public static Item BACKPACK_REINFORCED;
    public static Item BACKPACK_NETHER;

    private static String itemKey(String name) {
        return BetterWithBackpacksMod.MOD_ID + ":item/" + name;
    }

    public void initializeItems() {
        if (initialized) {
            BetterWithBackpacksMod.LOGGER.warn("BackpackItems.initializeItems() called twice - skipping");
            return;
        }
        initialized = true;

        BetterWithBackpacksMod.LOGGER.info("Registering SMALL with ID {}", currentId);
        BACKPACK_SMALL = new ItemBuilder(BetterWithBackpacksMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.small", itemKey("backpack_small"), currentId++, BackpackTier.SMALL));

        BetterWithBackpacksMod.LOGGER.info("Registering MEDIUM with ID {}", currentId);
        BACKPACK_MEDIUM = new ItemBuilder(BetterWithBackpacksMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.medium", itemKey("backpack_medium"), currentId++, BackpackTier.MEDIUM));

        BACKPACK_LARGE = new ItemBuilder(BetterWithBackpacksMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.large", itemKey("backpack_large"), currentId++, BackpackTier.LARGE));

        BACKPACK_REINFORCED = new ItemBuilder(BetterWithBackpacksMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.reinforced", itemKey("backpack_reinforced"), currentId++, BackpackTier.REINFORCED));

        BACKPACK_NETHER = new ItemBuilder(BetterWithBackpacksMod.MOD_ID)
            .setStackSize(1)
            .build(new ItemBackpack("backpack.nether", itemKey("backpack_nether"), currentId++, BackpackTier.NETHER));

        BetterWithBackpacksMod.LOGGER.info("Registered 5 backpack items starting at ID {}", BetterWithBackpacksMod.itemIdStart);
    }

    public static Item getItemForTier(BackpackTier tier) {
        switch (tier) {
            case SMALL: return BACKPACK_SMALL;
            case MEDIUM: return BACKPACK_MEDIUM;
            case LARGE: return BACKPACK_LARGE;
            case REINFORCED: return BACKPACK_REINFORCED;
            case NETHER: return BACKPACK_NETHER;
            default: return BACKPACK_SMALL;
        }
    }
}
