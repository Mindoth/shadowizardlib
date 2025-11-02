package net.mindoth.shadowizardlib.registries;

import net.mindoth.shadowizardlib.ShadowizardLib;
import net.mindoth.shadowizardlib.client.particle.ember.ColorParticleTypeData;
import net.mindoth.shadowizardlib.client.particle.ember.EmberParticleProvider;
import net.mindoth.shadowizardlib.client.particle.ember.EmberParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ShadowizardLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ShadowizardLib.MOD_ID);

    public static final RegistryObject<ParticleType<ColorParticleTypeData>> EMBER_TYPE = PARTICLES.register(EmberParticleProvider.NAME, EmberParticleType::new);

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(EMBER_TYPE.get(), EmberParticleProvider::new);
    }
}
