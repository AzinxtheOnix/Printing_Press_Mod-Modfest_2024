package com.glowbrick.printingpress.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CauldronRecipeSerializer implements RecipeSerializer<CauldronRecipe> {
    @Override
    public MapCodec<CauldronRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static final MapCodec<CauldronRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BlockState.CODEC.fieldOf("state").forGetter(CauldronRecipe::getInputState),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(CauldronRecipe::getInputItem),
            ItemStack.CODEC.fieldOf("result").forGetter(CauldronRecipe::getResult)
    ).apply(inst, CauldronRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), CauldronRecipe::getInputState,
                    Ingredient.CONTENTS_STREAM_CODEC, CauldronRecipe::getInputItem,
                    ItemStack.STREAM_CODEC, CauldronRecipe::getResult,
                    CauldronRecipe::new
            );
}
