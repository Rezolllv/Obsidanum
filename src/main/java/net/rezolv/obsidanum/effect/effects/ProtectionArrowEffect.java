package net.rezolv.obsidanum.effect.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rezolv.obsidanum.effect.EffectsObs;
@Mod.EventBusSubscriber
public class ProtectionArrowEffect extends MobEffect {

    public ProtectionArrowEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }
    // Слушатель событий для защиты от стрел
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        // Проверяем, что это стрела
        if (event.getProjectile() instanceof Arrow arrow) {
            // Проверяем, есть ли у сущности эффект защиты
            LivingEntity shooter = (LivingEntity) arrow.getOwner();
            if (shooter != null && shooter.hasEffect(EffectsObs.PROTECTION_ARROW.get())) {
                // Удаляем урон от стрелы
                event.getProjectile().discard(); // Удаляем стрелу
                return; // Завершаем выполнение метода
            }
        }
    }
    // Слушатель событий для отмены урона от стрел
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        // Проверяем, является ли источник урона стрелой
        if (event.getSource().getMsgId().equals("arrow")) {
            // Проверяем, есть ли у сущности эффект защиты
            LivingEntity entity = event.getEntity();
            if (entity.hasEffect(EffectsObs.PROTECTION_ARROW.get())) {
                event.setCanceled(true); // Отменяем урон
            }
        }
    }


    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}