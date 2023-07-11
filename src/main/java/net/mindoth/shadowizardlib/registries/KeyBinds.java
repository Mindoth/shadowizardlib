package net.mindoth.shadowizardlib.registries;

import net.mindoth.shadowizardlib.ShadoWizardLib;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.awt.event.KeyEvent;

@OnlyIn(Dist.CLIENT)
public class KeyBinds {
    public static KeyBinding TOGGLE;

    public static void register(final FMLClientSetupEvent event) {
        TOGGLE = create("toggle", KeyEvent.VK_I);

        ClientRegistry.registerKeyBinding(TOGGLE);
    }

    private static KeyBinding create(String name, int key) {
        return new KeyBinding("key." + ShadoWizardLib.MOD_ID + "." + name, key, "key.category." + ShadoWizardLib.MOD_ID);
    }
}
