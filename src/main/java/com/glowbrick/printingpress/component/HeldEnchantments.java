package com.glowbrick.printingpress.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Objects;

public record HeldEnchantments(ItemEnchantments test) {
    public static final Codec<HeldEnchantments> CODEC2 = RecordCodecBuilder.create(heldEnchantmentsInstance ->
            heldEnchantmentsInstance.group(
                    //Codec.STRING.fieldOf("keys").forGetter(HeldEnchantments::keys),
                    //Codec.STRING.fieldOf("values").forGetter(HeldEnchantments::values)
                    ItemEnchantments.CODEC.fieldOf("test").forGetter(HeldEnchantments::test)
            ).apply(heldEnchantmentsInstance, HeldEnchantments::new));

    public String getOutputString() {
        return test.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.test);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        } else {
            return obj instanceof HeldEnchantments hed && this.test == hed.test;
        }
    }
}
