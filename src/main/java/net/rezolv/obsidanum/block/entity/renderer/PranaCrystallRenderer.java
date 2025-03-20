package net.rezolv.obsidanum.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.rezolv.obsidanum.block.entity.PranaCrystallEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.*;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class PranaCrystallRenderer<T extends PranaCrystallEntity> implements BlockEntityRenderer<T> {

    private static final Map<BlockPos, PranaCrystallEntity> allOnScreen = new HashMap<>();
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0D) / 2.0D);
    private static final int SHINE_R = 69;
    private static final int SHINE_G = 206;
    private static final int SHINE_B = 162;

    private static final int SHINE_CENTER_R = 42;
    private static final int SHINE_CENTER_G = 255;
    private static final int SHINE_CENTER_B = 140;

    public PranaCrystallRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static void renderEntireBatch(LevelRenderer levelRenderer, PoseStack poseStack, int renderTick, Camera camera, float partialTick) {
        if (!allOnScreen.isEmpty()) {
            List<BlockPos> sortedPoses = new ArrayList<>(allOnScreen.keySet());
            sortedPoses.sort((blockPos1, blockPos2) -> sortBlockPos(camera, blockPos1, blockPos2));
            poseStack.pushPose();
            Vec3 cameraPos = camera.getPosition();
            poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            for (BlockPos pos : sortedPoses) {
                Vec3 blockAt = Vec3.atCenterOf(pos);
                poseStack.pushPose();
                poseStack.translate(blockAt.x, blockAt.y, blockAt.z);
                renderAt(allOnScreen.get(pos), partialTick, poseStack, bufferSource);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
        allOnScreen.clear();
    }

    private static int sortBlockPos(Camera camera, BlockPos blockPos1, BlockPos blockPos2) {
        double d1 = camera.getPosition().distanceTo(Vec3.atCenterOf(blockPos1));
        double d2 = camera.getPosition().distanceTo(Vec3.atCenterOf(blockPos2));
        return Double.compare(d2, d1);
    }

    @Override
    public void render(T pranaCrystall, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!pranaCrystall.isRemoved()) {
            allOnScreen.put(pranaCrystall.getBlockPos(), pranaCrystall);
        } else {
            allOnScreen.remove(pranaCrystall.getBlockPos());
        }
    }

    private static void renderAt(PranaCrystallEntity pranaCrystall, float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        float scale = 1.0F;
        float time = 0;
        if (Minecraft.getInstance().getCameraEntity() != null) {
            scale = pranaCrystall.calculateShineScale(Minecraft.getInstance().getCameraEntity().getPosition(partialTicks));
            time = Minecraft.getInstance().getCameraEntity().tickCount + partialTicks;
        }
        if (scale > 0.0F) {
            Quaternionf cameraOrientation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            float rotationTime = time * pranaCrystall.getRotSpeed() * 0.3F;
            float waveTime = time * 0.1F;
            poseStack.pushPose();
            poseStack.mulPose(cameraOrientation);
            VertexConsumer vertexConsumer = buffer.getBuffer(ACRenderTypes.getAmbersolShine());
            int lights = pranaCrystall.getLights();
            poseStack.mulPose(Axis.ZN.rotationDegrees(pranaCrystall.getRotOffset()));
            for (int i = 0; i < lights; i++) {
                float length = (float) (3F + Math.sin(waveTime + i * 2)) * scale;
                float width = (float) (1F - 0.2F * Math.abs(Math.cos(waveTime - i * Math.PI * 0.5F))) * scale;
                int alpha = 255;
                float u = 0;
                float v = 0;
                poseStack.pushPose();
                poseStack.mulPose(Axis.ZN.rotationDegrees(rotationTime - (i / (float) lights * 360)));
                PoseStack.Pose pose = poseStack.last();
                Matrix4f poseMatrix = pose.pose();
                Matrix3f normalMatrix = pose.normal();
                renderShineOrigin(vertexConsumer, poseMatrix, normalMatrix, alpha, u, v);
                renderShineLeftCorner(vertexConsumer, poseMatrix, normalMatrix, length, width, u, v);
                renderShineRightCorner(vertexConsumer, poseMatrix, normalMatrix, length, width, u, v);
                renderShineLeftCorner(vertexConsumer, poseMatrix, normalMatrix, length, width, u, v);
                poseStack.popPose();
            }
            // Minecraft's render system requires a selection box for transparent blocks
            PoseStack.Pose pose = poseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            VertexConsumer lines = buffer.getBuffer(RenderType.lines());
            poseStack.popPose();
        }
    }

    private static void renderShineOrigin(VertexConsumer vertexConsumer, Matrix4f poseMatrix, Matrix3f normalMatrix, int alpha, float u, float v) {
        vertexConsumer.vertex(poseMatrix, 0.0F, 0.0F, 0.0F)
                .color(SHINE_CENTER_R, SHINE_CENTER_G, SHINE_CENTER_B, 230)
                .uv(u + 0.5F, v)
                .overlayCoords(NO_OVERLAY)
                .uv2(240)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    private static void renderShineLeftCorner(VertexConsumer vertexConsumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float length, float width, float u, float v) {
        vertexConsumer.vertex(poseMatrix, -HALF_SQRT_3 * width, length, 0)
                .color(SHINE_R, SHINE_G, SHINE_B, 0)
                .uv(u, v + 1)
                .overlayCoords(NO_OVERLAY)
                .uv2(240)
                .normal(normalMatrix, 0.0F, -1.0F, 0.0F)
                .endVertex();
    }

    private static void renderShineRightCorner(VertexConsumer vertexConsumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float length, float width, float u, float v) {
        vertexConsumer.vertex(poseMatrix, HALF_SQRT_3 * width, length, 0)
                .color(SHINE_R, SHINE_G, SHINE_B, 0)
                .uv(u + 1, v + 1)
                .overlayCoords(NO_OVERLAY)
                .uv2(240)
                .normal(normalMatrix, 0.0F, -1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
