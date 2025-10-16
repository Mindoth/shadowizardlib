package net.mindoth.shadowizardlib.client.particle.ember;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mindoth.shadowizardlib.registries.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

public class ColoredDynamicTypeData implements ParticleOptions {

    private ParticleType<ColoredDynamicTypeData> type;
    public ParticleColor color;
    float scale;
    int age;
    boolean fade;
    int renderType;

    public static final Codec<ColoredDynamicTypeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.fieldOf("r").forGetter(d -> d.color.getRed()),
                    Codec.FLOAT.fieldOf("g").forGetter(d -> d.color.getGreen()),
                    Codec.FLOAT.fieldOf("b").forGetter(d -> d.color.getBlue()),
                    Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale),
                    Codec.INT.fieldOf("age").forGetter(d -> d.age),
                    Codec.BOOL.fieldOf("fade").forGetter(d -> d.fade),
                    Codec.INT.fieldOf("mask").forGetter(d -> d.renderType)
            )
            .apply(instance, ColoredDynamicTypeData::new));

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    static final Deserializer<ColoredDynamicTypeData> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColoredDynamicTypeData fromCommand(ParticleType<ColoredDynamicTypeData> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ColoredDynamicTypeData(type, ParticleColor.deserialize(reader.readString()), reader.readFloat(), reader.readInt(), reader.readBoolean(), reader.readInt());
        }

        @Override
        public ColoredDynamicTypeData fromNetwork(ParticleType<ColoredDynamicTypeData> type, FriendlyByteBuf buffer) {
            return new ColoredDynamicTypeData(type, ParticleColor.deserialize(buffer.readUtf()), buffer.readFloat(), buffer.readInt(), buffer.readBoolean(), buffer.readInt());
        }
    };

    public ColoredDynamicTypeData(float r, float g, float b, float scale, int age, boolean fade, int renderType) {
        this.type = ModParticles.EMBER_TYPE.get();
        this.color = new ParticleColor(r, g, b);
        this.scale = scale;
        this.age = age;
        this.fade = fade;
        this.renderType = renderType;
    }

    public ColoredDynamicTypeData(ParticleType<ColoredDynamicTypeData> particleTypeData, ParticleColor color, float scale, int age, boolean fade, int renderType) {
        this.type = particleTypeData;
        this.color = color;
        this.scale = scale;
        this.age = age;
        this.fade = fade;
        this.renderType = renderType;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.color.serialize());
        buffer.writeFloat(this.scale);
        buffer.writeInt(this.age);
        buffer.writeBoolean(this.fade);
        buffer.writeByte(this.renderType);
    }

    @Override
    public String writeToString() {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.type).toString() + " " + this.color.serialize() + " " + this.scale + " " + this.age + " " + this.fade + " " + this.renderType;
    }
}
