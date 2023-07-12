package net.mindoth.shadowizardlib.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class MessageHelper {

    public static <T> void registerMessage(SimpleChannel channel, int id, MessageProvider<T> prov) {
        channel.registerMessage(id, prov.getMsgClass(), prov::encode, prov::decode, prov::handle);
    }

    public static void handlePacket(Supplier<Runnable> r, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(r.get());
        ctx.setPacketHandled(true);
    }
}
