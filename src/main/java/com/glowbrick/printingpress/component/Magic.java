package com.glowbrick.printingpress.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Objects;

public record Magic(boolean magic) {
    //Lets you make a new instance out of a json file and a json file out of an instance
    //forGetter is how it knows how to save the field
    public static final Codec<Magic> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("magic").forGetter(Magic::magic)
            ).apply(instance, Magic::new));

    @Override
    public int hashCode() {
        return Objects.hash(this.magic);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        } else {
            //Return an instance of this record where all of these things sre true?
            return obj instanceof Magic im && this.magic == im.magic;
        }
    }
}
