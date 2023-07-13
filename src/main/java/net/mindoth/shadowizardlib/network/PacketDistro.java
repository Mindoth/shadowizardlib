package net.mindoth.shadowizardlib.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketDistro {

    public static void sendTo(SimpleChannel channel, Object packet, PlayerEntity player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), packet);
    }

    public static void sendToAll(SimpleChannel channel, Object packet) {
        channel.send(PacketDistributor.ALL.noArg(), packet);
    }
}
