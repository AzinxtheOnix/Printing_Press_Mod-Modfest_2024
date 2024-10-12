package com.glowbrick.printingpress.item;

import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrintingPress.MOD_ID);

    public static final DeferredItem<Item> MOVABLE_TYPE =
            ITEMS.registerItem("movable_type", Item::new, new Item.Properties());


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
