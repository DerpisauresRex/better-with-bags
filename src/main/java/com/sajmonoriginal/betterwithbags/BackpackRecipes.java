package com.sajmonoriginal.betterwithbags;

import com.sajmonoriginal.betterwithbags.item.BackpackItems;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class BackpackRecipes implements RecipeEntrypoint {

    @Override
    public void onRecipesReady() {
        String modId = BetterWithBagsMod.MOD_ID;

        RecipeBuilder.Shaped(modId, "CLC", "LSL", "CLC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('S', Items.STRING)
            .create("backpack_small", new ItemStack(BackpackItems.BACKPACK_SMALL, 1));

        RecipeBuilder.Shaped(modId, "CIC", "LBL", "CIC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('I', Items.INGOT_IRON)
            .addInput('B', BackpackItems.BACKPACK_SMALL)
            .create("backpack_medium", new ItemStack(BackpackItems.BACKPACK_MEDIUM, 1));

        RecipeBuilder.Shaped(modId, "CGC", "LBL", "CGC")
            .addInput('L', Items.LEATHER)
            .addInput('C', Items.CLOTH)
            .addInput('G', Items.INGOT_GOLD)
            .addInput('B', BackpackItems.BACKPACK_MEDIUM)
            .create("backpack_large", new ItemStack(BackpackItems.BACKPACK_LARGE, 1));

        RecipeBuilder.Shaped(modId, "DCD", "MBM", "DCD")
            .addInput('D', Items.DIAMOND)
            .addInput('C', Items.CLOTH)
            .addInput('M', Blocks.MESH)
            .addInput('B', BackpackItems.BACKPACK_LARGE)
            .create("backpack_reinforced", new ItemStack(BackpackItems.BACKPACK_REINFORCED, 1));

        RecipeBuilder.Shaped(modId, "OCO", "MBM", "OCO")
            .addInput('O', Items.OLIVINE)
            .addInput('C', Items.NETHERCOAL)
            .addInput('M', Blocks.MESH_GOLD)
            .addInput('B', BackpackItems.BACKPACK_REINFORCED)
            .create("backpack_nether", new ItemStack(BackpackItems.BACKPACK_NETHER, 1));

        RecipeBuilder.Shaped(modId, "LWL", "LWL", "LLL")
            .addInput('L', Items.LEATHER)
            .addInput('W', Items.CLOTH)
            .create("bedroll", new ItemStack(BackpackItems.BEDROLL, 1));
    }

    @Override
    public void initNamespaces() {
        RecipeBuilder.initNameSpace(BetterWithBagsMod.MOD_ID);
    }
}
