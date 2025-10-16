package net.mindoth.shadowizardlib.network;

import net.mindoth.shadowizardlib.client.particle.ember.EmberParticleProvider;
import net.mindoth.shadowizardlib.client.particle.ember.ParticleColor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSendCustomParticles {

    public int r;
    public int g;
    public int b;
    public float size;
    public int age;
    public boolean fade;
    public int renderType;
    public double x;
    public double y;
    public double z;
    public double vx;
    public double vy;
    public double vz;

    public PacketSendCustomParticles(int r, int g, int b, float size, int age, boolean fade, int renderType, double x, double y, double z, double vx, double vy, double vz) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.size = size;
        this.age = age;
        this.fade = fade;
        this.renderType = renderType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public PacketSendCustomParticles(FriendlyByteBuf buf) {
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.size = buf.readFloat();
        this.age = buf.readInt();
        this.fade = buf.readBoolean();
        this.renderType = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.vx = buf.readDouble();
        this.vy = buf.readDouble();
        this.vz = buf.readDouble();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.r);
        buf.writeInt(this.g);
        buf.writeInt(this.b);
        buf.writeFloat(this.size);
        buf.writeInt(this.age);
        buf.writeBoolean(this.fade);
        buf.writeInt(this.renderType);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeDouble(this.vx);
        buf.writeDouble(this.vy);
        buf.writeDouble(this.vz);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.level.addParticle(EmberParticleProvider.createData(new ParticleColor(this.r, this.g, this.b), this.size, this.age, this.fade, this.renderType),
                    this.x, this.y, this.z, this.vx, this.vy, this.vz);
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
