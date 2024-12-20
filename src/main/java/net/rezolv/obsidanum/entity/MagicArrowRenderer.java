package net.rezolv.obsidanum.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;

public class MagicArrowRenderer<T extends Entity & ItemSupplier> extends ThrownItemRenderer<T> {

    public MagicArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight) {
        // Увеличиваем масштаб модели в 3 раза
        poseStack.pushPose();
        poseStack.scale(3.0F, 3.0F, 3.0F);
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
    @Override
    protected int getBlockLightLevel(T pEntity, BlockPos pPos) {
        return 15;
    }
}