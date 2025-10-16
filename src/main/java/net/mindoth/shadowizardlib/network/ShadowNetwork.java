package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ShadowNetwork {
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ShadowizardLib.MOD_ID, "network"))
                .networkProtocolVersion(() -> "3.0.0")
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

        net.messageBuilder(PacketSendCustomParticles.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PacketSendCustomParticles::new)
                .encoder(PacketSendCustomParticles::encode)
                .consumerMainThread(PacketSendCustomParticles::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        CHANNEL.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        if ( player != null ) CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity) {
        if ( entity != null ) sendToPlayersTrackingEntity(message, entity, false);
    }

    public static <MSG> void sendToPlayersTrackingEntity(MSG message, Entity entity, boolean sendToSource) {
        if ( entity != null ) {
            CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
            if ( sendToSource && entity instanceof ServerPlayer serverPlayer ) sendToPlayer(message, serverPlayer);
        }
    }

    public static <MSG> void sendToNearby(MSG message, Level world, Entity caster) {
        sendToNearby(message, world, caster.blockPosition());
    }

    public static <MSG> void sendToNearby(MSG message, Level level, Vec3 center) {
        if ( level instanceof ServerLevel serverLevel ) {
            BlockPos pos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> sendToPlayer(message, p));
        }
    }

    public static <MSG> void sendToNearby(MSG message, Level level, BlockPos pos) {
        if ( level instanceof ServerLevel serverLevel ) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64)
                    .forEach(p -> sendToPlayer(message, p));
        }
    }

    public static <MSG> void sendToAll(MSG message) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
}
