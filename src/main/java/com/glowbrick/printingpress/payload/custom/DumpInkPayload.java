package com.glowbrick.printingpress.payload.custom;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DumpInkPayload(int xCoord, int yCoord, int zCoord) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<DumpInkPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("printingpress", "dump_ink_payload"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, DumpInkPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        DumpInkPayload::xCoord,
        ByteBufCodecs.VAR_INT,
        DumpInkPayload::yCoord,
        ByteBufCodecs.VAR_INT,
        DumpInkPayload::zCoord,
        DumpInkPayload::new
    );
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
