package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.event.ThanksList;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

public class ShadowizardLibClient {

    public static void registerHandlers(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(ShadowizardLibClient::clientSetup);
    }

    private static void clientSetup(final FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(ShadowizardLibClient::tick);
        ThanksList.init();
    }

    @EventBusSubscriber(modid = ShadowizardLib.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        public static final String KEY_TOGGLE = "shadowizardlib.toggle";
        public static final String KEY_CATEGORY_SHADOWIZARDLIB = "key.category.shadowizardlib";
        public static final KeyMapping TOGGLE = new KeyMapping(KEY_TOGGLE, GLFW.GLFW_KEY_I, KEY_CATEGORY_SHADOWIZARDLIB);

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(TOGGLE);
        }
    }

    public static long ticks = 0;

    public static void tick(ClientTickEvent.Post event) {
        ticks++;
    }
}
