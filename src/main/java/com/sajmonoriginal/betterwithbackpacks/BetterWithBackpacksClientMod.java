package com.sajmonoriginal.betterwithbackpacks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import turniplabs.halplibe.util.ClientStartEntrypoint;

@Environment(EnvType.CLIENT)
public class BetterWithBackpacksClientMod implements ClientModInitializer, ClientStartEntrypoint {

    @Override
    public void onInitializeClient() {
        BetterWithBackpacksMod.LOGGER.info("Better With Backpacks client initialized!");
    }

    @Override
    public void beforeClientStart() {
    }

    @Override
    public void afterClientStart() {
    }
}
