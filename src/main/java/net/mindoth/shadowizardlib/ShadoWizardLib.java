package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.client.curio.CurioLayers;
import net.mindoth.shadowizardlib.network.ShadowizardNetwork;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
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
    }
    private void addRegistries(final IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onRegisterLayerDefinitions);
    }

    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        CurioLayers.register(event);
    }
    public void commonSetup(final FMLCommonSetupEvent event) {
        ShadowizardNetwork.init();
    }
}
