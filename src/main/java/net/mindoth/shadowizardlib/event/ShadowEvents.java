package net.mindoth.shadowizardlib.event;

import com.google.common.collect.Lists;
import net.mindoth.shadowizardlib.client.particle.ember.ParticleColor;
import net.mindoth.shadowizardlib.network.PacketSendCustomParticles;
import net.mindoth.shadowizardlib.network.ShadowNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiPredicate;

public class ShadowEvents {

    @Nullable
    public static Entity getEntityByUUID(Level level, @Nullable UUID uuid) {
        if ( uuid == null || !(level instanceof ServerLevel serverLevel) ) return null;
        for ( Entity entity : serverLevel.getAllEntities() ) if ( entity != null && entity.getUUID().equals(uuid) ) return entity;
        return null;
    }

    //Pass any LivingEntity into this and you get its hitbox' centre
    public static Vec3 getEntityCenter(Entity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        List<Entity> targets = center.level().getEntities(center, center.getBoundingBox().inflate(x, y, z));
        return targets;
    }

    //Pass an item attribute and the uuid for said attribute, and it will be removed. Usually used inside ItemAttributeModifierEvent for changing attack damage/speed of item
    public static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Attribute attribute, UUID baseUUID) {
        event.getOriginalModifiers()
                .get(attribute)
                .stream()
                .filter(modifier -> modifier.getId() == baseUUID)
                .findAny()
                .ifPresent(modifier -> event.removeModifier(attribute, modifier));
    }

    //Get LivingEntities around the given entity. Pass the centerpoint entity and size its' boundingbox will be extended
    public static List<Entity> getEntitiesAround(Entity caster, Level pLevel, double size, @Nullable BiPredicate<Entity, Entity> filter) {
        List<Entity> targets = pLevel.getEntitiesOfClass(Entity.class, caster.getBoundingBox().inflate(size));
        if ( filter != null ) {
            List<Entity> filteredTargets = Lists.newArrayList();
            for ( Entity entity : targets ) if ( filter.test(caster, entity) ) filteredTargets.add(entity);
            targets = filteredTargets;
        }
        return targets;
    }

    //Get nearest LivingEntity to given entity
    public static Entity getNearestEntity(Entity caster, Level pLevel, double size, @Nullable BiPredicate<Entity, Entity> filter) {
        List<Entity> targets = getEntitiesAround(caster, pLevel, size, filter);
        Entity target = null;
        double lowestSoFar = Double.MAX_VALUE;
        for ( Entity closestSoFar : targets ) {
            double testDistance = caster.distanceTo(closestSoFar);
            if ( testDistance < lowestSoFar ) target = closestSoFar;
        }
        return target;
    }

    //Custom method for getting a LivingEntity the player is looking at. If no target is found returns caster itself
    //Range is the distance entities are taken into account
    //Error is the distance the player's crosshair can be from target and still be considered as "looking at it"
    //Technically "caster" can be any entity but using a Player is recommended
    public static Entity getPointedEntity(Level level, Entity caster, float range, float error, boolean stopsAtSolid, @Nullable BiPredicate<Entity, Entity> filter) {
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot(), caster.getYRot()).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition().add(direction);
        Entity returnEntity = caster;
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            //((ServerLevel)level).sendParticles(ParticleTypes.FLAME, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( filter == null || filter.test(caster, closestSoFar) ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( target != null ) {
                returnEntity = target;
                break;
            }
            if ( stopsAtSolid && caster.level().getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) break;
        }
        return returnEntity;
    }

    private Entity getPointedEntity(Vec3 position, Vec3 direction, Entity caster, Level level, float range, float error, boolean stopsAtSolid, @Nullable BiPredicate<Entity, Entity> filter) {
        Vec3 center = position.add(direction.multiply(range, range, range));
        Entity returnEntity = null;
        double playerX = position.x();
        double playerY = position.y();
        double playerZ = position.z();
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(position.distanceToSqr(center));
        Vec3 startPos = position.add(direction);
        Vec3 endPos = center;
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            endPos = new Vec3(lineX, lineY, lineZ);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( filter == null || filter.test(caster, closestSoFar) ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( target != null ) {
                returnEntity = target;
                break;
            }
            if ( stopsAtSolid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) break;
        }
        return returnEntity;
    }

    public static Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        double f2 = Math.cos(f1);
        double f3 = Math.sin(f1);
        double f4 = Math.cos(f);
        double f5 = Math.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public static double blockHeight(Level level, Entity caster, float range, float error, int maxHeight, boolean stopsAtSolid) {
        BlockPos startPos = ShadowEvents.getPointedEntity(level, caster, range, error, stopsAtSolid, null).blockPosition();
        double returnHeight = startPos.getY();
        for ( int i = startPos.getY() + 1; i < (startPos.getY() + maxHeight); i++ ) {
            BlockPos testBlockPost = new BlockPos(startPos.getX(), i, startPos.getZ());
            if ( level.getBlockState(testBlockPost).isSolid() || i == (startPos.getY() + maxHeight - 1) ) {
                returnHeight = i;
                break;
            }
        }
        return returnHeight;
    }

    //Custom ray-tracing method
    public static Vec3 getPoint(Level level, Entity caster, float range, float error, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid, boolean stopsAtLiquid) {
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot(), caster.getYRot()).normalize();
        direction = direction.multiply((double)range, (double)range, (double)range);
        Vec3 center = caster.getEyePosition().add(direction);
        Vec3 returnPoint = center;
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            //((ServerLevel)level).sendParticles(ParticleTypes.FLAME, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( stopsAtEntity && target != null ) {
                if ( centerBlock ) {
                    BlockPos pos = target.blockPosition();
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtLiquid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).getBlock() instanceof LiquidBlock ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtSolid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            returnPoint = new Vec3(lineX, lineY, lineZ);
        }
        return returnPoint;
    }

    public static Vec3 getPoint(Vec3 position, Vec3 direction, Entity caster, Level level, float range, float error, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid, boolean stopsAtLiquid) {
        direction = direction.multiply(range, range, range);
        Vec3 center = position.add(direction);
        Vec3 returnPoint = center;
        double playerX = position.x();
        double playerY = position.y();
        double playerZ = position.z();
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(position.distanceToSqr(center));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) target = closestSoFar;
                }
            }
            if ( stopsAtEntity && target != null ) {
                if ( centerBlock ) {
                    BlockPos pos = target.blockPosition();
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtLiquid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).getBlock() instanceof LiquidBlock) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            if ( stopsAtSolid && level.getBlockState(new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ))).isSolid() ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos(Mth.floor(returnPoint.x), Mth.floor(returnPoint.y), Mth.floor(returnPoint.z));
                    returnPoint = pos.getCenter();
                }
                break;
            }
            returnPoint = new Vec3(lineX, lineY, lineZ);
        }
        return returnPoint;
    }

    //Use this instead if you need the blockpos of a block you're looking at
    public static BlockPos getBlockPoint(Entity caster, float range) {
        Vec3 direction = ShadowEvents.calculateViewVector(caster.getXRot(), caster.getYRot()).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition().add(direction);
        double playerX = caster.getEyePosition().x;
        double playerY = caster.getEyePosition().y;
        double playerZ = caster.getEyePosition().z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        BlockPos blockPos = new BlockPos(Mth.floor(center.x), Mth.floor(center.y), Mth.floor(center.z));
        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0 - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0 - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0 - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);

            BlockPos tempPos = new BlockPos(Mth.floor(lineX), Mth.floor(lineY), Mth.floor(lineZ));
            if ( caster.level().getBlockState(tempPos).isSolid() ) {
                blockPos = tempPos;
                break;
            }
        }
        return blockPos;
    }
}
