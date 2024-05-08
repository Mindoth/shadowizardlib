package net.mindoth.shadowizardlib.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShadowEvents {

    //Pass any LivingEntity into this and you get its hitbox' centre
    public static Vector3d getEntityCenter(Entity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        List<Entity> targets = center.level.getEntities(center, center.getBoundingBox().inflate(x, y, z));
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

    //Get an arraylist of LivingEntities around the given entity. Pass the centerpoint entity and size its' boundingbox will be extended
    public static ArrayList<LivingEntity> getEntitiesAround(Entity caster, World pLevel, double size, @Nullable List<LivingEntity> exceptions) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(size));
        if ( exceptions != null && !exceptions.isEmpty() ) targets.removeIf(exceptions::contains);
        return targets;
    }

    //Get nearest LivingEntity to given entity
    public static Entity getNearestEntity(Entity player, World pLevel, double size, @Nullable List<LivingEntity> exceptions) {
        ArrayList<LivingEntity> targets = getEntitiesAround(player, pLevel, size, exceptions);
        LivingEntity target = null;
        double lowestSoFar = Double.MAX_VALUE;
        for ( LivingEntity closestSoFar : targets ) {
            if ( !closestSoFar.isAlliedTo(player) ) {
                double testDistance = player.distanceTo(closestSoFar);
                if ( testDistance < lowestSoFar ) {
                    target = closestSoFar;
                }
            }
        }
        return target;
    }

    //Custom method for getting a LivingEntity the player is looking at. If no target is found returns caster itself
    //Range is the distance entities are taken into account
    //Error is the distance the player's crosshair can be from target and still be recognised as "looking at it"
    //Technically "caster" can be any entity but using a Player is recommended
    public static Entity getPointedEntity(World level, Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vector3d direction = calculateViewVector(caster.xRot * adjuster, caster.yRot * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vector3d center = caster.getEyePosition(0).add(direction);
        Entity returnEntity = caster;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = ShadowEvents.getEntityCenter(caster).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            //float error = 0.25F;
            Vector3d start = new Vector3d(lineX + error, lineY + error, lineZ + error);
            Vector3d end = new Vector3d(lineX - error, lineY - error, lineZ - error);
            AxisAlignedBB area = new AxisAlignedBB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity ) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) {
                        target = closestSoFar;
                    }
                }
            }
            if ( target != null ) {
                returnEntity = target;
                break;
            }
        }
        return returnEntity;
    }

    public static Vector3d calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        double f2 = Math.cos(f1);
        double f3 = Math.sin(f1);
        double f4 = Math.cos(f);
        double f5 = Math.sin(f);
        return new Vector3d((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }

    public static double blockHeight(World level, Entity caster, float range, float error, int maxHeight) {
        BlockPos startPos = ShadowEvents.getPointedEntity(level, caster, range, error, true).blockPosition();
        double returnHeight = startPos.getY();
        for ( int i = startPos.getY() + 1; i < (startPos.getY() + maxHeight); i++ ) {
            BlockPos testBlockPost = new BlockPos(startPos.getX(), i, startPos.getZ());
            if ( level.getBlockState(testBlockPost).getMaterial().isSolid() || i == (startPos.getY() + maxHeight - 1) ) {
                returnHeight = i;
                break;
            }
        }
        return returnHeight;
    }

    public static Vector3d getPoint(World level, Entity caster, float range, float error, boolean isPlayer, boolean centerBlock, boolean stopsAtEntity, boolean stopsAtSolid) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;

        Vector3d direction = ShadowEvents.calculateViewVector(caster.xRot * (float)adjuster, caster.yRot * (float)adjuster).normalize();
        direction = direction.multiply((double)range, (double)range, (double)range);
        Vector3d center = caster.getEyePosition(1.0F).add(direction);
        Vector3d returnPoint = center;
        double playerX = ShadowEvents.getEntityCenter(caster).x;
        double playerY = caster.getEyePosition(1.0F).y;
        double playerZ = ShadowEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));

        for ( int k = 1; k < 1 + particleInterval; ++k ) {
            double lineX = playerX * (1.0D - (double)k / (double)particleInterval) + listedEntityX * ((double)k / (double)particleInterval);
            double lineY = playerY * (1.0D - (double)k / (double)particleInterval) + listedEntityY * ((double)k / (double)particleInterval);
            double lineZ = playerZ * (1.0D - (double)k / (double)particleInterval) + listedEntityZ * ((double)k / (double)particleInterval);
            //((ServerWorld)level).sendParticles(ParticleTypes.FLAME, lineX, lineY, lineZ, 0, 0, 0, 0, 0);
            Vector3d start = new Vector3d(lineX + error, lineY + error, lineZ + error);
            Vector3d end = new Vector3d(lineX - error, lineY - error, lineZ - error);
            AxisAlignedBB area = new AxisAlignedBB(start, end);
            List<Entity> targets = level.getEntities(caster, area);
            Entity target = null;
            double lowestSoFar = Double.MAX_VALUE;
            for ( Entity closestSoFar : targets ) {
                if ( closestSoFar instanceof LivingEntity) {
                    double testDistance = closestSoFar.distanceToSqr(center);
                    if ( testDistance < lowestSoFar ) {
                        target = closestSoFar;
                    }
                }
            }
            if ( stopsAtEntity && target != null ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos((int)returnPoint.x, (int)returnPoint.y, (int)returnPoint.z);
                    double multX = pos.getX() < 0 ? -1 : 1;
                    double multZ = pos.getZ() < 0 ? -1 : 1;
                    returnPoint = new Vector3d(pos.getX() + (0.5D * multX), pos.getY() + 0.5D, pos.getZ() + (0.5D * multZ));
                }
                break;
            }
            if ( stopsAtSolid && caster.level.getBlockState(new BlockPos(lineX, lineY, lineZ)).getMaterial().isSolid() ) {
                if ( centerBlock ) {
                    BlockPos pos = new BlockPos((int)returnPoint.x, (int)returnPoint.y, (int)returnPoint.z);
                    double multX = pos.getX() < 0 ? -1 : 1;
                    double multZ = pos.getZ() < 0 ? -1 : 1;
                    returnPoint = new Vector3d(pos.getX() + (0.5D * multX), pos.getY() + 0.5D, pos.getZ() + (0.5D * multZ));
                }
                break;
            }
            returnPoint = new Vector3d(lineX, lineY, lineZ);
        }
        return returnPoint;
    }
}
