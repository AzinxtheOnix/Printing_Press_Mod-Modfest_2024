package com.glowbrick.printingpress.item;

import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrintingPress.MOD_ID);

    public static final Supplier<CreativeModeTab> PRINTING_PRESS_TAB =
           CREATIVE_MODE_TAB.register("printing_press_tab", () -> CreativeModeTab.builder()
                   .title(Component.translatable("itemGroup.printingpress.printing_press_tab"))
                   .icon(()-> new ItemStack(ModBlocks.PRINTINGPRESS.get()))                                 //may break
                   .displayItems((pParameters, pOutput)->{
                       pOutput.accept(ModItems.MOVABLE_TYPE.get());
                       pOutput.accept(ModBlocks.PRINTINGPRESS);
                       pOutput.accept(ModBlocks.TYPESETTER);
                       pOutput.accept(ModItems.INK_BOTTLE);
                       pOutput.accept(ModItems.MAGIC_INK_BOTTLE);
                       pOutput.accept(ModItems.TYPE_BLOCK);
                       pOutput.accept(ModItems.INK_BOTTLE);
                       pOutput.accept(ModItems.MAGIC_INK_BOTTLE);
                   }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
