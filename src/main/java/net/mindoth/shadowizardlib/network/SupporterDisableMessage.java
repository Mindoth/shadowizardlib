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

public class SupporterDisableMessage implements MessageProvider<SupporterDisableMessage> {

    private int type;
    private UUID id;

    public SupporterDisableMessage(int type) {
        this.type = type;
    }

    public SupporterDisableMessage(int type, UUID id) {
        this(type);
        this.id = id;
    }

    @Override
    public void encode(SupporterDisableMessage message, FriendlyByteBuf buffer) {
        buffer.writeByte(message.type);
        buffer.writeByte(message.id == null ? 0 : 1);
        if (message.id != null) buffer.writeUUID(message.id);
    }

    @Override
    public SupporterDisableMessage decode(FriendlyByteBuf buffer) {
        int type = buffer.readByte();
        if ( buffer.readByte() == 1 ) {
            return new SupporterDisableMessage(type, buffer.readUUID());
        }
        else return new SupporterDisableMessage(type);
    }

    @Override
    public void handle(SupporterDisableMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        if ( contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_SERVER ) {
            MessageHelper.handlePacket(() -> () -> {
                PacketDistro.sendToAll(ShadowizardNetwork.CHANNEL, new SupporterDisableMessage(message.type, contextSupplier.get().getSender().getUUID()));
            }, contextSupplier);
        }
        else {
            MessageHelper.handlePacket(() -> () -> {
                Set<UUID> set = ThanksList.DISABLED;
                if ( set.contains(message.id) || (set.contains(message.id) && message.type == 1) ) {
                    set.remove(message.id);
                }
                else if ( message.type == 0 ) {
                    set.add(message.id);
                }
            }, contextSupplier);
        }
    }
}
