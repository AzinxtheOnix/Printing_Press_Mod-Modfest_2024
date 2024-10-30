package com.glowbrick.printingpress.item;

import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.component.Magic;
import com.glowbrick.printingpress.item.custom.InkBottleItem;
import com.glowbrick.printingpress.item.custom.TypeBlockItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.glowbrick.printingpress.component.ModDataComponentTypes.MAGIC;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PrintingPress.MOD_ID);

    public static final DeferredItem<Item> MOVABLE_TYPE =
            ITEMS.registerItem("movable_type", Item::new, new Item.Properties());

    public static final DeferredItem<Item> INK_BOTTLE =
            ITEMS.registerItem("ink_bottle", InkBottleItem::new);
    public static final DeferredItem<Item> MAGIC_INK_BOTTLE =
            ITEMS.registerItem("magic_ink_bottle", properties -> new InkBottleItem(properties.component(MAGIC.value(), new Magic(true))));


    public static final DeferredItem<Item> TYPE_BLOCK =
            ITEMS.registerItem("type_block", properties -> new TypeBlockItem(new Item.Properties()
                    .stacksTo(1).component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
