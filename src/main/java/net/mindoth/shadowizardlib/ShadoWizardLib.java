package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.util.ThanksList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(ShadoWizardLib.MOD_ID)
public class ShadoWizardLib {
    public static final String MOD_ID = "shadowizardlib";

    public ShadoWizardLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ShadoWizardLibClient.registerHandlers();
        }
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
    }
    private void serverAboutToStart(MinecraftServer server) {

        if (server.isDedicatedServer()) {
            ThanksList.firstStart();
        }
    }
}
