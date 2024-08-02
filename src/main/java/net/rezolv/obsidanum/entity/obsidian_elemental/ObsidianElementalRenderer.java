package net.rezolv.obsidanum.entity.obsidian_elemental;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModModelLayers;

public class ObsidianElementalRenderer extends MobRenderer<ObsidianElemental, ObsidianElementalModel<ObsidianElemental>>

    {
    public ObsidianElementalRenderer(EntityRendererProvider.Context context) {
        super(context, new ObsidianElementalModel<>(context.bakeLayer(ModModelLayers.OBSIDIAN_ELEMENTAL)), 0.5f);
        this.addLayer(new RenderLayer<ObsidianElemental, ObsidianElementalModel<ObsidianElemental>>(this) {
            final ResourceLocation ELEMENTAL_EYES = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/obsidian_elemental/obsidian_elemental_eyes.png");

            @Override
            public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, ObsidianElemental entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(ELEMENTAL_EYES));
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 1), 1, 1, 1, 1);
            }
        });

    }

    @Override
    public ResourceLocation getTextureLocation(ObsidianElemental pEntity) {
        return new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/obsidian_elemental/obsidian_elemental.png");
    }

    @Override
    public void render(ObsidianElemental pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
            MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}