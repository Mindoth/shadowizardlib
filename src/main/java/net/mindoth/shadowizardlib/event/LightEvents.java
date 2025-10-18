package net.mindoth.shadowizardlib.event;

import net.mindoth.shadowizardlib.client.particle.ember.ParticleColor;
import net.mindoth.shadowizardlib.network.PacketSendCustomParticles;
import net.mindoth.shadowizardlib.network.ShadowNetwork;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Random;

public class LightEvents {

    public static HashMap<String, Float> defaultStats() {
        HashMap<String, Float> stats = new HashMap<>();
        stats.put("red", -1.0F);
        stats.put("green", -1.0F);
        stats.put("blue", -1.0F);
        stats.put("type", 1.0F);
        return stats;
    }

    public static ParticleColor getParticleColor(HashMap<String, Float> stats) {
        ParticleColor color = new ParticleColor(Mth.floor(stats.get("red")), Mth.floor(stats.get("green")), Mth.floor(stats.get("blue")));
        if ( color.getRed() < 0 || color.getRed() > 255 || color.getGreen() < 0 || color.getGreen() > 255 || color.getBlue() < 0 || color.getBlue() > 255 ) {
            int r = new Random().nextInt(0, 256);
            int g = new Random().nextInt(0, 256);
            int b = new Random().nextInt(0, 256);
            return new ParticleColor(r, g, b);
        }
        else return color;
    }

    public static int getParticleType(HashMap<String, Float> stats) {
        return (int)Math.floor(stats.get("type"));
    }

