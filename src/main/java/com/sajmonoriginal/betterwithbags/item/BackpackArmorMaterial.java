package com.sajmonoriginal.betterwithbags.item;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import net.minecraft.core.item.material.ArmorMaterial;
import turniplabs.halplibe.helper.ArmorHelper;

public class BackpackArmorMaterial {
    public static final ArmorMaterial BACKPACK = ArmorHelper.createArmorMaterial(
        BetterWithBagsMod.MOD_ID,
        "backpack",
        500,
        0.0f,
        0.0f,
        0.0f,
        0.0f
    );
}
