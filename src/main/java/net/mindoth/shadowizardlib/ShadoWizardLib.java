package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.network.ShadowNetwork;
import net.mindoth.shadowizardlib.registries.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@SuppressWarnings("removal")
@Mod(ShadowizardLib.MOD_ID)
public class ShadowizardLib {
    public static final String MOD_ID = "shadowizardlib";

    public ShadowizardLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if ( FMLEnvironment.dist == Dist.CLIENT ) ShadowizardLibClient.registerHandlers();
        addRegistries(modEventBus);
        modEventBus.addListener(this::commonSetup);
    }
    private void addRegistries(final IEventBus modEventBus) {
        ModParticles.PARTICLES.register(modEventBus);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        ShadowNetwork.init();
    }
}
