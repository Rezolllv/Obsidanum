package net.rezolv.obsidanum.entity;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.obsidian_elemental.ObsidianElementalModel;

public class ModModelLayers {
    public static final ModelLayerLocation OBSIDIAN_ELEMENTAL = new ModelLayerLocation(new ResourceLocation("obsidanum", "obsidian_elemental"), "main");

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(OBSIDIAN_ELEMENTAL, ObsidianElementalModel::createBodyLayer);
    }
}
