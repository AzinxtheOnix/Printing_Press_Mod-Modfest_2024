package com.glowbrick.printingpress.item.custom;

import com.glowbrick.printingpress.component.ModDataComponentTypes;
import com.glowbrick.printingpress.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;


public class TypeBlockItem extends Item {
    public TypeBlockItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()){
            tooltipComponents.add(Component.translatable("tooltip.printingpress.typeblock.tooltip.1"));
            if(stack.get(DataComponents.STORED_ENCHANTMENTS) != null){
                tooltipComponents.add(Component.literal("Tooltip: " + stack.get(DataComponents.STORED_ENCHANTMENTS)));
            }else{
                tooltipComponents.add(Component.literal("Empty"));
            }
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
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
