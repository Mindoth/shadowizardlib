package net.mindoth.shadowizardlib.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.ItemAttributeModifierEvent;

import java.util.List;
import java.util.UUID;

public class CommonEvents {

    //Pass any LivingEntity into this and you get its hitbox' centre
    public static Vector3d getEntityCenter(Entity entity) {
        return entity.getBoundingBox().getCenter();
    }

    //Pass any Entity into this along with three Doubles and you get a List of all other entities touching its hitbox inflated by the Doubles you gave
    public static List<Entity> getEntitiesAround(Entity center, double x, double y, double z) {
        return center.level.getEntities(center, center.getBoundingBox().inflate(x, y, z));
    }

    //Pass an iten attribute and the uuid for said attribute and it will be removed. Usually used inside ItemAttributeModifierEvent for changing attack damage/speed of item
    private static void findAndRemoveVanillaModifier(ItemAttributeModifierEvent event, Attribute attribute, UUID baseUUID) {
        event.getOriginalModifiers()
                .get(attribute)
                .stream()
                .filter(modifier -> modifier.getId() == baseUUID)
                .findAny()
                .ifPresent(modifier -> event.removeModifier(attribute, modifier));
    }
}
