package net.rezolv.obsidanum.item.item_entity.arrows.flame_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow.ObsidianArrow;

public class FlameArrowRenderer extends ArrowRenderer<FlameArrow> {
    public static final ResourceLocation FLAME_ARROW = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/projectiles/flame_arrow.png");

    public FlameArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(FlameArrow flameArrow) {
        return FLAME_ARROW;
    }
}