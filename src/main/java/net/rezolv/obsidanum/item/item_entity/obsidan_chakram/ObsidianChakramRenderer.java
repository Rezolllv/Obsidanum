package net.rezolv.obsidanum.item.item_entity.obsidan_chakram;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.item.ItemsObs;

public class ObsidianChakramRenderer extends EntityRenderer<ObsidianChakramEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("obsidanum", "textures/entity/projectiles/obsidian_chakram.png");
    private final ItemRenderer itemRenderer;

    public ObsidianChakramRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(ObsidianChakramEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS; // Используем текстуру атласа для рендеринга предмета
    }

    @Override
    public void render(ObsidianChakramEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        float computedYaw, computedPitch;

        if (entity.isStopped()) {
            // Используем синхронизированные углы
            computedYaw = entity.getStoppedYaw() + 90.0F; // Смещение для ориентации
            computedPitch = entity.getStoppedPitch();
        } else {
            Vec3 motion = entity.getDeltaMovement();
            float horizontalSpeed = (float) Math.sqrt(motion.x * motion.x + motion.z * motion.z);
            if (horizontalSpeed > 0.001F) {
                computedYaw = (float) (Math.atan2(motion.x, motion.z) * (180 / Math.PI));
                computedPitch = (float) (Math.atan2(motion.y, horizontalSpeed) * (180 / Math.PI));
            } else {
                computedYaw = entityYaw;
                computedPitch = (motion.y > 0) ? -90.0F : 90.0F;
            }
            computedYaw += 90.0F; // Смещение для ориентации
        }

        // Применяем повороты
        poseStack.mulPose(Axis.YP.rotationDegrees(computedYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(computedPitch));

        // Вращение в полете
        if (!entity.isStopped()) {
            float spinAngle = (entity.tickCount + partialTicks) * -20.0F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(spinAngle));
        }

        // Корректировка позиции
        poseStack.translate(0.0D, -0.1D, 0.0D);

        // Рендер модели
        ItemStack itemStack = new ItemStack(ItemsObs.OBSIDIAN_CHAKRAM.get());
        BakedModel model = itemRenderer.getModel(itemStack, entity.level(), null, 0);
        itemRenderer.render(itemStack, ItemDisplayContext.GROUND, false, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}