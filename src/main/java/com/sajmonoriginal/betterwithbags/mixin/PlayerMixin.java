package com.sajmonoriginal.betterwithbags.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.sajmonoriginal.betterwithbags.block.BackpackBlocks;
import com.sajmonoriginal.betterwithbags.item.ItemBackpack;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//** Config Logic*/
import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Entity {

    protected PlayerMixin(World world) {
        super(world);
    }


    //** Config logic to enable/disable and modify percentage values of penalties from config*/
    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void bwb$applyBackpackSpeedPenalty(CallbackInfo ci) {
        Player self = (Player) (Object) this;

        boolean worn = ItemBackpack.isWearingBackpack(self);
        if (worn && !BetterWithBagsMod.wornBagHasPenalty) return;
        if (!worn && !BetterWithBagsMod.carriedBagPenalty) return;

        ItemStack backpackStack = ItemBackpack.findBackpackStack(self);
        if (backpackStack == null) return;

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        float maxPenalty = BetterWithBagsMod.getMaxSpeedPenalty(backpack.getTier());
        if (maxPenalty <= 0f) return;

        int occupied = ItemBackpack.getOccupiedSlotCount(backpackStack);
        int capacity = backpack.getCapacity();
        if (occupied <= 0 || capacity <= 0) return;

        float fillRatio = (float) occupied / capacity;
        this.speed *= (1.0f - fillRatio * maxPenalty);
    }


    // This logic was added to drop bag if multiple are present
    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void bwb$enforceSingleBackpack(CallbackInfo ci) {
        if (this.world.isClientSide || !BetterWithBagsMod.oneBagLimit) return; // added oneBagLimit config logic here
        Player self = (Player) (Object) this;

        boolean haveOne = ItemBackpack.isWearingBackpack(self); // worn bag wins
        ItemStack[] inv = self.inventory.mainInventory;
        for (int i = 0; i < inv.length; i++) {
            ItemStack s = inv[i];
            if (!ItemBackpack.isBackpack(s)) continue;
            if (!haveOne) { haveOne = true; continue; }  // keep the first one found
            inv[i] = null;
            self.dropPlayerItem(s);  // contents live in the stack's NBT, so nothing is lost
    }
}


    @WrapWithCondition(
        method = "sleepInBedAt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;sendStatusMessageTranslated(Ljava/lang/String;[Ljava/lang/String;)V")
    )
    private boolean bwb$skipBedrollSpawnMessage(Player self, String key, String[] args, @Local(argsOnly = true, ordinal = 0) int x,
                                                @Local(argsOnly = true, ordinal = 1) int y, @Local(argsOnly = true, ordinal = 2) int z) {
        return !"messages.bed.setSpawn".equals(key) || !bwb$isBedroll(x, y, z);
    }

    @WrapWithCondition(
        method = "sleepInBedAt",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/Player;setPlayerSpawnPoint(Lnet/minecraft/core/world/pos/TilePos;)V")
    )
    private boolean bwb$skipBedrollSpawnPoint(Player self, TilePos spawn) {
        return !bwb$isBedroll(spawn.x, spawn.y, spawn.z);
    }

    @Unique
    private boolean bwb$isBedroll(int x, int y, int z) {
        return this.world.getBlockType(new TilePos(x, y, z)) == BackpackBlocks.BEDROLL_BLOCK;
    }

    @Inject(method = "setPlayerSleeping", at = @At("TAIL"))
    private void bwb$adjustBedrollSleepPosition(int x, int y, int z, CallbackInfo ci) {
        if (this.world != null && this.world.isBlockLoaded(new TilePos(x, y, z)) && bwb$isBedroll(x, y, z)) {
            this.setPos(this.x, y + 0.5625, this.z);
        }
    }
}
