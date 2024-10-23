/*package com.glowbrick.printingpress.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record TypesetterRecipe(NonNullList<Ingredient> inputItemList, ItemStack output) implements Recipe<TypesetterRecipeInput> {

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(inputItemList);
        return list;
    }

    @Override
    public boolean matches(TypesetterRecipeInput typesetterRecipeInput, Level level) {
        if(level.isClientSide()) {
            return false;
        }

        return (inputItemList.getFirst().test(typesetterRecipeInput.getItem(0)) && inputItemList.get(1).test(typesetterRecipeInput.getItem(1)));
    }

    @Override
    public ItemStack assemble(TypesetterRecipeInput typesetterRecipeInput, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
*/