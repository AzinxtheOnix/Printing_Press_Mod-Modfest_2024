package com.glowbrick.printingpress.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

//Code based on Neoforged Docs at https://docs.neoforged.net/docs/resources/server/recipes/
public record CauldronRecipeInput(BlockState state, ItemStack stack) implements RecipeInput {
    @Override
    public ItemStack getItem(int slot) {
        if (slot != 0) throw new IllegalArgumentException("No item for index " + slot);
        return this.stack;
    }

    @Override
    public int size() {
        return 1;
    }
}
