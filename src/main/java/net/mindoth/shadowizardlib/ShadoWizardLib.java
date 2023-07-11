package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        addRegistries(modEventBus);
        //IEventBus bus = MinecraftForge.EVENT_BUS;
        //bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
    }
    private void addRegistries(final IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
    }
    public void commonSetup(final FMLCommonSetupEvent event) {
        ShadowizardNetwork.init();
    }
    /*private void serverAboutToStart(MinecraftServer server) {

        if (server.isDedicatedServer()) {
            ThanksList.firstStart();
        }
    }*/
}
