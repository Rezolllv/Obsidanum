package net.rezolv.obsidanum.item.item_entity.pot_grenade.fog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PotGrenadeFogRenderer extends EntityRenderer<PotGrenadeFog> {
    public PotGrenadeFogRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PotGrenadeFog entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Реализация рендеринга облака
        // Можно скопировать логику из AreaEffectCloudRenderer
    }

    @Override
    public ResourceLocation getTextureLocation(PotGrenadeFog entity) {
        return new ResourceLocation("textures/entity/area_effect_cloud.png");
    }
}