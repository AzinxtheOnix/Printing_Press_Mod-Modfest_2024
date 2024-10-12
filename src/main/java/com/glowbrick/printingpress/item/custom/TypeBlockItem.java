package com.glowbrick.printingpress.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class TypeBlockItem extends Item {
    public TypeBlockItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
