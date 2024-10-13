package com.glowbrick.printingpress.item.custom;

import com.glowbrick.printingpress.component.ModDataComponentTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InkBottleItem extends Item {

    public InkBottleItem(Properties properties) {
        super(properties.stacksTo(1).durability(3));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.get(ModDataComponentTypes.MAGIC.get()) != null;
    }
}
