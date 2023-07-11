package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.util.ThanksList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class KeyPressMessage {
    public int key;

    public KeyPressMessage() {
    }

    public KeyPressMessage(int key) {
        this.key = key;
    }

    public static void encode(KeyPressMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.key);
    }

    public static KeyPressMessage decode(FriendlyByteBuf buffer) {
        return new KeyPressMessage(buffer.readInt());
    }

    public static void handle(KeyPressMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        if ( contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_SERVER ) {
            sendToAll(ShadowizardNetwork.CHANNEL, new SupporterDisableMessage(contextSupplier.get().getSender().getUUID()));
        }
        else {
            Set<UUID> set = ThanksList.DISABLED;
            UUID id = contextSupplier.get().getSender().getUUID();
            if ( set.contains(id) ) {
                set.remove(id);
            }
            else {
                set.add(id);
            }
        }
    }

    public static void sendToAll(SimpleChannel channel, Object packet) {
        channel.send(PacketDistributor.ALL.noArg(), packet);
    }
}
