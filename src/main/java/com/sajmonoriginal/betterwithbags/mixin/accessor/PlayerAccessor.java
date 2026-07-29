package com.sajmonoriginal.betterwithbags.mixin.accessor;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Player.class, remap = false)
public interface PlayerAccessor {

    @Accessor("playerSpawnPoint")
    TilePos getPlayerSpawnCoordinate();

    @Accessor("playerSpawnPoint")
    void setPlayerSpawnCoordinate(TilePos coords);
}
