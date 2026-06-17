package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ToggleClientEffectsPacket implements CustomPacketPayload {

    public static final Type<ToggleClientEffectsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(ShadowizardLib.MOD_ID, "toggle_client_effects"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleClientEffectsPacket> STREAM_CODEC =
            CustomPacketPayload.codec(ToggleClientEffectsPacket::encode, ToggleClientEffectsPacket::new);

    public ToggleClientEffectsPacket() {
    }

    public ToggleClientEffectsPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public static void handle(ToggleClientEffectsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player();
            UUID id = sender.getUUID();
            if ( ThanksList.SERVER_ENABLED.contains(id) ) ThanksList.SERVER_ENABLED.remove(id);
            else ThanksList.SERVER_ENABLED.add(id);
            PacketDistributor.sendToAllPlayers(new SyncEnabledListPacket(ThanksList.SERVER_ENABLED));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
