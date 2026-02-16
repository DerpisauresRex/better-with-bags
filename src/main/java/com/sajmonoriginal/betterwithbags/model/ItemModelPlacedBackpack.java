package com.sajmonoriginal.betterwithbags.model;

import com.sajmonoriginal.betterwithbags.BetterWithBagsMod;
import com.sajmonoriginal.betterwithbags.item.BackpackTier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;

@Environment(EnvType.CLIENT)
public class ItemModelPlacedBackpack extends ItemModelStandard {

    private static final String PLACED_GEOMETRY_ID = "geometry.bag.placed";
    private static final String TEXTURE_PATH = "/assets/" + BetterWithBagsMod.MOD_ID + "/textures/entity/";

    private static StaticEntityModel placedModel;

    private final BackpackTier tier;
    private final String texture;

    public ItemModelPlacedBackpack(Item blockItem, BackpackTier tier) {
        super(blockItem, null);
        this.tier = tier;
        this.texture = TEXTURE_PATH + "backpack_placed_" + tier.getName() + ".png";
        this.icon = TextureRegistry.getTexture(BetterWithBagsMod.MOD_ID + ":item/backpack_" + tier.getName());
    }

    private static StaticEntityModel getPlacedModel() {
        if (placedModel == null) {
            placedModel = EntityGeometryMojangData.Cache.getModel(PLACED_GEOMETRY_ID, 0);
        }
        return placedModel;
    }

    @Override
    public void renderItemIntoGui(Tessellator tessellator, Font font, TextureManager textureManager, ItemStack itemStack, int x, int y, float brightness, float alpha) {
        StaticEntityModel model = getPlacedModel();
        if (model == null) {
            super.renderItemIntoGui(tessellator, font, textureManager, itemStack, x, y, brightness, alpha);
            return;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        GL11.glTranslatef(x - 2, y + 3, -3.0f);
        GL11.glScalef(10.0f, 10.0f, 10.0f);
        GL11.glTranslatef(1.0f, 0.5f, 1.0f);
        GL11.glScalef(1.0f, 1.0f, -1.0f);
        GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glColor4f(brightness, brightness, brightness, alpha);
        GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);

        float modelScale = 1.0f / 16.0f;
        GL11.glScalef(modelScale, modelScale, modelScale);
        GL11.glTranslatef(0.0f, -13.5f, 0.0f);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_CULL_FACE);

        textureManager.loadTexture(this.texture).bind();
        model.render(tessellator);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
