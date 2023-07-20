package net.mindoth.shadowizardlib.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CommonEvents {

    //Pass any Entity into this and you get its hitbox' centre
    public static Vec3 getEntityCenter(Entity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        List<Entity> targets = center.level().getEntities(center, center.getBoundingBox().inflate(x, y, z));
        return targets;
    }
}
