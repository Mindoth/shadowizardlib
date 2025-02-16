package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.client.KeyBinds;
import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ShadowizardLibClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ShadowizardLibClient::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(ShadowizardLibClient::tick);
        ThanksList.init();
    }

    @Mod.EventBusSubscriber(modid = ShadowizardLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.TOGGLE);
        }
    }

    public static long ticks = 0;

    public static void tick(TickEvent.ClientTickEvent event) {
        if ( event.phase == TickEvent.Phase.END ) ticks++;
    }
}
