package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;

public class EmberParticleType extends ParticleType<ColoredDynamicTypeData> {
    public EmberParticleType() {
        super(false, ColoredDynamicTypeData.DESERIALIZER);
    }

    @Override
    public Codec<ColoredDynamicTypeData> codec() {
        return ColoredDynamicTypeData.CODEC;
    }
}
