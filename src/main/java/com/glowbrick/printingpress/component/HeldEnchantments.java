package com.glowbrick.printingpress.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HeldEnchantments(String keys, String values) {
    public static final Codec<HeldEnchantments> CODEC2 = RecordCodecBuilder.create(heldEnchantmentsInstance ->
            heldEnchantmentsInstance.group(
                    Codec.STRING.fieldOf("keys").forGetter(HeldEnchantments::keys),
                    Codec.STRING.fieldOf("values").forGetter(HeldEnchantments::values)
            ).apply(heldEnchantmentsInstance, HeldEnchantments::new));
}
