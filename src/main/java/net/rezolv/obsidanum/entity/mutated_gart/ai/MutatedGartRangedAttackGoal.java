package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;

public class MutatedGartRangedAttackGoal extends Goal {
    private final MutatedGart mob;
    private final float attackRadiusSq;
    private int attackCooldown = 0;
    private int animationProgress = 0;
    private static final int ATTACK_DELAY = 20; // Тиков до выстрела
    private static final int RECHARGE_DELAY = 20; // Тиков перезарядки

    public MutatedGartRangedAttackGoal(MutatedGart mob, double speed, float radius) {
        this.mob = mob;
        this.attackRadiusSq = radius * radius;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        // Добавлена проверка: дистанция должна быть не меньше 7 блоков (49)
        return target != null
                && mob.distanceToSqr(target) >= 49.0F
                && mob.distanceToSqr(target) <= attackRadiusSq
                && mob.hasLineOfSight(target);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        // Если цель стала ближе 7 блоков, прекращаем дальнюю атаку
        return target != null
                && mob.distanceToSqr(target) >= 49.0F
                && mob.hasLineOfSight(target);
    }

    @Override
    public void start() {
        mob.getNavigation().stop();
        mob.setMagicAttacking(true);
        animationProgress = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Если кулдаун активен, уменьшаем его
        if (attackCooldown > 0) {
            attackCooldown--;
        } else {
            // Продвигаем анимацию атаки, если кулдауна нет
            animationProgress++;
            if (animationProgress == ATTACK_DELAY) {
                // На момент ATTACK_DELAY выполняем дальний выстрел
                mob.performRangedAttack(target, 1.0F);
            }
            if (animationProgress >= RECHARGE_DELAY) {
                // По окончании цикла сбрасываем анимацию и устанавливаем кулдаун,
                // после чего цикл повторится, как только кулдаун обнулится.
                mob.magicAttackAnimationState.stop();
                attackCooldown = RECHARGE_DELAY;
                animationProgress = 0;
            }
        }
    }

    @Override
    public void stop() {
        mob.setMagicAttacking(false);
        animationProgress = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}