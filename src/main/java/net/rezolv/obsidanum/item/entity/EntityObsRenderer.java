package net.rezolv.obsidanum.item.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramRenderer;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.PotGrenadeRenderer;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.fog.PotGrenadeFogRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class EntityObsRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntitiesItem.OBSIDIAN_CHAKRAM.get(), ObsidianChakramRenderer::new);
        event.registerEntityRenderer(ModEntitiesItem.POT_GRENADE.get(), PotGrenadeRenderer::new);
        event.registerEntityRenderer(ModEntitiesItem.POT_GRENADE_FOG.get(), PotGrenadeFogRenderer::new);
    }
}
