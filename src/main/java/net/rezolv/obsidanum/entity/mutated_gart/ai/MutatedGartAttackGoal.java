package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;

public class MutatedGartAttackGoal extends MeleeAttackGoal {
    private final MutatedGart entity;
    private static final int ATTACK_WINDUP = 15; // Момент нанесения удара
    private static final int ATTACK_COOLDOWN = 40; // Общая длительность цикла атаки (задержка между атаками)
    private int attackTimer = 0; // Таймер атаки

    // Конструктор
    public MutatedGartAttackGoal(MutatedGart gart, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(gart, speedModifier, followingTargetEvenIfNotSeen);
        this.entity = gart;
    }

    // Момент нанесения удара – только когда цель очень близко (4 блока)
    @Override
    protected double getAttackReachSqr(LivingEntity enemy) {
        return 16.0; // 4 блока в квадрате
    }

    // Активация melee-цели, если цель в пределах 7 блоков (7² = 49)
    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null
                && target.isAlive()
                && this.entity.distanceToSqr(target) <= 49.0D;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null
                && target.isAlive()
                && this.entity.distanceToSqr(target) <= 49.0D;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        if (target == null || !target.isAlive()) {
            stop();
            return;
        }
        double distanceSqr = this.entity.distanceToSqr(target);
        // Если цель вне предела удара (4 блока), сбрасываем таймер атаки
        if (distanceSqr > getAttackReachSqr(target)) {
            resetAttack();
            return;
        }
        // Остановка навигации, чтобы сущность не двигалась во время атаки
        this.entity.getNavigation().stop();

        if (attackTimer == 0) {
            // Запуск анимации удара (сбрасываем магическую анимацию)
            entity.setAttacking(true);
            entity.magicAttackAnimationState.stop();
        }

        attackTimer++;

        if (attackTimer == ATTACK_WINDUP) {
            // На ATTACK_WINDUP наносим урон
            performAttack(target);
        } else if (attackTimer >= ATTACK_COOLDOWN) {
            // Сброс таймера для повторения атаки
            resetAttack();
        }
    }

    private void performAttack(LivingEntity enemy) {
        this.entity.swing(InteractionHand.MAIN_HAND);
        this.entity.doHurtTarget(enemy);
    }

    private void resetAttack() {
        attackTimer = 0;
        entity.setAttacking(false);
        entity.magicAttackAnimationState.stop();
    }

    @Override
    public void stop() {
        resetAttack();
        super.stop();
    }
}