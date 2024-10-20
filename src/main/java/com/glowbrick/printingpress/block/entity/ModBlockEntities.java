package com.glowbrick.printingpress.block.entity;


import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.block.ModBlocks;
import com.glowbrick.printingpress.block.entity.block.PrintingPressBlockEntity;
import com.glowbrick.printingpress.block.entity.block.TypesetterBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PrintingPress.MOD_ID);
    public static final Supplier<BlockEntityType<TypesetterBlockEntity>> TYPESETTER_BE =
            BLOCK_ENTITIES.register("typesetter_be", () -> BlockEntityType.Builder.of(
                    TypesetterBlockEntity::new, ModBlocks.TYPESETTER.get()).build(null));
    public static final Supplier<BlockEntityType<PrintingPressBlockEntity>> PRINTINGPRESS_BE = 
            BLOCK_ENTITIES.register("printingpress_be", () -> BlockEntityType.Builder.of(
                    PrintingPressBlockEntity::new, ModBlocks.PRINTINGPRESS.get()).build(null));


    public static void register(IEventBus eventBus){
        BLOCK_ENTITIES.register(eventBus);
    }
}
