package net.mindoth.shadowizardlib.client.particle.ember;

import net.mindoth.shadowizardlib.registries.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

public class EmberParticleProvider implements ParticleProvider<ColoredDynamicTypeData> {

    private final SpriteSet spriteSet;
    public static final String NAME = "ember";

    public EmberParticleProvider(SpriteSet sprite) {
        this.spriteSet = sprite;
    }

    @Override
    public Particle createParticle(ColoredDynamicTypeData data, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        return new ParticleEmber(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data.color.getRed(), data.color.getGreen(), data.color.getBlue(),
                data.scale, data.age, data.fade, data.renderType, this.spriteSet);
    }

    public static ParticleOptions createData(ParticleColor color, float scale, int age, boolean fade, int renderType) {
        return new ColoredDynamicTypeData(ModParticles.EMBER_TYPE.get(), color, scale, age, fade, renderType);
    }
}
