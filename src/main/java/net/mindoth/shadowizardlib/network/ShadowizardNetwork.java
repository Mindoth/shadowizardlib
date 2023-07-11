package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.ShadoWizardLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ShadowizardNetwork {
    public static final String NETWORK_VERSION = "0.1.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ShadoWizardLib.MOD_ID, "network"), () -> NETWORK_VERSION,
            version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, KeyPressMessage.class, KeyPressMessage::encode, KeyPressMessage::decode, KeyPressMessage::handle);
        CHANNEL.registerMessage(1, SupporterDisableMessage.class, SupporterDisableMessage::encode, SupporterDisableMessage::decode, SupporterDisableMessage::handle);
    }
}
