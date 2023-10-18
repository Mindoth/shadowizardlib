package net.mindoth.shadowizardlib.client.curio;

import net.mindoth.shadowizardlib.ShadoWizardLib;
import net.mindoth.shadowizardlib.client.models.CloakModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class CurioLayers {

    public static final ModelLayerLocation
            SKILLCLOAK = createLayerLocation("skillcloak");

    public static ModelLayerLocation createLayerLocation(String name) {
        return new ModelLayerLocation(new ResourceLocation(ShadoWizardLib.MOD_ID, name), name);
    }

    private static Supplier<LayerDefinition> layer(MeshDefinition mesh, int textureWidth, int textureHeight) {
        return () -> LayerDefinition.create(mesh, textureWidth, textureHeight);
    }

    private static void register(EntityRenderersEvent.RegisterLayerDefinitions event, ModelLayerLocation layerLocation, Supplier<LayerDefinition> layer) {
        event.registerLayerDefinition(layerLocation, layer);
    }

    public static void register(EntityRenderersEvent.RegisterLayerDefinitions event) {
        register(event, SKILLCLOAK, layer(CloakModel.createCloak(), 64, 64));
    }
}
