package com.sajmonoriginal.betterwithbackpacks;

import com.sajmonoriginal.betterwithbackpacks.hud.HudComponentMeshSlot;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.HudComponent;
import net.minecraft.client.gui.hud.component.HudComponents;
import net.minecraft.client.gui.hud.component.layout.LayoutSnap;
import turniplabs.halplibe.util.ClientStartEntrypoint;

@Environment(EnvType.CLIENT)
public class BetterWithBackpacksClientMod implements ClientModInitializer, ClientStartEntrypoint {

    public static HudComponent MESH_SLOT_LEFT;
    public static HudComponent MESH_SLOT_RIGHT;

    @Override
    public void onInitializeClient() {
        BetterWithBackpacksMod.LOGGER.info("Better With Backpacks client initialized!");
    }

    @Override
    public void beforeClientStart() {
    }

    @Override
    public void afterClientStart() {
        MESH_SLOT_LEFT = HudComponents.register(new HudComponentMeshSlot(
            "mesh_slot_left", 0,
            new LayoutSnap(HudComponents.HOTBAR, ComponentAnchor.CENTER_LEFT, ComponentAnchor.CENTER_RIGHT)
        ));
        MESH_SLOT_RIGHT = HudComponents.register(new HudComponentMeshSlot(
            "mesh_slot_right", 1,
            new LayoutSnap(HudComponents.HOTBAR, ComponentAnchor.CENTER_RIGHT, ComponentAnchor.CENTER_LEFT)
        ));
    }
}
