package net.rezolv.obsidanum.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.rezolv.obsidanum.effect.EffectsObs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    // Внедряем наш код в существующий метод hurt
public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        // Проверка на наличие эффекта заморозки и огненного источника урона
        if (livingEntity.hasEffect(EffectsObs.PROTECTION_ARROW.get()) && source.is(DamageTypeTags.IS_PROJECTILE)) {
            cir.setReturnValue(false);  // Блокируем урон от огня, если есть эффект сопротивления огню
        }
    }
}