package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;

public class MutatedGartAttackGoal extends MeleeAttackGoal {
    private final MutatedGart entity; // Сущность, к которой привязана цель
    private static final int ATTACK_WINDUP = 15; // Удар наносится на 15-м тике
    private static final int ATTACK_COOLDOWN = 30; // Общая длительность анимации (15 тиков)
    private int attackTimer = 0; // Таймер атаки

    // Конструктор
    public MutatedGartAttackGoal(MutatedGart gart, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(gart, speedModifier, followingTargetEvenIfNotSeen);
        this.entity = gart;
    }

    // Определение дальности атаки
    @Override
    protected double getAttackReachSqr(LivingEntity enemy) {
        return 16.0; // 4 блока в квадрате
    }

    // Проверка, можно ли использовать атаку
    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.distanceTo(target) <= 4.0D;
    }

    // Обновление логики атаки
    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            stop(); // Остановка, если цель мертва или отсутствует
            return;
        }
        double distanceSqr = this.entity.distanceToSqr(target);
        if (distanceSqr > getAttackReachSqr(target)) {
            resetAttack(); // Сброс атаки, если цель вне досягаемости
            return;
        }
        // Остановка навигации, чтобы сущность не двигалась во время атаки
        this.entity.getNavigation().stop();

        if (attackTimer == 0) {
            // Запуск анимации удара, если она ещё не запущена
            entity.setAttacking(true);
            entity.magicAttackAnimationState.stop(); // Остановка магической анимации
        }

        attackTimer++; // Увеличение таймера

        if (attackTimer == ATTACK_WINDUP) {
            // На 15-м тике наносим урон
            performAttack(target);
        } else if (attackTimer >= ATTACK_COOLDOWN) {
            // Сброс таймера для повторения атаки
            resetAttack();
        }
    }

    // Нанесение урона
    private void performAttack(LivingEntity enemy) {
        this.entity.swing(InteractionHand.MAIN_HAND); // Анимация удара
        this.entity.doHurtTarget(enemy); // Нанесение урона цели
    }

    // Сброс состояния атаки
    private void resetAttack() {
        attackTimer = 0; // Сброс таймера
        entity.setAttacking(false); // Остановка анимации атаки
        entity.magicAttackAnimationState.stop(); // Остановка магической анимации
    }

    // Остановка цели
    @Override
    public void stop() {
        resetAttack(); // Сброс атаки
        super.stop();
    }
}