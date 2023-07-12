package net.mindoth.shadowizardlib.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface MessageProvider<T> {

    @SuppressWarnings("unchecked")
    default Class<T> getMsgClass() {
        return (Class<T>) this.getClass();
    }

    public abstract void encode(T msg, PacketBuffer buf);

    public abstract T decode(PacketBuffer buf);

    public abstract void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}
