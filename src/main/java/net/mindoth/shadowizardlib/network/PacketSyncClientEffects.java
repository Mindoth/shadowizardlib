package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.UUID;

public class PacketSyncClientEffects {

    //type = 0: toggle effects on player join. type = 1: toggle effects.
    private int type;
    private UUID id;

    public PacketSyncClientEffects(int type, UUID id) {
        this.type = type;
        this.id = id;
    }

    public PacketSyncClientEffects(FriendlyByteBuf buf) {
        this.type = buf.readByte();
        this.id = buf.readUUID();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.type);
        buf.writeUUID(this.id);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            if ( this.type == 0 ) ThanksList.DISABLED.add(this.id);
            else if ( this.type == 1 ) {
                if ( ThanksList.DISABLED.contains(this.id) ) ThanksList.DISABLED.remove(this.id);
                else ThanksList.DISABLED.add(this.id);
            }
        });
        context.setPacketHandled(true);
    }
}
