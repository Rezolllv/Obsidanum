package net.rezolv.obsidanum.effect.effects.effect_overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.item.item_entity.pot_grenade.fog.PotGrenadeFog;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PreConfusionOverlay {
    private static final ResourceLocation[] OVERLAY_TEXTURES = {
            new ResourceLocation("obsidanum", "textures/overlay/morok_stage_1.png"),
            new ResourceLocation("obsidanum", "textures/overlay/morok_stage_2.png")
    };

    private static final Map<UUID, PlayerFogState> playerStates = new HashMap<>();

    public static final IGuiOverlay PRE_CONFUSION_OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        UUID playerId = player.getUUID();
        PlayerFogState state = playerStates.computeIfAbsent(playerId, k -> new PlayerFogState());

        // Обновляем состояние только для визуальных эффектов
        boolean currentlyInFog = isPlayerInFog(player);
        if (currentlyInFog) {
            state.fogExposureTime = Math.min(state.fogExposureTime + 1, PlayerFogState.MAX_EXPOSURE_TIME_LVL2);
        } else {
            state.fogExposureTime = Math.max(state.fogExposureTime - 1, 0);
        }

        MobEffectInstance flashEffect = player.getEffect(EffectsObs.FLASH.get());

        // Если эффект активен - показываем полный оверлей
        if (flashEffect != null) {
            int amplifier = flashEffect.getAmplifier();
            if (amplifier >= 1) {
                drawFullEffectOverlay(guiGraphics, screenWidth, screenHeight);
            } else {
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], 1.0f, screenWidth, screenHeight);
            }
        }
        // Иначе показываем прогрессивный оверлей
        else if (state.fogExposureTime > 0) {
            // Первый слой появляется сразу с нарастанием прозрачности
            float stage1Alpha = Math.min(state.fogExposureTime / (float)PlayerFogState.MAX_EXPOSURE_TIME_LVL1, 1.0f);
            drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], stage1Alpha, screenWidth, screenHeight);

            // Второй слой появляется после достижения первого порога
            if (state.fogExposureTime > PlayerFogState.MAX_EXPOSURE_TIME_LVL1) {
                float stage2Progress = (state.fogExposureTime - PlayerFogState.MAX_EXPOSURE_TIME_LVL1) /
                        (float)(PlayerFogState.MAX_EXPOSURE_TIME_LVL2 - PlayerFogState.MAX_EXPOSURE_TIME_LVL1);
                float stage2Alpha = Math.min(stage2Progress, 1.0f);
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[1], stage2Alpha, screenWidth, screenHeight);
            }
        }

        // Очистка состояний для неактивных игроков
        playerStates.keySet().removeIf(uuid -> {
            if (minecraft.level == null) return true;
            Player p = minecraft.level.getPlayerByUUID(uuid);
            return p == null || !p.isAlive();
        });
    };


    private static boolean isPlayerInFog(Player player) {
        return player.level().getEntitiesOfClass(PotGrenadeFog.class, player.getBoundingBox())
                .stream()
                .anyMatch(fog -> fog.getBoundingBox().intersects(player.getBoundingBox()));
    }

    private static void drawFullEffectOverlay(GuiGraphics guiGraphics, int width, int height) {
        drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], 1.0f, width, height);
        drawOverlay(guiGraphics, OVERLAY_TEXTURES[1], 1.0f, width, height);
    }

    private static void drawOverlay(GuiGraphics guiGraphics, ResourceLocation texture, float alpha, int width, int height) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(0, height, 0).uv(0, 1).endVertex();
        buffer.vertex(width, height, 0).uv(1, 1).endVertex();
        buffer.vertex(width, 0, 0).uv(1, 0).endVertex();
        buffer.vertex(0, 0, 0).uv(0, 0).endVertex();
        tesselator.end();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}