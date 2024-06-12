package net.rezolv.obsidanum.entity.obsidian_elemental;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.entity.ModModelLayers;

public class ObsidianElementalRenderer extends MobRenderer<ObsidianElemental, ObsidianElementalModel<ObsidianElemental>>

    {
    public ObsidianElementalRenderer(EntityRendererProvider.Context context) {
        super(context, new ObsidianElementalModel<>(context.bakeLayer(ModModelLayers.OBSIDIAN_ELEMENTAL)), 0.5f);
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