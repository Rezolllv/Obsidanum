package net.rezolv.obsidanum.item.item_entity.pot_grenade;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.item.ItemsObs;
import net.rezolv.obsidanum.item.item_entity.obsidan_chakram.ObsidianChakramEntity;

public class PotGrenadeRenderer extends EntityRenderer<PotGrenade> {
    private final ItemRenderer itemRenderer;

    public PotGrenadeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PotGrenade entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        // Получаем позицию игрока
        Player player = entity.level().getNearestPlayer(entity, 20.0D);
        if (player != null) {
            // Вычисляем вектор направления от сущности к игроку
            Vec3 direction = player.getEyePosition(partialTicks).subtract(entity.getPosition(partialTicks)).normalize();

            // Вычисляем углы поворота
            float yaw = (float)Math.atan2(direction.x, direction.z);
            float pitch = (float)Math.asin(direction.y);

            // Применяем поворот
            poseStack.mulPose(Axis.YP.rotation(yaw));
            poseStack.mulPose(Axis.XP.rotation(pitch));
        }

        // Масштабирование
        poseStack.scale(0.8F, 0.8F, 0.8F);

        // Рендер самого предмета
        this.itemRenderer.renderStatic(
                entity.getItem(),
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId()
        );

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PotGrenade entity) {
        // Возвращаем текстуру предмета (рендерер предмета сам обработает)
        return this.itemRenderer.getItemModelShaper()
                .getItemModel(entity.getItem())
                .getParticleIcon().atlasLocation();
    }
}