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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.rezolv.obsidanum.block.entity.ForgeCrucibleEntity;

import java.util.List;

public class ForgeCrucibleEntityRenderer implements BlockEntityRenderer<ForgeCrucibleEntity> {
    public ForgeCrucibleEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ForgeCrucibleEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        // Получаем список ингредиентов
        List<ItemStack> ingredients = pBlockEntity.getIngredients();

        long time = pBlockEntity.getLevel().getGameTime();
        double animationOffset = Math.sin((time + pPartialTick) * 0.1) * 0.025;
        pPoseStack.translate(0.5f, 1.75f + animationOffset, 0.5f);
        pPoseStack.scale(0.30f, 0.30f, 0.30f);

        pPoseStack.pushPose();

        Direction facing = pBlockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        switch (facing) {
            case NORTH:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case EAST:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(90));
                break;
            case SOUTH:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(0));
                break;
            case WEST:
                pPoseStack.mulPose(Axis.YP.rotationDegrees(270));
                break;
            default:
                break;
        }

        // Поднимаем золотое яблоко, если есть ингредиенты
        float appleYOffset = ingredients.isEmpty() ? 0.0f : 2.0f;

        // Отображение выходного предмета, если есть ингредиенты
        if (!ingredients.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.0f, 0.6f + appleYOffset, 0.0f);
            pPoseStack.scale(1.0f, 1.0f, 1.0f);

            ItemStack output = pBlockEntity.getOutput();

            // Проверяем, есть ли выходной предмет
            if (!output.isEmpty()) {
                itemRenderer.renderStatic(output, ItemDisplayContext.FIXED,
                        getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                        OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            }

            pPoseStack.popPose();
        }

        // Параметры размещения
        float offsetX = -1.8f; // Начальный сдвиг по оси X
        float offsetY = 1.2f; // Сдвиг по оси Y для следующего ряда (вниз)
        int itemsPerRow = 4;   // Максимальное количество элементов в ряду
        int rowCount = 0;      // Счётчик рядов

        // Отображаем каждый ингредиент с учётом сдвига
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack itemStack = ingredients.get(i);

            pPoseStack.pushPose();

            // Сдвиг по оси X для текущего элемента (в пределах текущего ряда)
            float rowOffsetX = offsetX + (i % itemsPerRow) * 1.2f;
            // Сдвиг по оси Y для следующего ряда (сдвигаем на следующую строку, если достигнут лимит по X)
            float rowOffsetY = (i / itemsPerRow) * offsetY;

            pPoseStack.translate(rowOffsetX, rowOffsetY, 0.0f); // Сдвиг по X и Y

            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED,
                    getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
            pPoseStack.popPose();
        }

        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

}