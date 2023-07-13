package net.mindoth.shadowizardlib.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketDistro {

    public static void sendTo(SimpleChannel channel, Object packet, Player player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
    }

    public static void sendToAll(SimpleChannel channel, Object packet) {
        channel.send(PacketDistributor.ALL.noArg(), packet);
    }
}
