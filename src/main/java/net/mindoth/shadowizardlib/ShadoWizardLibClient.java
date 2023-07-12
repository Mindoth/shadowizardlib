package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.registries.KeyBinds;
import net.mindoth.shadowizardlib.util.ThanksList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ShadoWizardLibClient {

    public static void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ShadoWizardLibClient::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        KeyBinds.register(event);
        MinecraftForge.EVENT_BUS.addListener(ShadoWizardLibClient::tick);
        ThanksList.init();
    }

    public static long ticks = 0;
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
        }
    }
}
