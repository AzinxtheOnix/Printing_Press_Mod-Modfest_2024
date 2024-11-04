package com.glowbrick.printingpress.item.custom;

import com.glowbrick.printingpress.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;



public class TypeBlockItem extends Item {
    public TypeBlockItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public static ItemStack testMethod (EnchantmentInstance instance) {
        ItemStack itemstack = new ItemStack(ModItems.TYPE_BLOCK.get());
        itemstack.enchant(instance.enchantment, instance.level);
        return itemstack;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
