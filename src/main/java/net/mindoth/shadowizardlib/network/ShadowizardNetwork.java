package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ShadowizardNetwork {
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ShadowizardLib.MOD_ID, "network"))
                .networkProtocolVersion(() -> "2.0.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        CHANNEL = net;

        net.messageBuilder(PacketSyncClientEffects.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSyncClientEffects::new)
                .encoder(PacketSyncClientEffects::encode)
                .consumerMainThread(PacketSyncClientEffects::handle)
                .add();

        net.messageBuilder(PacketToggleClientEffects.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(PacketToggleClientEffects::new)
                .encoder(PacketToggleClientEffects::encode)
                .consumerMainThread(PacketToggleClientEffects::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAll(MSG message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
        if ( sendToSource && entity instanceof ServerPlayer serverPlayer ) sendToPlayer(message, serverPlayer);
    }
}
