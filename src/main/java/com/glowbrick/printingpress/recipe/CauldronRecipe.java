package com.glowbrick.printingpress.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

//Code based on Neoforged Docs at https://docs.neoforged.net/docs/resources/server/recipes/
public class CauldronRecipe implements Recipe<CauldronRecipeInput> {
    private final BlockState inputState;
    private final Ingredient inputItem;
    private final ItemStack result;

    public BlockState getInputState() {
        return inputState;
    }

    public Ingredient getInputItem() {
        return inputItem;
    }

    public ItemStack getResult() {
        return result;
    }

    public CauldronRecipe(BlockState inputState, Ingredient inputItem, ItemStack result){
        this.inputState = inputState;
        this.inputItem = inputItem;
        this.result = result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.inputItem);
        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 1;
    }

    @Override
    public boolean matches(CauldronRecipeInput input, Level level) {
        return this.inputState == input.state() && this.inputItem.test(input.stack());
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public ItemStack assemble(CauldronRecipeInput input, HolderLookup.Provider registries) {
        return this.result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAULDRON_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CAULDRON_TYPE.get();
    }

}
