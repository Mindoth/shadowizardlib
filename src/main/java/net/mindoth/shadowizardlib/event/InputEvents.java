package net.mindoth.shadowizardlib.event;

import net.mindoth.shadowizardlib.ShadoWizardLib;
import net.mindoth.shadowizardlib.network.KeyPressMessage;
import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.mindoth.shadowizardlib.registries.KeyBinds;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShadoWizardLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {
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
