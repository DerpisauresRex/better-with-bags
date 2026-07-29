package com.sajmonoriginal.betterwithbags.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.core.item.Item;

@Environment(EnvType.CLIENT)
public class ItemModelBackpack extends ItemModelStandard {

    public ItemModelBackpack(Item item, String namespace) {
        super(item, namespace);
    }
}