    public static void generateParticles(Vec3 pos, Vec3 center, Level level, float size, int age, double vecX, double vecY, double vecZ, HashMap<String, Float> stats) {
        ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
        ShadowNetwork.sendToNearby(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getParticleType(stats),
                pos.x, pos.y, pos.z, vecX, vecY, vecZ), level, center);
    }

    public static void summonParticleLine(Vec3 startPos, Vec3 endPos, int amount, Vec3 center, Level level, float size, int age, HashMap<String, Float> stats) {
        double startX = startPos.x;
        double startY = startPos.y;
        double startZ = startPos.z;
        double endX = endPos.x;
        double endY = endPos.y;
        double endZ = endPos.z;
        double speed = 0.05D;
        for ( int k = 1; k < (1 + amount); k++ ) {
            double vecX = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecY = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double vecZ = new Random().nextDouble(1.0D - -1.0D) + -1.0D;
            double lineX = startX * (1 - ((double) k / amount)) + endX * ((double) k / amount);
            double lineY = startY * (1 - ((double) k / amount)) + endY * ((double) k / amount);
            double lineZ = startZ * (1 - ((double) k / amount)) + endZ * ((double) k / amount);
            generateParticles(new Vec3(lineX, lineY, lineZ), center, level, size, age, vecX * speed, vecY * speed, vecZ * speed, stats);
        }
    }

    public static void addEnchantParticles(Entity target, float size, HashMap<String, Float> stats) {
        double var = 0.15D;
        double maxX = target.getBoundingBox().maxX + var;
        double minX = target.getBoundingBox().minX - var;
        double maxZ = target.getBoundingBox().maxZ + var;
        double minZ = target.getBoundingBox().minZ - var;
        double vecX = 0.0D;
        double minVecY = 0.10D;
        double maxVecY = 0.60D;
        double vecZ = 0.0D;
        for ( int i = 0; i < 4; i++ ) {
            double vecY = minVecY + (maxVecY - minVecY) * new Random().nextDouble();
            int age = 11 - (int)((vecY - 0.1D) * 20);
            double randX = maxX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ShadowNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getParticleType(stats),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double vecY = minVecY + (maxVecY - minVecY) * new Random().nextDouble();
            int age = 11 - (int)((vecY - 0.1D) * 20);
            double randX = minX;
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ + (maxZ - minZ) * new Random().nextDouble();
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ShadowNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getParticleType(stats),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double vecY = minVecY + (maxVecY - minVecY) * new Random().nextDouble();
            int age = 11 - (int)((vecY - 0.1D) * 20);
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = minZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ShadowNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getParticleType(stats),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
        for ( int i = 0; i < 4; i++ ) {
            double vecY = minVecY + (maxVecY - minVecY) * new Random().nextDouble();
            int age = 11 - (int)((vecY - 0.1D) * 20);
            double randX = minX + (maxX - minX) * new Random().nextDouble();
            double randY = target.getY() + ((target.getY() + (target.getBbHeight() / 2)) - target.getY()) * new Random().nextDouble();
            double randZ = maxZ;
            Vec3 pos = new Vec3(randX, randY, randZ);
            ParticleColor.IntWrapper color = new ParticleColor.IntWrapper(getParticleColor(stats));
            ShadowNetwork.sendToPlayersTrackingEntity(new PacketSendCustomParticles(color.r, color.g, color.b, size, age, false, getParticleType(stats),
                    pos.x, pos.y, pos.z, vecX, vecY, vecZ), target, true);
        }
    }

    public static void addAoeParticles(boolean targetBlocks, Level level, AABB box, float size, int age, HashMap<String, Float> stats) {
        Vec3 center = box.getCenter();
        double maxX = box.maxX;
        double minX = box.minX;
        double maxY = box.maxY;
        double minY = box.minY;
        double maxZ = box.maxZ;
        double minZ = box.minZ;

        if ( targetBlocks ) {
            int amountX = 4 * (int)box.getXsize();
            int amountY = 4 * (int)box.getYsize();
            int amountZ = 4 * (int)box.getZsize();

            //VectorPos for each corner
            Vec3 pos0 = new Vec3(minX, minY, minZ);
            Vec3 pos1 = new Vec3(maxX, minY, minZ);
            Vec3 pos2 = new Vec3(minX, minY, maxZ);
            Vec3 pos3 = new Vec3(maxX, minY, maxZ);
            Vec3 pos4 = new Vec3(minX, maxY, minZ);
            Vec3 pos5 = new Vec3(maxX, maxY, minZ);
            Vec3 pos6 = new Vec3(minX, maxY, maxZ);
            Vec3 pos7 = new Vec3(maxX, maxY, maxZ);
            //Bottom corners
            generateParticles(pos0, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos1, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos2, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos3, center, level, size, age, 0, 0, 0, stats);
            //Top corners
            generateParticles(pos4, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos5, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos6, center, level, size, age, 0, 0, 0, stats);
            generateParticles(pos7, center, level, size, age, 0, 0, 0, stats);
            //Bottom edges
            summonParticleLine(pos0, pos1, amountX, center, level, size, age, stats);
            summonParticleLine(pos0, pos2, amountZ, center, level, size, age, stats);
            summonParticleLine(pos3, pos1, amountZ, center, level, size, age, stats);
            summonParticleLine(pos3, pos2, amountX, center, level, size, age, stats);
            //Middle edges
            summonParticleLine(pos0, pos4, amountY, center, level, size, age, stats);
            summonParticleLine(pos1, pos5, amountY, center, level, size, age, stats);
            summonParticleLine(pos2, pos6, amountY, center, level, size, age, stats);
            summonParticleLine(pos3, pos7, amountY, center, level, size, age, stats);
            //Top edges
            summonParticleLine(pos4, pos5, amountX, center, level, size, age, stats);
            summonParticleLine(pos4, pos6, amountZ, center, level, size, age, stats);
            summonParticleLine(pos7, pos5, amountZ, center, level, size, age, stats);
            summonParticleLine(pos7, pos6, amountX, center, level, size, age, stats);
        }
        else {
            int amount = 4 * Math.max((int)box.getYsize(), (int)box.getXsize());
            for ( int i = 0; i < amount; i++ ) {
                double vec = 0.05D + (0.25D - 0.05D) * new Random().nextDouble();
                //double vec = 0.15D;
                generateParticles(new Vec3(maxX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX, center.y - 0.5D + new Random().nextDouble(), minZ + (maxZ - minZ) * new Random().nextDouble()), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), minZ), center, level, size, age, 0, vec, 0, stats);
                generateParticles(new Vec3(minX + (maxX - minX) * new Random().nextDouble(), center.y - 0.5D + new Random().nextDouble(), maxZ), center, level, size, age, 0, vec, 0, stats);
            }
        }
    }
}
