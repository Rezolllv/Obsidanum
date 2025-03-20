package net.rezolv.obsidanum.chests.events;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.chests.client.render.ObsidanumChestRenderer;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ObsidanumChestsClientEvents {

  public static final ModelLayerLocation IRON_CHEST = new ModelLayerLocation(new ResourceLocation(Obsidanum.MOD_ID, "iron_chest"), "main");

  @SubscribeEvent
  public static void layerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    event.registerLayerDefinition(IRON_CHEST, ObsidanumChestRenderer::createBodyLayer);
  }
}
