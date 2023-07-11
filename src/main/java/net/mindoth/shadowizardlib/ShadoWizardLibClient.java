package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.network.KeyPressMessage;
import net.mindoth.shadowizardlib.registries.KeyBinds;
import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.mindoth.shadowizardlib.network.SupporterDisableMessage;
import net.mindoth.shadowizardlib.util.ThanksList;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ShadoWizardLibClient {

    public static void registerHandlers() {
        ThanksList.init();
        MinecraftForge.EVENT_BUS.addListener(ShadoWizardLibClient::tick);
    }

    @Mod.EventBusSubscriber(modid = ShadoWizardLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.TOGGLE);
        }
    }

    @Mod.EventBusSubscriber(modid = ShadoWizardLib.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;
            onInput(mc, event.getKey(), event.getAction());
        }

        private static void onInput(Minecraft mc, int key, int action) {
            if (mc.screen == null && KeyBinds.TOGGLE.isDown()) {
                ShadowizardNetwork.CHANNEL.sendToServer(new KeyPressMessage(key));
            }
        }
    }

    public static long ticks = 0;
    public static void tick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ticks++;
        }
    }
}
