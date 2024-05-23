package net.rezolv.obsidanum.item.item_entity.arrows.obsidian_arrow;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;

public class ObsidianArrowRenderer extends ArrowRenderer<ObsidianArrow> {
    public static final ResourceLocation OBSIDIAN_ARROW = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/projectiles/obsidian_arrow.png");

    public ObsidianArrowRenderer(EntityRendererProvider.Context p_174399_) {
        super(p_174399_);
    }

    public ResourceLocation getTextureLocation(ObsidianArrow p_116001_) {
        return OBSIDIAN_ARROW;
    }
}