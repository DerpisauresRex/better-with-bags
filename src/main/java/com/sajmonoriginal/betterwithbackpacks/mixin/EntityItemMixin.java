package com.sajmonoriginal.betterwithbackpacks.mixin;

import com.sajmonoriginal.betterwithbackpacks.item.BackpackTier;
import com.sajmonoriginal.betterwithbackpacks.item.ItemBackpack;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityItem.class, remap = false)
public abstract class EntityItemMixin extends Entity {

    @Shadow
    public ItemStack item;

    protected EntityItemMixin(World world) {
        super(world);
    }

    @Inject(method = "<init>(Lnet/minecraft/core/world/World;DDDLnet/minecraft/core/item/ItemStack;)V", at = @At("RETURN"))
    private void bwb$setFireImmuneForNetherBackpack(World world, double x, double y, double z, ItemStack itemStack, CallbackInfo ci) {
        if (itemStack != null && itemStack.getItem() instanceof ItemBackpack) {
            BackpackTier tier = ((ItemBackpack) itemStack.getItem()).getTier();
            if (tier.isFireResistant()) {
                this.fireImmune = true;
            }
        }
    }
}
