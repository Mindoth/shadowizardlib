package net.mindoth.shadowizardlib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface MessageProvider<T> {

    @SuppressWarnings("unchecked")
    default Class<T> getMsgClass() {
        return (Class<T>) this.getClass();
    }

    public abstract void encode(T msg, FriendlyByteBuf buf);

    public abstract T decode(FriendlyByteBuf buf);

    public abstract void handle(T msg, Supplier<NetworkEvent.Context> ctx);
}
