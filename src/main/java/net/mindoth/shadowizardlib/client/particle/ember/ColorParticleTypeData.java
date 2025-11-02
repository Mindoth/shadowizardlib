package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mindoth.shadowizardlib.registries.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ColorParticleTypeData implements ParticleOptions {

    protected ParticleType<? extends ColorParticleTypeData> type;
    public static final MapCodec<ColorParticleTypeData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age),
                    Codec.BOOL.fieldOf("fade").forGetter(d -> d.fade),
                    Codec.INT.fieldOf("mask").forGetter(d -> d.renderType)
            )
            .apply(instance, ColorParticleTypeData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ColorParticleTypeData> STREAM_CODEC = StreamCodec.of(
            ColorParticleTypeData::toNetwork, ColorParticleTypeData::fromNetwork
    );

    public static void toNetwork(RegistryFriendlyByteBuf buf, ColorParticleTypeData data) {
        buf.writeFloat(data.color.getRed());
        buf.writeFloat(data.color.getGreen());
        buf.writeFloat(data.color.getBlue());
        buf.writeFloat(data.scale);
        buf.writeInt(data.age);
        buf.writeBoolean(data.fade);
        buf.writeInt(data.renderType);
    }

    public static ColorParticleTypeData fromNetwork(RegistryFriendlyByteBuf buffer) {
        float r = buffer.readFloat();
        float g = buffer.readFloat();
        float b = buffer.readFloat();
        float scale = buffer.readFloat();
        int age = buffer.readInt();
        boolean fade = buffer.readBoolean();
        int type = buffer.readInt();
        return new ColorParticleTypeData(r, g, b, scale, age, fade, type);
    }

    public ParticleColor color;
    float scale;
    int age;
    boolean fade;
    int renderType;

    public ColorParticleTypeData(float r, float g, float b, float scale, int age, boolean fade, int type) {
        this(ModParticles.EMBER_TYPE.get(), new ParticleColor(r, g, b), scale, age, fade, type);
    }

    public ColorParticleTypeData(ParticleType<? extends ColorParticleTypeData> particleTypeData, ParticleColor color, float scale, int age, boolean fade, int renderType) {
        this.type = particleTypeData;
        this.color = color;
        this.scale = scale;
        this.age = age;
        this.fade = fade;
        this.renderType = renderType;
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }
}
