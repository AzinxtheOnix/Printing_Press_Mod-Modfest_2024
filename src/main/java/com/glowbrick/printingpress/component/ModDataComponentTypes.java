package com.glowbrick.printingpress.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "printingpress");

    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Magic>> MAGIC =
            REGISTRAR.registerComponentType("magic", builder -> builder.persistent(Magic.CODEC));
}
