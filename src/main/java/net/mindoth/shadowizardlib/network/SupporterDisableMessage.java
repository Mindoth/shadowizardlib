package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.util.ThanksList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class SupporterDisableMessage {

    private UUID id;

    public SupporterDisableMessage() {
    }

    public SupporterDisableMessage(UUID id) {
        this.id = id;
    }

    public static void encode(SupporterDisableMessage message, PacketBuffer buffer) {
        if (message.id != null) buffer.writeUUID(message.id);
    }

    public static SupporterDisableMessage decode(PacketBuffer buffer) {
        return new SupporterDisableMessage(buffer.readUUID());
    }

    public static void handle(SupporterDisableMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        Set<UUID> set = ThanksList.DISABLED;
        UUID id = message.id;
        if ( set.contains(id) ) {
            set.remove(id);
        }
        else {
            set.add(id);
        }
    }
}
