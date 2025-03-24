package net.rezolv.obsidanum.item.item_entity.arrows.netherite_bolt;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;

public class NetheriteBoltRenderer extends ArrowRenderer<NetheriteBolt> {
    public static final ResourceLocation NETHERITE_BOLT = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/projectiles/netherite_bolt.png");

    public NetheriteBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(NetheriteBolt flameArrow) {
        return NETHERITE_BOLT;
    }
}