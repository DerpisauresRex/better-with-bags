package com.sajmonoriginal.betterwithbackpacks.mixin;

import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import com.sajmonoriginal.betterwithbackpacks.util.MeshHotbarCapable;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Entity {

    @Shadow
    public ContainerInventory inventory;

    @Shadow
    public abstract void dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean flag);

    protected PlayerMixin(World world) {
        super(world);
    }

    @Inject(method = "dropCurrentItem", at = @At("HEAD"), cancellable = true)
    private void bwb$dropMeshItem(boolean dropAll, CallbackInfo ci) {
        MeshHotbarCapable mesh = (MeshHotbarCapable) this.inventory;
        if (mesh.betterWithBackpacks$getMeshSelected() <= 0) return;

        int maxCount = dropAll ? 64 : 1;
        ItemStack dropped = mesh.betterWithBackpacks$removeMeshItem(maxCount);
        if (dropped != null) {
            this.dropPlayerItemWithRandomChoice(dropped, false);
        }
        ci.cancel();
    }

    @Inject(method = "destroyCurrentEquippedItem", at = @At("HEAD"), cancellable = true)
    private void bwb$destroyMeshItem(CallbackInfo ci) {
        MeshHotbarCapable mesh = (MeshHotbarCapable) this.inventory;
        if (mesh.betterWithBackpacks$getMeshSelected() <= 0) return;

        mesh.betterWithBackpacks$destroyMeshItem();
        ci.cancel();
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void bwb$tickMeshSync(CallbackInfo ci) {
        Player self = (Player) (Object) this;

        MeshHotbarCapable mesh = (MeshHotbarCapable) self.inventory;
        if (mesh.betterWithBackpacks$getMeshSelected() > 0) {
            mesh.betterWithBackpacks$syncMeshItem();
        }

        ItemStack backpackStack = ItemBackpack.getWornBackpackStack(self);
        if (backpackStack == null) return;

        ItemBackpack backpack = (ItemBackpack) backpackStack.getItem();
        if (!backpack.getTier().hasSpeedPenalty()) return;

        int occupied = ItemBackpack.getOccupiedSlotCount(backpackStack);
        int capacity = backpack.getCapacity();
        if (occupied <= 0 || capacity <= 0) return;

        float fillRatio = (float) occupied / capacity;
        float maxPenalty = 1.0f - backpack.getTier().getSpeedMultiplier();
        this.speed *= (1.0f - fillRatio * maxPenalty);
    }
}
