package net.mindoth.shadowizardlib.event;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommonEvents {

    //Pass any LivingEntity into this and you get its hitbox' centre
    public static Vec3 getEntityCenter(Entity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        List<Entity> targets = center.level.getEntities(center, center.getBoundingBox().inflate(x, y, z));
        return targets;
    }

<<<<<<< Updated upstream
    //Pass an iten attribute and the uuid for said attribute and it will be removed. Usually used inside ItemAttributeModifierEvent for changing attack damage/speed of item
    private static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Attribute attribute, UUID baseUUID) {
=======
    //Pass an item attribute and the uuid for said attribute, and it will be removed. Usually used inside ItemAttributeModifierEvent for changing attack damage/speed of item
    public static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Attribute attribute, UUID baseUUID) {
>>>>>>> Stashed changes
        event.getOriginalModifiers()
                .get(attribute)
                .stream()
                .filter(modifier -> modifier.getId() == baseUUID)
                .findAny()
                .ifPresent(modifier -> event.removeModifier(attribute, modifier));
    }

    //Get an arraylist of LivingEntities around the given entity. Pass the centerpoint entity and size its' boundingbox will be extended
    public static ArrayList<LivingEntity> getEntitiesAround(Entity caster, Level pLevel, double size) {
        ArrayList<LivingEntity> targets = (ArrayList<LivingEntity>) pLevel.getEntitiesOfClass(LivingEntity.class, caster.getBoundingBox().inflate(size));
        targets.removeIf(entry -> entry == caster || !(entry.isAttackable()) || !(entry.isAlive() || (entry instanceof Player && ((Player)entry).getAbilities().instabuild)));
        return targets;
    }

    //Get nearest LivingEntity to given entity
    public static Entity getNearestEntity(Entity player, Level pLevel, double size) {
        ArrayList<LivingEntity> targets = getEntitiesAround(player, pLevel, size);
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
    public static Entity getPointedEntity(Level level, Entity caster, float range, float error, boolean isPlayer) {
        int adjuster = 1;
        if ( !isPlayer ) adjuster = -1;
        Vec3 direction = calculateViewVector(caster.getXRot() * adjuster, caster.getYRot() * adjuster).normalize();
        direction = direction.multiply(range, range, range);
        Vec3 center = caster.getEyePosition().add(direction);
        Entity returnEntity = caster;
        double playerX = CommonEvents.getEntityCenter(caster).x;
        double playerY = CommonEvents.getEntityCenter(caster).y;
        double playerZ = CommonEvents.getEntityCenter(caster).z;
        double listedEntityX = center.x();
        double listedEntityY = center.y();
        double listedEntityZ = center.z();
        int particleInterval = (int)Math.round(caster.distanceToSqr(center));
        for ( int k = 1; k < (1 + particleInterval); k++ ) {
            double lineX = playerX * (1 - ((double) k / particleInterval)) + listedEntityX * ((double) k / particleInterval);
            double lineY = playerY * (1 - ((double) k / particleInterval)) + listedEntityY * ((double) k / particleInterval);
            double lineZ = playerZ * (1 - ((double) k / particleInterval)) + listedEntityZ * ((double) k / particleInterval);
            //float error = 0.25F;
            Vec3 start = new Vec3(lineX + error, lineY + error, lineZ + error);
            Vec3 end = new Vec3(lineX - error, lineY - error, lineZ - error);
            AABB area = new AABB(start, end);
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

    private static Vec3 calculateViewVector(float pXRot, float pYRot) {
        float f = pXRot * ((float)Math.PI / 180F);
        float f1 = -pYRot * ((float)Math.PI / 180F);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double)(f3 * f4), (double)(-f5), (double)(f2 * f4));
    }
}
