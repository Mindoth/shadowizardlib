package net.mindoth.shadowizardlib;

import net.mindoth.shadowizardlib.registries.ModParticles;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(ShadowizardLib.MOD_ID)
public class ShadowizardLib {
    public static final String MOD_ID = "shadowizardlib";

    public ShadowizardLib(IEventBus modBus, ModContainer modContainer) {
        if ( FMLEnvironment.dist == Dist.CLIENT ) ShadowizardLibClient.registerHandlers(modBus, modContainer);
        addRegistries(modBus);
    }

    private void addRegistries(final IEventBus modEventBus) {
        ModParticles.PARTICLES.register(modEventBus);
    }
}
