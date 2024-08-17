package net.rezolv.obsidanum.entity.gart;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModModelLayers;

public class GartRenderer extends MobRenderer<Gart, GartModel<Gart>>

{
    public GartRenderer(EntityRendererProvider.Context context) {
        super(context, new GartModel<>(context.bakeLayer(ModModelLayers.GART)), 0.2f);


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