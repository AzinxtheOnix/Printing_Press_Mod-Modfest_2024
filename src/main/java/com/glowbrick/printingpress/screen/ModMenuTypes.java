package com.glowbrick.printingpress.screen;

import com.glowbrick.printingpress.PrintingPress;
import com.glowbrick.printingpress.screen.custom.TypesetterMenu;
import com.glowbrick.printingpress.screen.custom.TypesetterScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, PrintingPress.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TypesetterMenu>> TYPESETTER_MENU =
            registerMenuType("typesetter_menu", TypesetterMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String pName, IContainerFactory<T> factory) {
        return MENUS.register(pName, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

}
