package net.mindoth.shadowizardlib.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class CommonEvents {
    public static Vec3 getEntityCenter(LivingEntity entity) {
        return entity.getBoundingBox().getCenter();
    }
}
