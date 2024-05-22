package net.rezolv.obsidanum.item.item_entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ObsidianChakramRenderer extends EntityRenderer<ObsidianChakramEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("obsidanum", "textures/entity/obsidian_chakram.png");
    private final ChakramModelEntity model;

    public ObsidianChakramRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ChakramModelEntity(context.bakeLayer(ChakramModelEntity.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(ObsidianChakramEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ObsidianChakramEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.0D, 0.0D); // Adjust model position here if necessary
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, entityYaw, entity.getXRot());
        VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}