package net.rezolv.obsidanum.item.item_entity.arrows.flame_bolt;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;

public class FlameBoltRenderer extends ArrowRenderer<FlameBolt> {
    public static final ResourceLocation FLAME_ARROW = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/projectiles/flame_bolt.png");

    public FlameBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(FlameBolt flameBolt) {
        return FLAME_ARROW;
    }
}