package net.rezolv.obsidanum.entity.mutated_gart;

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
import net.rezolv.obsidanum.entity.gart.Gart;
import net.rezolv.obsidanum.entity.gart.GartModel;

public class MutatedGartRenderer extends MobRenderer<MutatedGart, MutatedGartModel<MutatedGart>>

    {
    public MutatedGartRenderer(EntityRendererProvider.Context context) {
        super(context, new MutatedGartModel<>(context.bakeLayer(ModModelLayers.MUTATED_GART)), 0.5f);
        this.addLayer(new RenderLayer<MutatedGart, MutatedGartModel<MutatedGart>>(this) {
            final ResourceLocation MUTATED_GART_EYES = new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/mutated_gart/mutated_gart_eyes.png");

            @Override
            public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, MutatedGart entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.eyes(MUTATED_GART_EYES));
                this.getParentModel().renderToBuffer(poseStack, vertexConsumer, light, LivingEntityRenderer.getOverlayCoords(entity, 1), 1, 1, 1, 1);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(MutatedGart pEntity) {
        return new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/mutated_gart/mutated_gart.png");
    }

    @Override
    public void render(MutatedGart pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
            MultiBufferSource pBuffer, int pPackedLight) {
            pMatrixStack.scale(1.5f, 1.5f, 1.5f);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}