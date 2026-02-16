package com.sajmonoriginal.betterwithbags.util;

import net.minecraft.core.entity.player.Player;
import java.util.WeakHashMap;

public class BedrollSleepTracker {
    private static final WeakHashMap<Player, Boolean> SLEEPING_IN_BEDROLL = new WeakHashMap<>();

    public static void markSleepingInBedroll(Player player) {
        SLEEPING_IN_BEDROLL.put(player, true);
    }

    public static void clearSleepingInBedroll(Player player) {
        SLEEPING_IN_BEDROLL.remove(player);
    }

    public static boolean isSleepingInBedroll(Player player) {
        return SLEEPING_IN_BEDROLL.getOrDefault(player, false);
    }
}
