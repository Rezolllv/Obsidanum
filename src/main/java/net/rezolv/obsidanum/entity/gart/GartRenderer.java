package net.rezolv.obsidanum.entity.gart;

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

public class GartRenderer extends MobRenderer<Gart, GartModel<Gart>>

{
    public GartRenderer(EntityRendererProvider.Context context) {
        super(context, new GartModel<>(context.bakeLayer(ModModelLayers.GART)), 0.2f);

        this.addLayer(new RenderLayer<Gart, GartModel<Gart>>(this) {
            final ResourceLocation GART_EYES = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/gart/gart_eyes.png");

            @Override
            public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, Gart entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(GART_EYES));
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 1), 1, 1, 1, 1);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(Gart pEntity) {
        return new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/gart/gart.png");
    }

    @Override
    public void render(Gart pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.2f, 0.2f, 0.2f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}