package com.glowbrick.printingpress.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public record HeldEnchantments(ItemEnchantments test) {
    public static final Codec<HeldEnchantments> CODEC2 = RecordCodecBuilder.create(heldEnchantmentsInstance ->
            heldEnchantmentsInstance.group(
                    //Codec.STRING.fieldOf("keys").forGetter(HeldEnchantments::keys),
                    //Codec.STRING.fieldOf("values").forGetter(HeldEnchantments::values)
                    ItemEnchantments.CODEC.fieldOf("test").forGetter(HeldEnchantments::test)
            ).apply(heldEnchantmentsInstance, HeldEnchantments::new));
}
