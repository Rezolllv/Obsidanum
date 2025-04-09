package net.rezolv.obsidanum.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.Obsidanum;
import net.rezolv.obsidanum.effect.EffectsObs;

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
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectsObs.FLASH.get())) {
            int amplifier = entity.getEffect(EffectsObs.FLASH.get()).getAmplifier();

            float originalDamage = event.getAmount();
            float multiplier = 1.0f;

            if (amplifier == 0) {
                multiplier = 1.15f; // +15%
            } else if (amplifier == 1) {
                multiplier = 6.40f; // +40%
            }

            event.setAmount(originalDamage * multiplier);
        }
    }

}