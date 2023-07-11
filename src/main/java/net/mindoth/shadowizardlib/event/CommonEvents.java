package net.mindoth.shadowizardlib.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class CommonEvents {

    //Pass any LivingEntity into this and you get its hitbox' centre
    public static Vector3d getEntityCenter(LivingEntity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        List<Entity> targets = center.level.getEntities(center, center.getBoundingBox().inflate(x, y, z));
        return targets;
    }
}
