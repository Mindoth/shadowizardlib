package net.mindoth.shadowizardlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketToggleClientEffects {

    public PacketToggleClientEffects() {
    }

    public PacketToggleClientEffects(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if ( sender != null ) {
                ShadowizardNetwork.sendToAll(new PacketSyncClientEffects(1, sender.getUUID()));
            }
        });
    }
}
