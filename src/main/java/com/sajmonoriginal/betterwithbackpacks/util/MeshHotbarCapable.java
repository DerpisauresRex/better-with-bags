package com.sajmonoriginal.betterwithbackpacks.util;

import net.minecraft.core.item.ItemStack;

public interface MeshHotbarCapable {
    int betterWithBackpacks$getMeshSelected();
    void betterWithBackpacks$setMeshSelected(int side);
    void betterWithBackpacks$syncMeshItem();
    ItemStack betterWithBackpacks$removeMeshItem(int maxCount);
    void betterWithBackpacks$destroyMeshItem();
}
