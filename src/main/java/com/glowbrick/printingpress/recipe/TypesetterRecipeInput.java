package com.glowbrick.printingpress.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record TypesetterRecipeInput(NonNullList<ItemStack> itemStackNonNullList) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return null;
    }

    @Override
    public int size() {
        return 2;
    }
}
