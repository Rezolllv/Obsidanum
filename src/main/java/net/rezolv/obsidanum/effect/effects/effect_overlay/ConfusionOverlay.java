package net.rezolv.obsidanum.effect.effects.effect_overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.rezolv.obsidanum.effect.EffectsObs;

public class ConfusionOverlay {
    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("obsidanum", "textures/misc/flash.png");

    public static void renderOverlay(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            MobEffectInstance effectInstance = mc.player.getEffect(EffectsObs.FLASH.get());
            if (effectInstance != null) {
                int duration = effectInstance.getDuration(); // Оставшееся время эффекта в тиках

                // Линейная интерполяция прогресса (появление за 1 секунду, исчезновение за 1 секунду)
                float progress = Math.max(0.0f, Math.min(1.0f, (40.0f - duration) / 40.0f));

                // Плавное увеличение альфа-канала для появления
                float alpha;
                if (progress < 0.5f) {
                    alpha = progress * 2.0f; // Линейное увеличение в первую половину
                } else {
                    alpha = 1.0f - (progress - 0.5f) * 2.0f; // Линейное уменьшение во вторую половину
                }

                // Оверлей появляется, пока эффект активен, и исчезает по мере его окончания
                if (alpha > 0.0f) {
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderTexture(0, OVERLAY_TEXTURE);
                    RenderSystem.defaultBlendFunc();

                    // Установка альфа-канала
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

                    int screenWidth = mc.getWindow().getGuiScaledWidth();
                    int screenHeight = mc.getWindow().getGuiScaledHeight();
                    guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

                    RenderSystem.disableBlend();
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f); // Сброс цвета
                }
            }
        }
    }
}