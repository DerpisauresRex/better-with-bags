package com.sajmonoriginal.betterwithbags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import turniplabs.halplibe.util.ClientStartEntrypoint;

@Environment(EnvType.CLIENT)
public class BetterWithBagsClientMod implements ClientModInitializer, ClientStartEntrypoint {

    @Override
    public void onInitializeClient() {
        BetterWithBagsMod.LOGGER.info("Better With Bags client initialized!");
    }

    @Override
    public void beforeClientStart() {
    }

    @Override
    public void afterClientStart() {
    }
}
