package com.sajmonoriginal.betterwithbackpacks.item;

import com.sajmonoriginal.betterwithbackpacks.BetterWithBackpacksMod;
import net.minecraft.core.item.material.ArmorMaterial;
import turniplabs.halplibe.helper.ArmorHelper;

public class BackpackArmorMaterial {
    public static final ArmorMaterial BACKPACK = ArmorHelper.createArmorMaterial(
        BetterWithBackpacksMod.MOD_ID,
        "backpack",
        500,
        0.0f,
        0.0f,
        0.0f,
        0.0f
    );
}
