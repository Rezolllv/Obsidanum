package net.rezolv.obsidanum.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.rezolv.obsidanum.block.custom.ForgeCrucible;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

public class ForgeCrucibleEntityRenderer implements BlockEntityRenderer<ForgeCrucibleEntity> {
    public ForgeCrucibleEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ForgeCrucibleEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = new ItemStack(Items.BREAD); // Заменяем на статичный хлеб

        long time = pBlockEntity.getLevel().getGameTime(); // Текущий игровой тик
        double animationOffset = Math.sin((time + pPartialTick) * 0.1) * 0.025; // Вверх-вниз с плавным движением
        pPoseStack.translate(0.5f, 1.75f + animationOffset, 0.5f);
        pPoseStack.scale(0.35f, 0.35f, 0.35f);


        pPoseStack.pushPose();

        Direction facing = pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing) {
            case NORTH:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180)); // Rotate 180 degrees for NORTH
                break;
            case EAST:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90));  // Rotate 90 degrees for EAST
                break;
            case SOUTH:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(0));   // No rotation for SOUTH
                break;
            case WEST:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270)); // Rotate 270 degrees for WEST
                break;
            default:
                break;
        }

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();
    }
    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}