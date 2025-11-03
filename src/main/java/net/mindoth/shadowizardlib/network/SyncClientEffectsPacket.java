package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class SyncClientEffectsPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncClientEffectsPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ShadowizardLib.MOD_ID, "sync_client_effects"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncClientEffectsPacket> STREAM_CODEC =
            CustomPacketPayload.codec(SyncClientEffectsPacket::encode, SyncClientEffectsPacket::new);

    //type = 0: toggle effects on player join. type = 1: toggle effects.
    private int type;
    private UUID id;

    public SyncClientEffectsPacket(int type, UUID id) {
        this.type = type;
        this.id = id;
    }

    public SyncClientEffectsPacket(FriendlyByteBuf buf) {
        this.type = buf.readByte();
        this.id = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.type);
        buf.writeUUID(this.id);
    }

    public static void handle(SyncClientEffectsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if ( packet.type == 0 ) ThanksList.DISABLED.add(packet.id);
            else if ( packet.type == 1 ) {
                if ( ThanksList.DISABLED.contains(packet.id) ) ThanksList.DISABLED.remove(packet.id);
                else ThanksList.DISABLED.add(packet.id);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
