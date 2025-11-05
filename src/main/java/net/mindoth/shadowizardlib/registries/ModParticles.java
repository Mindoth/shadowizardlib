package net.mindoth.shadowizardlib.registries;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.mindoth.shadowizardlib.client.particle.ember.ColorParticleTypeData;
import net.mindoth.shadowizardlib.client.particle.ember.EmberParticleProvider;
import net.mindoth.shadowizardlib.client.particle.ember.EmberParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ShadowizardLib.MOD_ID, value = Dist.CLIENT)
public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ShadowizardLib.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<ColorParticleTypeData>> EMBER_TYPE = PARTICLES.register(EmberParticleProvider.NAME, EmberParticleType::new);

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(EMBER_TYPE.get(), EmberParticleProvider::new);
        //Minecraft.getInstance().particleEngine.register(EMBER_TYPE.get(), EmberParticleProvider::new);
    }
}
