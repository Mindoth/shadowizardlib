package net.mindoth.shadowizardlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketToggleClientEffects {

    public PacketToggleClientEffects() {
    }

    public PacketToggleClientEffects(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if ( sender != null ) ShadowNetwork.sendToAll(new PacketSyncClientEffects(1, sender.getUUID()));
        });
        context.setPacketHandled(true);
    }
}
