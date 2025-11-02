package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class EmberParticleType extends ParticleType<ColorParticleTypeData> {

    public EmberParticleType() {
        super(false);
    }

    @Override
    public MapCodec<ColorParticleTypeData> codec() {
        return ColorParticleTypeData.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleTypeData> streamCodec() {
        return ColorParticleTypeData.STREAM_CODEC;
    }
}
