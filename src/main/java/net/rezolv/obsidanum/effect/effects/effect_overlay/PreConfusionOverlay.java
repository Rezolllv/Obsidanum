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

        // Проверка нахождения игрока в тумане
        boolean currentlyInFog = isPlayerInFog(player);
        if (currentlyInFog) {
            if (!state.isInFog) {
                state.isInFog = true;
            }
            // Наращиваем экспозицию до максимума второго уровня
            state.fogExposureTime = Math.min(state.fogExposureTime + 1, PlayerFogState.MAX_EXPOSURE_TIME_LVL2);
        } else {
            if (state.isInFog) {
                state.isInFog = false;
            }
            state.fogExposureTime = Math.max(state.fogExposureTime - 1, 0);
        }

        // Применение эффекта уровня 1 (FLASH I)
        if (state.fogExposureTime >= PlayerFogState.MAX_EXPOSURE_TIME_LVL1 && state.appliedEffectLevel == 0) {
            player.addEffect(new MobEffectInstance(EffectsObs.FLASH.get(), PlayerFogState.EFFECT_DURATION, 0));
            state.appliedEffectLevel = 1;
            System.out.println("[DEBUG] Применён FLASH I для игрока " + player.getName().getString());
        }
        // Обновление эффекта до уровня 2 (FLASH II), если ранее был FLASH I
        if (state.fogExposureTime >= PlayerFogState.MAX_EXPOSURE_TIME_LVL2 && state.appliedEffectLevel == 1) {
            player.addEffect(new MobEffectInstance(EffectsObs.FLASH.get(), PlayerFogState.EFFECT_DURATION, 1));
            state.appliedEffectLevel = 2;
            System.out.println("[DEBUG] Обновлён эффект до FLASH II для игрока " + player.getName().getString());
        }

        // Обработка исчезновения эффекта: если время эффекта истекло, сбрасываем состояние
        MobEffectInstance flashInstance = player.getEffect(EffectsObs.FLASH.get());
        if (flashInstance != null && flashInstance.getDuration() <= 0) {
            player.removeEffect(EffectsObs.FLASH.get());
            flashInstance = null;
            System.out.println("[DEBUG] Удалён эффект FLASH у игрока " + player.getName().getString());
        }
        if (state.appliedEffectLevel > 0 && flashInstance == null) {
            state.fogExposureTime = 0;
            state.isInFog = false;
            state.appliedEffectLevel = 0;
            System.out.println("[DEBUG] Сброшено состояние эффекта для игрока " + player.getName().getString());
        }

        // Очистка записей для неактивных игроков
        playerStates.keySet().removeIf(uuid -> {
            if (minecraft.level == null) return true;
            Player p = minecraft.level.getPlayerByUUID(uuid);
            return p == null || !p.isAlive();
        });

        // Отрисовка оверлея
        if (flashInstance != null) {
            int amplifier = flashInstance.getAmplifier();
            if (amplifier == 0) {
                // FLASH I: первый слой полностью, второй — постепенно
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], 1.0f, screenWidth, screenHeight);

                float alpha2 = (state.fogExposureTime - PlayerFogState.MAX_EXPOSURE_TIME_LVL1)
                        / (float)(PlayerFogState.MAX_EXPOSURE_TIME_LVL2 - PlayerFogState.MAX_EXPOSURE_TIME_LVL1);
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[1], Math.min(alpha2, 1.0f), screenWidth, screenHeight);
            } else if (amplifier >= 1) {
                drawFullEffectOverlay(guiGraphics, screenWidth, screenHeight);
            }
        } else if (state.fogExposureTime >= 1) {
            // Фаза накопления экспозиции: оба слоя появляются постепенно перед вторым порогом
            if (state.fogExposureTime < PlayerFogState.MAX_EXPOSURE_TIME_LVL1) {
                // Пока до первого порога – только первый слой появляется (но его альфа вычисляется по MAX_EXPOSURE_TIME_LVL2)
                float alpha1 = state.fogExposureTime / PlayerFogState.MAX_EXPOSURE_TIME_LVL2;
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], Math.min(alpha1, 1.0f), screenWidth, screenHeight);
            } else {
                // При значении экспозиции от первого порога и выше – оба слоя появляются постепенно
                // Первый слой: альфа растёт линейно от (MAX_EXPOSURE_TIME_LVL1/MAX_EXPOSURE_TIME_LVL2) до 1
                float alpha1 = state.fogExposureTime / PlayerFogState.MAX_EXPOSURE_TIME_LVL2;
                // Второй слой: начинает с 0 после первого порога и растёт до 1 к моменту достижения второго порога
                float alpha2 = (state.fogExposureTime - PlayerFogState.MAX_EXPOSURE_TIME_LVL1)
                        / (PlayerFogState.MAX_EXPOSURE_TIME_LVL2 - PlayerFogState.MAX_EXPOSURE_TIME_LVL1);
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[0], Math.min(alpha1, 1.0f), screenWidth, screenHeight);
                drawOverlay(guiGraphics, OVERLAY_TEXTURES[1], Math.min(alpha2, 1.0f), screenWidth, screenHeight);
            }
        } else {
            // Если нечего отображать – сбрасываем состояние
            state.fogExposureTime = 0;
            state.isInFog = false;
            state.appliedEffectLevel = 0;
        }
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