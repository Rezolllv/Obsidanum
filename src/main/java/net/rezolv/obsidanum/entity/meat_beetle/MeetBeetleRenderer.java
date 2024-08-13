package net.rezolv.obsidanum.entity.meat_beetle;

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

public class MeetBeetleRenderer extends MobRenderer<MeetBeetle, MeetBeetleModel<MeetBeetle>>

    {
    public MeetBeetleRenderer(EntityRendererProvider.Context context) {
        super(context, new MeetBeetleModel<>(context.bakeLayer(ModModelLayers.MEET_BEETLE)), 0.2f);


    }

    @Override
    public ResourceLocation getTextureLocation(MeetBeetle pEntity) {
        return new ResourceLocation(Obsidanum.MOD_ID, "textures/entity/meet_beetle/meet_beetle.png");
    }

    @Override
    public void render(MeetBeetle pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
            MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.2f, 0.2f, 0.2f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}