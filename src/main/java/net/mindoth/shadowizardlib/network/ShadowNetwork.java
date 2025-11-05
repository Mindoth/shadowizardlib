package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ShadowizardLib.MOD_ID)
public class ShadowNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar payloadRegistrar = event.registrar(ShadowizardLib.MOD_ID).versioned("3.0.0").optional();

        payloadRegistrar.playToClient(SyncClientEffectsPacket.TYPE, SyncClientEffectsPacket.STREAM_CODEC, SyncClientEffectsPacket::handle);
        payloadRegistrar.playToServer(ToggleClientEffectsPacket.TYPE, ToggleClientEffectsPacket.STREAM_CODEC, ToggleClientEffectsPacket::handle);
        payloadRegistrar.playToClient(SendCustomParticlesPacket.TYPE, SendCustomParticlesPacket.STREAM_CODEC, SendCustomParticlesPacket::handle);
    }
}
