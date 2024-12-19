package net.rezolv.obsidanum.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.effect.EffectsObs;
import net.rezolv.obsidanum.effect.effects.effect_overlay.ConfusionOverlay;

@Mod.EventBusSubscriber(modid = Obsidanum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    @SubscribeEvent
    public static void onLivingVisibilityCheck(LivingEvent.LivingVisibilityEvent event) {
        // Проверяем, является ли сущность игроком
        if (event.getEntity() instanceof ServerPlayer player) {
            // Проверяем, есть ли у игрока эффект Inviolability
            MobEffectInstance inviolabilityEffect = player.getEffect(EffectsObs.INVIOLABILITY.get());

            // Если эффект есть, то меняем видимость
            if (inviolabilityEffect != null) {
                // Убедимся, что не происходит ошибка при изменении видимости
                try {
                    // Если эффект есть, ставим видимость 0.0 (невидимость)
                    event.modifyVisibility(0.0);
                } catch (Exception e) {
                    // Логируем ошибку, если что-то пошло не так
                    Obsidanum.LOGGER.error("Ошибка при изменении видимости: " + e.getMessage(), e);
                }
            }
        }
    }
}