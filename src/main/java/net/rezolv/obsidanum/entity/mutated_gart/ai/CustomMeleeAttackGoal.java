package net.rezolv.obsidanum.entity.mutated_gart.ai;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.rezolv.obsidanum.entity.mutated_gart.MutatedGart;

public class CustomMeleeAttackGoal extends MeleeAttackGoal {
    private final MutatedGart mutatedGart;

    public CustomMeleeAttackGoal(MutatedGart gart, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(gart, speedModifier, followingTargetEvenIfNotSeen);
        this.mutatedGart = gart;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.getTarget() != null && this.mob.distanceTo(this.mob.getTarget()) <= this.getAttackReachSqr(this.mob.getTarget())) {
            if (!mutatedGart.isAttacking()) {
                mutatedGart.setAttacking(true);
                mutatedGart.attackAnimationState.start(mutatedGart.tickCount); // Начало анимации
            }
        }
    }
}