package com.glowbrick.printingpress.block;


import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.block.custom.TypesetterBlock;
import com.glowbrick.printingpress.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.glowbrick.printingpress.block.custom.PrintingPressBlock;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(PrintingPress.MOD_ID);

    public static final DeferredBlock<Block> PRINTINGPRESS = registerBlock("printing_press",
            ()-> new PrintingPressBlock(BlockBehaviour.Properties.of().noOcclusion()));

    public static final DeferredBlock<Block> TYPESETTER = registerBlock("typesetter",
            ()-> new TypesetterBlock(BlockBehaviour.Properties.of().noOcclusion()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
