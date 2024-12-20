package net.rezolv.obsidanum.entity.projectile_entity;


import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.entity.GlowingThrownItemRenderer;
import net.rezolv.obsidanum.entity.MagicArrowRenderer;
import net.rezolv.obsidanum.entity.ModItemEntities;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ModMobRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModItemEntities.NETHER_FLAME_ENTITY.get(), GlowingThrownItemRenderer::new);
        event.registerEntityRenderer(ModItemEntities.MAGIC_ARROW_NETHER_FLAME_ENTITY.get(), MagicArrowRenderer::new);
        event.registerEntityRenderer(ModItemEntities.NETHER_FLAME_ENTITY_MINI.get(), GlowingThrownItemRenderer::new);
    }

}