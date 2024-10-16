package com.glowbrick.printingpress.recipe;

import com.glowbrick.printingpress.PrintingPress;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, PrintingPress.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, PrintingPress.MOD_ID);

    public static final Supplier<RecipeType<CauldronRecipe>> CAULDRON_TYPE =
            RECIPE_TYPES.register(
                    "cauldron_recipe",
                    () -> RecipeType.<CauldronRecipe>simple(ResourceLocation.fromNamespaceAndPath(PrintingPress.MOD_ID, "cauldron_recipe"))
            );
    public static final Supplier<RecipeSerializer<CauldronRecipe>> CAULDRON_SERIALIZER =
            RECIPE_SERIALIZERS.register("cauldron_recipe", CauldronRecipeSerializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }

}
