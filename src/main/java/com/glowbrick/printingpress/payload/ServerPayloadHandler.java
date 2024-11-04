package com.glowbrick.printingpress.payload;

import com.glowbrick.printingpress.payload.custom.DumpInkPayload;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {
    
    public static void handleDataOnMain(final DumpInkPayload data, final IPayloadContext context) {
        // Do something with the data, on the main thread
    }
}
