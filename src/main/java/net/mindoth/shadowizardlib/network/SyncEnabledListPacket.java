package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SyncEnabledListPacket implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncEnabledListPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ShadowizardLib.MOD_ID, "sync_enabled_list"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEnabledListPacket> STREAM_CODEC =
            CustomPacketPayload.codec(SyncEnabledListPacket::encode, SyncEnabledListPacket::new);

    public Set<UUID> set = new HashSet<>();

    public SyncEnabledListPacket(Set<UUID> set) {
        this.set = set;
    }

    public SyncEnabledListPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        if ( size > 0 ) for ( int i = 0; i < size; i++ ) this.set.add(buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        if ( !this.set.isEmpty() ) {
            buf.writeInt(this.set.size());
            for ( UUID id : this.set ) buf.writeUUID(id);
        }
        else buf.writeInt(0);
    }

    public static void handle(SyncEnabledListPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ThanksList.ENABLED = packet.set;
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
