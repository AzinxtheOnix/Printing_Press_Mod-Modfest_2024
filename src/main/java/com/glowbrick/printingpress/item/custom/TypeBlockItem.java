package com.glowbrick.printingpress.item.custom;

import com.glowbrick.printingpress.component.ModDataComponentTypes;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()){
            tooltipComponents.add(Component.translatable("tooltip.printingpress.typeblock.tooltip.1"));
            if(stack.get(ModDataComponentTypes.HELD_ENCHANTMENTS) != null){
                tooltipComponents.add(Component.literal(Objects.requireNonNull(stack.get(ModDataComponentTypes.HELD_ENCHANTMENTS.get())).getOutputString()));
            }
        }


        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
